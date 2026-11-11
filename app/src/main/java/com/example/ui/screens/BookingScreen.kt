package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Appointment
import com.example.data.model.Barber
import com.example.data.model.BarbershopService
import com.example.ui.theme.BorderGoldMuted
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceHigh
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusPending
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BarbershopViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun BookingScreen(
    viewModel: BarbershopViewModel,
    modifier: Modifier = Modifier
) {
    val services by viewModel.services.collectAsState()
    val barbers by viewModel.barbers.collectAsState()
    val selectedService by viewModel.selectedService.collectAsState()
    val selectedBarber by viewModel.selectedBarber.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedTimeSlot by viewModel.selectedTimeSlot.collectAsState()
    val bookedSlots by viewModel.bookedSlots.collectAsState()

    val customerName by viewModel.customerName.collectAsState()
    val customerPhone by viewModel.customerPhone.collectAsState()
    val customerEmail by viewModel.customerEmail.collectAsState()
    val bookingNotes by viewModel.bookingNotes.collectAsState()

    val confirmedBooking by viewModel.confirmedBooking.collectAsState()
    val bookingError by viewModel.bookingError.collectAsState()

    val scrollState = rememberScrollState()

    // 14 calendar booking dates
    val upcomingDates = remember { generateUpcomingDates(14) }

    val allTimeSlots = listOf(
        "09:00 AM", "09:45 AM", "10:30 AM", "11:15 AM",
        "12:00 PM", "01:30 PM", "02:15 PM", "03:00 PM",
        "03:45 PM", "04:30 PM", "05:15 PM", "06:00 PM", "06:45 PM"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .padding(bottom = 90.dp)
    ) {
        // Confirmation Ticket View if booked!
        if (confirmedBooking != null) {
            BookingConfirmationCard(
                booking = confirmedBooking!!,
                onViewMyBookings = {
                    viewModel.setClientPhoneSearch(confirmedBooking!!.customerPhone)
                    viewModel.navigateTo(AppScreen.MY_BOOKINGS)
                },
                onBookAnother = {
                    viewModel.resetBookingForm()
                }
            )
            return@Column
        }

        // Header Title
        Text(
            text = "RESERVE YOUR CHAIR",
            style = MaterialTheme.typography.labelSmall,
            color = GoldPrimary,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Bespoke Appointment",
            style = MaterialTheme.typography.displaySmall,
            color = TextPrimary
        )
        Text(
            text = "Select your desired service, master barber, and preferred time slot.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Error banner if any
        if (bookingError != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF4A1818))
                    .border(1.dp, Color(0xFFEF5350), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = bookingError!!,
                    color = Color(0xFFFFCDD2),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // STEP 1: SERVICE SELECTION
        SectionTitle(stepNumber = "1", title = "Select Service")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            services.forEach { service ->
                val isSelected = selectedService?.id == service.id
                SelectableServiceCard(
                    service = service,
                    isSelected = isSelected,
                    onSelect = { viewModel.selectService(service) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // STEP 2: MASTER BARBER SELECTION
        SectionTitle(stepNumber = "2", title = "Select Master Barber")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // "Any Available Barber" Option
            SelectableBarberChip(
                name = "Any Barber",
                title = "Fastest Availability",
                isSelected = selectedBarber == null,
                imageRes = null,
                onSelect = { viewModel.selectBarber(null) }
            )

            barbers.forEach { barber ->
                val imageRes = if (barber.drawableName == "barber_sophia") R.drawable.barber_sophia else R.drawable.barber_elias
                SelectableBarberChip(
                    name = barber.name,
                    title = barber.title.split("&").first().trim(),
                    isSelected = selectedBarber?.id == barber.id,
                    imageRes = imageRes,
                    onSelect = { viewModel.selectBarber(barber) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // STEP 3: DATE SELECTION
        SectionTitle(stepNumber = "3", title = "Choose Date")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            upcomingDates.forEach { dateItem ->
                val isSelected = selectedDate == dateItem.formattedDate
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) GoldPrimary else DarkSurfaceVariant)
                        .border(
                            1.dp,
                            if (isSelected) GoldPrimary else BorderSubtle,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { viewModel.selectDate(dateItem.formattedDate) }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .testTag("date_chip_${dateItem.formattedDate}"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = dateItem.dayOfWeek,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) DarkBackground else TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = dateItem.dayOfMonth,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) DarkBackground else TextPrimary
                        )
                        Text(
                            text = dateItem.monthName,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = if (isSelected) DarkBackground else TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // STEP 4: AVAILABLE TIME SLOTS
        SectionTitle(stepNumber = "4", title = "Select Available Time Slot")
        Text(
            text = "Showing slots for ${formatDisplayDate(selectedDate)} (Unavailable slots are booked)",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Slots grid
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val chunkedSlots = allTimeSlots.chunked(3)
            chunkedSlots.forEach { rowSlots ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowSlots.forEach { slot ->
                        val isBooked = bookedSlots.contains(slot)
                        val isSelected = selectedTimeSlot == slot

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    when {
                                        isBooked -> DarkSurface.copy(alpha = 0.5f)
                                        isSelected -> GoldPrimary
                                        else -> DarkSurfaceVariant
                                    }
                                )
                                .border(
                                    1.dp,
                                    when {
                                        isBooked -> BorderSubtle.copy(alpha = 0.3f)
                                        isSelected -> GoldPrimary
                                        else -> BorderSubtle
                                    },
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable(enabled = !isBooked) {
                                    viewModel.selectTimeSlot(slot)
                                }
                                .testTag("time_slot_$slot"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isBooked) "$slot\n(Booked)" else slot,
                                textAlign = TextAlign.Center,
                                fontSize = if (isBooked) 10.sp else 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = when {
                                    isBooked -> TextMuted.copy(alpha = 0.5f)
                                    isSelected -> DarkBackground
                                    else -> TextPrimary
                                }
                            )
                        }
                    }
                    if (rowSlots.size < 3) {
                        repeat(3 - rowSlots.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // STEP 5: CUSTOMER INFORMATION
        SectionTitle(stepNumber = "5", title = "Gentleman's Contact Details")

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(BorderGoldMuted, BorderSubtle))),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Name
                OutlinedTextField(
                    value = customerName,
                    onValueChange = { viewModel.customerName.value = it },
                    label = { Text("Full Name *") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = GoldPrimary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_customer_name"),
                    singleLine = true,
                    colors = textFieldColors()
                )

                // Phone
                OutlinedTextField(
                    value = customerPhone,
                    onValueChange = { viewModel.customerPhone.value = it },
                    label = { Text("Phone Number * (for SMS & WhatsApp pass)") },
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = GoldPrimary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_customer_phone"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = textFieldColors()
                )

                // Email
                OutlinedTextField(
                    value = customerEmail,
                    onValueChange = { viewModel.customerEmail.value = it },
                    label = { Text("Email Address (Optional)") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = GoldPrimary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_customer_email"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = textFieldColors()
                )

                // Notes
                OutlinedTextField(
                    value = bookingNotes,
                    onValueChange = { viewModel.bookingNotes.value = it },
                    label = { Text("Special Requests / Grooming Notes") },
                    placeholder = { Text("e.g. skin sensitivity, hot towel preferences...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_customer_notes"),
                    maxLines = 3,
                    colors = textFieldColors()
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ORDER SUMMARY CARD
        if (selectedService != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceHigh),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(GoldPrimary, BorderGoldMuted))),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "APPOINTMENT SUMMARY",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldPrimary,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = selectedService!!.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Rs ${"%,.0f".format(selectedService!!.price)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Barber: ${selectedBarber?.name ?: "Any Available Master Barber"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Text(
                        text = "Schedule: ${formatDisplayDate(selectedDate)} at ${selectedTimeSlot ?: "Select Time Slot"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (selectedTimeSlot != null) GoldLight else TextMuted
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // CONFIRM BUTTON
        Button(
            onClick = {
                viewModel.submitBooking {
                    // Success callback
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_booking_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = DarkBackground
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.ConfirmationNumber, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Confirm & Book Chair",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun SectionTitle(stepNumber: String, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(GoldPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber,
                color = DarkBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}

@Composable
private fun SelectableServiceCard(
    service: BarbershopService,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("selectable_service_${service.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) DarkSurfaceHigh else DarkSurface
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (isSelected) listOf(GoldPrimary, GoldLight)
                else listOf(BorderSubtle, BorderSubtle)
            )
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) GoldPrimary else DarkBackground)
                    .border(1.dp, if (isSelected) GoldPrimary else BorderGoldMuted, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(DarkBackground)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) GoldLight else TextPrimary
                )
                Text(
                    text = "${service.durationMinutes} min • ${service.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Text(
                text = "Rs ${"%,.0f".format(service.price)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldLight
            )
        }
    }
}

@Composable
private fun SelectableBarberChip(
    name: String,
    title: String,
    isSelected: Boolean,
    imageRes: Int?,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(130.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) DarkSurfaceHigh else DarkSurface)
            .border(
                1.5.dp,
                if (isSelected) GoldPrimary else BorderSubtle,
                RoundedCornerShape(14.dp)
            )
            .clickable { onSelect() }
            .padding(10.dp)
            .testTag("barber_chip_${name.replace(" ", "_")}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = name,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(1.dp, if (isSelected) GoldPrimary else BorderGoldMuted, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(BorderGoldMuted),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCut,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) GoldLight else TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun BookingConfirmationCard(
    booking: Appointment,
    onViewMyBookings: () -> Unit,
    onBookAnother: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(StatusApproved.copy(alpha = 0.2f))
                .border(2.dp, StatusApproved, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = StatusApproved,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "CHAIR RESERVED!",
            style = MaterialTheme.typography.displaySmall,
            color = GoldPrimary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Your appointment request has been submitted to Crown & Blade parlour.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Luxury Digital Ticket Pass
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("booking_confirmation_pass"),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(GoldPrimary, BorderGoldMuted))),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "APPOINTMENT PASS",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldPrimary,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = booking.id,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(StatusPending.copy(alpha = 0.2f))
                            .border(1.dp, StatusPending, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = booking.status,
                            color = StatusPending,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = BorderSubtle)
                Spacer(modifier = Modifier.height(14.dp))

                TicketDetailRow(label = "Gentleman", value = booking.customerName)
                TicketDetailRow(label = "Phone", value = booking.customerPhone)
                TicketDetailRow(label = "Service", value = booking.serviceName)
                TicketDetailRow(label = "Master Barber", value = booking.barberName)
                TicketDetailRow(label = "Date & Time", value = "${formatDisplayDate(booking.date)} at ${booking.timeSlot}")
                TicketDetailRow(label = "Total Investment", value = "Rs ${"%,.0f".format(booking.servicePrice)}")

                if (booking.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Notes: ${booking.notes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onViewMyBookings,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("view_my_bookings_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = DarkBackground
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("View Passes", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onBookAnother,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("book_another_btn"),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(BorderGoldMuted, GoldPrimary))
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("New Booking", color = GoldLight)
            }
        }
    }
}

@Composable
fun TicketDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
    }
}

data class DateItem(
    val formattedDate: String, // "YYYY-MM-DD"
    val dayOfWeek: String,     // "MON"
    val dayOfMonth: String,    // "12"
    val monthName: String      // "NOV"
)

fun generateUpcomingDates(count: Int): List<DateItem> {
    val list = mutableListOf<DateItem>()
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, 1) // start tomorrow

    val sdfYmd = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val sdfDow = SimpleDateFormat("EEE", Locale.US)
    val sdfDom = SimpleDateFormat("dd", Locale.US)
    val sdfMon = SimpleDateFormat("MMM", Locale.US)

    repeat(count) {
        list.add(
            DateItem(
                formattedDate = sdfYmd.format(cal.time),
                dayOfWeek = sdfDow.format(cal.time).uppercase(),
                dayOfMonth = sdfDom.format(cal.time),
                monthName = sdfMon.format(cal.time).uppercase()
            )
        )
        cal.add(Calendar.DAY_OF_YEAR, 1)
    }
    return list
}

fun formatDisplayDate(dateStr: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val formatter = SimpleDateFormat("EEE, MMM dd", Locale.US)
        val date = parser.parse(dateStr) ?: return dateStr
        formatter.format(date)
    } catch (e: Exception) {
        dateStr
    }
}

@Composable
fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GoldPrimary,
    unfocusedBorderColor = BorderSubtle,
    focusedLabelColor = GoldPrimary,
    unfocusedLabelColor = TextMuted,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    cursorColor = GoldPrimary
)
