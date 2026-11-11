package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Appointment
import com.example.ui.theme.BorderGoldMuted
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceHigh
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusCancelled
import com.example.ui.theme.StatusCompleted
import com.example.ui.theme.StatusPending
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BarbershopViewModel

@Composable
fun MyAppointmentsScreen(
    viewModel: BarbershopViewModel,
    modifier: Modifier = Modifier
) {
    val allAppointments by viewModel.appointments.collectAsState()
    val phoneSearch by viewModel.clientPhoneSearch.collectAsState()

    val filteredAppointments = if (phoneSearch.isBlank()) {
        allAppointments
    } else {
        allAppointments.filter { it.customerPhone.contains(phoneSearch, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .padding(bottom = 90.dp)
    ) {
        Text(
            text = "GENTLEMAN'S PASSES",
            style = MaterialTheme.typography.labelSmall,
            color = GoldPrimary,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Appointment Passes",
            style = MaterialTheme.typography.displaySmall,
            color = TextPrimary
        )
        Text(
            text = "Track your reservation status or look up bookings by phone number.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Phone Search Bar
        OutlinedTextField(
            value = phoneSearch,
            onValueChange = { viewModel.setClientPhoneSearch(it) },
            label = { Text("Filter by your phone number") },
            placeholder = { Text("e.g. 555-234-8901") },
            leadingIcon = {
                Icon(Icons.Default.Phone, contentDescription = null, tint = GoldPrimary)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("client_search_phone_input"),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            colors = textFieldColors()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredAppointments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = null,
                        tint = GoldPrimary.copy(alpha = 0.5f),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "No Passes Found",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "You don't have any appointments matching this phone number yet.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.BOOKING) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = DarkBackground
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Reserve a Chair", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredAppointments, key = { it.id }) { appt ->
                    ClientPassCard(
                        appointment = appt,
                        onCancel = {
                            viewModel.updateAppointmentStatus(appt.id, "CANCELLED")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ClientPassCard(
    appointment: Appointment,
    onCancel: () -> Unit
) {
    val (statusColor, statusBg) = when (appointment.status) {
        "APPROVED" -> StatusApproved to StatusApproved.copy(alpha = 0.15f)
        "COMPLETED" -> StatusCompleted to StatusCompleted.copy(alpha = 0.15f)
        "CANCELLED", "REJECTED" -> StatusCancelled to StatusCancelled.copy(alpha = 0.15f)
        else -> StatusPending to StatusPending.copy(alpha = 0.15f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("pass_card_${appointment.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (appointment.status == "APPROVED") listOf(GoldPrimary, BorderGoldMuted)
                else listOf(BorderSubtle, BorderSubtle)
            )
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = appointment.id,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusBg)
                        .border(1.dp, statusColor.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = appointment.status,
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderSubtle)
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = appointment.serviceName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Master Barber: ${appointment.barberName}",
                style = MaterialTheme.typography.bodyMedium,
                color = GoldPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Scheduled: ${formatDisplayDate(appointment.date)} at ${appointment.timeSlot}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Text(
                text = "Client: ${appointment.customerName} (${appointment.customerPhone})",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rs ${"%,.0f".format(appointment.servicePrice)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )

                if (appointment.status == "PENDING" || appointment.status == "APPROVED") {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("cancel_pass_${appointment.id}"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusCancelled),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(listOf(StatusCancelled, StatusCancelled))
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel Pass", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
