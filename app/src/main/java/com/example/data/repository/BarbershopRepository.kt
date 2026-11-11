package com.example.data.repository

import com.example.data.local.BarbershopDao
import com.example.data.model.Appointment
import com.example.data.model.Barber
import com.example.data.model.BarbershopService
import com.example.data.model.Customer
import com.example.data.model.Review
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class BarbershopRepository(private val dao: BarbershopDao) {

    val allBarbers: Flow<List<Barber>> = dao.getAllBarbers()
    val activeBarbers: Flow<List<Barber>> = dao.getActiveBarbers()
    val allServices: Flow<List<BarbershopService>> = dao.getAllServices()
    val activeServices: Flow<List<BarbershopService>> = dao.getActiveServices()
    val allAppointments: Flow<List<Appointment>> = dao.getAllAppointments()
    val allReviews: Flow<List<Review>> = dao.getAllReviews()
    val allCustomers: Flow<List<Customer>> = dao.getAllCustomers()

    fun getAppointmentsForCustomer(phone: String): Flow<List<Appointment>> {
        return dao.getAppointmentsForCustomer(phone)
    }

    suspend fun getBookedSlotsForDate(date: String): Set<String> {
        val booked = dao.getBookedAppointmentsForDate(date)
        return booked.map { it.timeSlot }.toSet()
    }

    suspend fun bookAppointment(
        customerName: String,
        customerPhone: String,
        customerEmail: String,
        barberId: String,
        barberName: String,
        serviceId: String,
        serviceName: String,
        servicePrice: Double,
        date: String,
        timeSlot: String,
        notes: String
    ): Appointment {
        val shortId = "CB-${(1000..9999).random()}"
        val appointment = Appointment(
            id = shortId,
            customerName = customerName.trim(),
            customerPhone = customerPhone.trim(),
            customerEmail = customerEmail.trim(),
            barberId = barberId,
            barberName = barberName,
            serviceId = serviceId,
            serviceName = serviceName,
            servicePrice = servicePrice,
            date = date,
            timeSlot = timeSlot,
            status = "PENDING",
            notes = notes.trim(),
            createdAt = System.currentTimeMillis()
        )
        dao.insertAppointment(appointment)

        // Upsert customer
        val existingCustomer = Customer(
            phone = customerPhone.trim(),
            name = customerName.trim(),
            email = customerEmail.trim(),
            totalBookings = 1,
            totalSpent = servicePrice,
            lastVisit = date
        )
        dao.upsertCustomer(existingCustomer)

        return appointment
    }

    suspend fun updateAppointmentStatus(appointmentId: String, newStatus: String) {
        dao.updateAppointmentStatus(appointmentId, newStatus)
    }

    suspend fun deleteAppointment(appointmentId: String) {
        dao.deleteAppointmentById(appointmentId)
    }

    // Service management
    suspend fun addService(service: BarbershopService) {
        dao.insertService(service)
    }

    suspend fun updateService(service: BarbershopService) {
        dao.updateService(service)
    }

    suspend fun deleteService(service: BarbershopService) {
        dao.deleteService(service)
    }

    // Barber management
    suspend fun addBarber(barber: Barber) {
        dao.insertBarber(barber)
    }

    suspend fun updateBarber(barber: Barber) {
        dao.updateBarber(barber)
    }

    suspend fun deleteBarber(barber: Barber) {
        dao.deleteBarber(barber)
    }
}
