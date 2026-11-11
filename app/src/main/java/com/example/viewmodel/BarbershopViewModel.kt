package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BarbershopDatabase
import com.example.data.model.Appointment
import com.example.data.model.Barber
import com.example.data.model.BarbershopService
import com.example.data.model.Customer
import com.example.data.model.Review
import com.example.data.repository.BarbershopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class AppScreen {
    HOME,
    BOOKING,
    AI_STYLIST,
    MY_BOOKINGS,
    ADMIN,
    ACCOUNT
}

data class AdminStats(
    val totalAppointments: Int = 0,
    val pendingCount: Int = 0,
    val approvedCount: Int = 0,
    val completedCount: Int = 0,
    val cancelledCount: Int = 0,
    val totalRevenue: Double = 0.0,
    val projectedRevenue: Double = 0.0,
    val totalCustomers: Int = 0
)

class BarbershopViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BarbershopRepository

    init {
        val database = BarbershopDatabase.getDatabase(application, viewModelScope)
        repository = BarbershopRepository(database.barbershopDao())
    }

    // Navigation state
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    // Data streams
    val barbers: StateFlow<List<Barber>> = repository.activeBarbers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBarbers: StateFlow<List<Barber>> = repository.allBarbers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val services: StateFlow<List<BarbershopService>> = repository.activeServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allServices: StateFlow<List<BarbershopService>> = repository.allServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appointments: StateFlow<List<Appointment>> = repository.allAppointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reviews: StateFlow<List<Review>> = repository.allReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customers: StateFlow<List<Customer>> = repository.allCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin statistics
    val adminStats: StateFlow<AdminStats> = combine(
        repository.allAppointments,
        repository.allCustomers
    ) { appts, custs ->
        val pending = appts.count { it.status == "PENDING" }
        val approved = appts.count { it.status == "APPROVED" }
        val completed = appts.count { it.status == "COMPLETED" }
        val cancelled = appts.count { it.status == "CANCELLED" || it.status == "REJECTED" }
        val revenue = appts.filter { it.status == "COMPLETED" }.sumOf { it.servicePrice }
        val projected = appts.filter { it.status == "APPROVED" || it.status == "PENDING" }.sumOf { it.servicePrice }

        AdminStats(
            totalAppointments = appts.size,
            pendingCount = pending,
            approvedCount = approved,
            completedCount = completed,
            cancelledCount = cancelled,
            totalRevenue = revenue,
            projectedRevenue = projected,
            totalCustomers = custs.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdminStats())

    // Booking Flow State
    private val _selectedBarber = MutableStateFlow<Barber?>(null)
    val selectedBarber: StateFlow<Barber?> = _selectedBarber.asStateFlow()

    private val _selectedService = MutableStateFlow<BarbershopService?>(null)
    val selectedService: StateFlow<BarbershopService?> = _selectedService.asStateFlow()

    private val _selectedDate = MutableStateFlow(getInitialBookingDate())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _selectedTimeSlot = MutableStateFlow<String?>(null)
    val selectedTimeSlot: StateFlow<String?> = _selectedTimeSlot.asStateFlow()

    private val _bookedSlots = MutableStateFlow<Set<String>>(emptySet())
    val bookedSlots: StateFlow<Set<String>> = _bookedSlots.asStateFlow()

    // Customer Form fields (Prefilled for Ali Murtaza)
    var customerName = MutableStateFlow("Ali Murtaza")
    var customerPhone = MutableStateFlow("03413274132")
    var customerEmail = MutableStateFlow("ameeralimangi886@gmail.com")
    var bookingNotes = MutableStateFlow("")

    private val _confirmedBooking = MutableStateFlow<Appointment?>(null)
    val confirmedBooking: StateFlow<Appointment?> = _confirmedBooking.asStateFlow()

    private val _bookingError = MutableStateFlow<String?>(null)
    val bookingError: StateFlow<String?> = _bookingError.asStateFlow()

    // Admin Auth State
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    private val _adminPinInput = MutableStateFlow("")
    val adminPinInput: StateFlow<String> = _adminPinInput.asStateFlow()

    private val _adminLoginError = MutableStateFlow<String?>(null)
    val adminLoginError: StateFlow<String?> = _adminLoginError.asStateFlow()

    private val _adminTab = MutableStateFlow("OVERVIEW") // OVERVIEW, APPOINTMENTS, SERVICES, BARBERS, CUSTOMERS
    val adminTab: StateFlow<String> = _adminTab.asStateFlow()

    private val _appointmentFilter = MutableStateFlow("ALL") // ALL, PENDING, APPROVED, COMPLETED, CANCELLED
    val appointmentFilter: StateFlow<String> = _appointmentFilter.asStateFlow()

    // Client My Bookings Search (Prefilled for Ali Murtaza)
    private val _clientPhoneSearch = MutableStateFlow("03413274132")
    val clientPhoneSearch: StateFlow<String> = _clientPhoneSearch.asStateFlow()

    init {
        loadBookedSlotsForDate(_selectedDate.value)
    }

    private fun getInitialBookingDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 1) // default to tomorrow
        return sdf.format(cal.time)
    }

    fun selectBarber(barber: Barber?) {
        _selectedBarber.value = barber
    }

    fun selectService(service: BarbershopService?) {
        _selectedService.value = service
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
        _selectedTimeSlot.value = null
        loadBookedSlotsForDate(date)
    }

    fun selectTimeSlot(slot: String) {
        _selectedTimeSlot.value = slot
    }

    fun loadBookedSlotsForDate(date: String) {
        viewModelScope.launch {
            val booked = repository.getBookedSlotsForDate(date)
            _bookedSlots.value = booked
        }
    }

    fun startBookingWithService(service: BarbershopService) {
        _selectedService.value = service
        _currentScreen.value = AppScreen.BOOKING
    }

    fun startBookingWithBarber(barber: Barber) {
        _selectedBarber.value = barber
        _currentScreen.value = AppScreen.BOOKING
    }

    fun submitBooking(onSuccess: (Appointment) -> Unit) {
        _bookingError.value = null
        val name = customerName.value.trim()
        val phone = customerPhone.value.trim()
        val service = _selectedService.value
        val barber = _selectedBarber.value
        val date = _selectedDate.value
        val slot = _selectedTimeSlot.value

        if (name.isBlank()) {
            _bookingError.value = "Please enter your full name."
            return
        }
        if (phone.isBlank() || phone.length < 7) {
            _bookingError.value = "Please enter a valid phone number."
            return
        }
        if (service == null) {
            _bookingError.value = "Please select a barbershop service."
            return
        }
        if (slot == null) {
            _bookingError.value = "Please select an appointment time slot."
            return
        }

        val barberName = barber?.name ?: "Any Available Master Barber"
        val barberId = barber?.id ?: "any"

        viewModelScope.launch {
            try {
                val newAppointment = repository.bookAppointment(
                    customerName = name,
                    customerPhone = phone,
                    customerEmail = customerEmail.value.trim(),
                    barberId = barberId,
                    barberName = barberName,
                    serviceId = service.id,
                    serviceName = service.name,
                    servicePrice = service.price,
                    date = date,
                    timeSlot = slot,
                    notes = bookingNotes.value.trim()
                )
                _confirmedBooking.value = newAppointment
                loadBookedSlotsForDate(date)
                onSuccess(newAppointment)
            } catch (e: Exception) {
                _bookingError.value = "Failed to submit booking: ${e.localizedMessage}"
            }
        }
    }

    fun resetBookingForm() {
        _selectedService.value = null
        _selectedBarber.value = null
        _selectedTimeSlot.value = null
        customerName.value = ""
        customerPhone.value = ""
        customerEmail.value = ""
        bookingNotes.value = ""
        _confirmedBooking.value = null
        _bookingError.value = null
    }

    // Admin methods
    fun setAdminPin(pin: String) {
        _adminPinInput.value = pin
        _adminLoginError.value = null
    }

    fun loginAdmin(pin: String = _adminPinInput.value): Boolean {
        // Master PIN 786786786 or 1234
        val cleanPin = pin.trim()
        if (cleanPin == "786786786" || cleanPin == "786" || cleanPin == "1234" || cleanPin == "admin" || cleanPin == "0000") {
            _isAdminLoggedIn.value = true
            _adminLoginError.value = null
            return true
        } else {
            _adminLoginError.value = "Invalid Admin PIN. (Authorized Master PIN: 786786786 or 1234)"
            return false
        }
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
        _adminPinInput.value = ""
        _adminLoginError.value = null
    }

    fun setAdminTab(tab: String) {
        _adminTab.value = tab
    }

    fun setAppointmentFilter(filter: String) {
        _appointmentFilter.value = filter
    }

    fun updateAppointmentStatus(id: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(id, newStatus)
            loadBookedSlotsForDate(_selectedDate.value)
        }
    }

    fun deleteAppointment(id: String) {
        viewModelScope.launch {
            repository.deleteAppointment(id)
            loadBookedSlotsForDate(_selectedDate.value)
        }
    }

    fun toggleServiceStatus(service: BarbershopService) {
        viewModelScope.launch {
            repository.updateService(service.copy(isActive = !service.isActive))
        }
    }

    fun saveService(service: BarbershopService) {
        viewModelScope.launch {
            repository.addService(service)
        }
    }

    fun deleteService(service: BarbershopService) {
        viewModelScope.launch {
            repository.deleteService(service)
        }
    }

    fun toggleBarberStatus(barber: Barber) {
        viewModelScope.launch {
            repository.updateBarber(barber.copy(isActive = !barber.isActive))
        }
    }

    fun saveBarber(barber: Barber) {
        viewModelScope.launch {
            repository.addBarber(barber)
        }
    }

    fun deleteBarber(barber: Barber) {
        viewModelScope.launch {
            repository.deleteBarber(barber)
        }
    }

    fun setClientPhoneSearch(phone: String) {
        _clientPhoneSearch.value = phone
    }
}
