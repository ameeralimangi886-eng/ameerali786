package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Appointment
import com.example.data.model.Barber
import com.example.data.model.BarbershopService
import com.example.data.model.Customer
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
import com.example.ui.theme.StatusCancelled
import com.example.ui.theme.StatusCompleted
import com.example.ui.theme.StatusPending
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.BarbershopViewModel
import java.util.UUID

@Composable
fun AdminDashboardScreen(
    viewModel: BarbershopViewModel,
    modifier: Modifier = Modifier
) {
    val isLoggedIn by viewModel.isAdminLoggedIn.collectAsState()

    if (!isLoggedIn) {
        AdminLoginView(viewModel = viewModel, modifier = modifier)
    } else {
        AdminPortalView(viewModel = viewModel, modifier = modifier)
    }
}

@Composable
private fun AdminLoginView(
    viewModel: BarbershopViewModel,
    modifier: Modifier = Modifier
) {
    val pin by viewModel.adminPinInput.collectAsState()
    val error by viewModel.adminLoginError.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(DarkSurfaceVariant)
                .border(2.dp, GoldPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "ADMIN PORTAL",
            style = MaterialTheme.typography.labelSmall,
            color = GoldPrimary,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Crown & Blade Management",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter master passcode to access bookings, revenue analytics, staff roster, and services.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (error != null) {
            Text(
                text = error!!,
                color = StatusCancelled,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        OutlinedTextField(
            value = pin,
            onValueChange = { viewModel.setAdminPin(it) },
            label = { Text("Master PIN (Authorized: 786786786 or 1234)") },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = GoldPrimary)
            },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle PIN visibility",
                        tint = TextMuted
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_pin_input"),
            colors = textFieldColors(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.loginAdmin() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("admin_login_submit_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = DarkBackground
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Unlock Dashboard", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick 1-tap demo unlock button
        OutlinedButton(
            onClick = { viewModel.loginAdmin("786786786") },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("admin_quick_demo_login_btn"),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = Brush.horizontalGradient(listOf(BorderGoldMuted, GoldPrimary))
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("1-Tap Master Admin Access (PIN 786786786)", color = GoldLight, fontSize = 12.sp)
        }
    }
}

@Composable
private fun AdminPortalView(
    viewModel: BarbershopViewModel,
    modifier: Modifier = Modifier
) {
    val stats by viewModel.adminStats.collectAsState()
    val adminTab by viewModel.adminTab.collectAsState()
    val appointments by viewModel.appointments.collectAsState()
    val services by viewModel.allServices.collectAsState()
    val barbers by viewModel.allBarbers.collectAsState()
    val customers by viewModel.customers.collectAsState()
    val filter by viewModel.appointmentFilter.collectAsState()

    var showAddServiceDialog by remember { mutableStateOf(false) }
    var showAddBarberDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .padding(bottom = 90.dp)
    ) {
        // Admin Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "EXECUTIVE CONSOLE",
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldPrimary,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Parlour Management",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
            }

            IconButton(
                onClick = { viewModel.logoutAdmin() },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant)
                    .border(1.dp, BorderSubtle, CircleShape)
                    .testTag("admin_logout_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = StatusCancelled,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // STATS OVERVIEW CARDS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AdminStatMiniCard(
                modifier = Modifier.weight(1f),
                title = "Total",
                value = stats.totalAppointments.toString(),
                icon = Icons.Default.ContentCut,
                color = TextPrimary
            )
            AdminStatMiniCard(
                modifier = Modifier.weight(1f),
                title = "Pending",
                value = stats.pendingCount.toString(),
                icon = Icons.Default.PendingActions,
                color = StatusPending
            )
            AdminStatMiniCard(
                modifier = Modifier.weight(1f),
                title = "Approved",
                value = stats.approvedCount.toString(),
                icon = Icons.Default.TaskAlt,
                color = StatusApproved
            )
            AdminStatMiniCard(
                modifier = Modifier.weight(1f),
                title = "Revenue",
                value = "Rs ${"%,.0f".format(stats.totalRevenue)}",
                icon = Icons.Default.AttachMoney,
                color = GoldLight
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ADMIN TAB SELECTOR
        val tabs = listOf(
            "OVERVIEW" to "Bookings",
            "SERVICES" to "Services",
            "BARBERS" to "Staff",
            "CUSTOMERS" to "Clients"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEach { (key, label) ->
                val isSelected = adminTab == key
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) GoldPrimary else DarkSurfaceVariant)
                        .border(1.dp, if (isSelected) GoldPrimary else BorderSubtle, RoundedCornerShape(20.dp))
                        .clickable { viewModel.setAdminTab(key) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("admin_tab_$key")
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) DarkBackground else TextSecondary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // CONTENT BY TAB
        when (adminTab) {
            "SERVICES" -> {
                AdminServicesTab(
                    services = services,
                    onToggle = { viewModel.toggleServiceStatus(it) },
                    onDelete = { viewModel.deleteService(it) },
                    onAddNew = { showAddServiceDialog = true }
                )
            }
            "BARBERS" -> {
                AdminBarbersTab(
                    barbers = barbers,
                    onToggle = { viewModel.toggleBarberStatus(it) },
                    onDelete = { viewModel.deleteBarber(it) },
                    onAddNew = { showAddBarberDialog = true }
                )
            }
            "CUSTOMERS" -> {
                AdminCustomersTab(customers = customers)
            }
            else -> {
                // OVERVIEW & APPOINTMENTS
                AdminAppointmentsTab(
                    appointments = appointments,
                    filter = filter,
                    onFilterChange = { viewModel.setAppointmentFilter(it) },
                    onUpdateStatus = { id, status -> viewModel.updateAppointmentStatus(id, status) },
                    onDelete = { viewModel.deleteAppointment(it) }
                )
            }
        }
    }

    // Dialogs
    if (showAddServiceDialog) {
        AddServiceDialog(
            onDismiss = { showAddServiceDialog = false },
            onConfirm = { service ->
                viewModel.saveService(service)
                showAddServiceDialog = false
            }
        )
    }

    if (showAddBarberDialog) {
        AddBarberDialog(
            onDismiss = { showAddBarberDialog = false },
            onConfirm = { barber ->
                viewModel.saveBarber(barber)
                showAddBarberDialog = false
            }
        )
    }
}

@Composable
private fun AdminStatMiniCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(BorderGoldMuted.copy(alpha = 0.5f), BorderSubtle))),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color,
                fontSize = 14.sp
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun AdminAppointmentsTab(
    appointments: List<Appointment>,
    filter: String,
    onFilterChange: (String) -> Unit,
    onUpdateStatus: (String, String) -> Unit,
    onDelete: (String) -> Unit
) {
    val filters = listOf("ALL", "PENDING", "APPROVED", "COMPLETED", "CANCELLED")

    val filteredList = when (filter) {
        "PENDING" -> appointments.filter { it.status == "PENDING" }
        "APPROVED" -> appointments.filter { it.status == "APPROVED" }
        "COMPLETED" -> appointments.filter { it.status == "COMPLETED" }
        "CANCELLED" -> appointments.filter { it.status == "CANCELLED" || it.status == "REJECTED" }
        else -> appointments
    }

    Column {
        // Filter row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            filters.forEach { opt ->
                val selected = filter == opt
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (selected) GoldPrimary.copy(alpha = 0.2f) else DarkSurface)
                        .border(1.dp, if (selected) GoldPrimary else BorderSubtle, RoundedCornerShape(6.dp))
                        .clickable { onFilterChange(opt) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("filter_$opt")
                ) {
                    Text(
                        text = opt,
                        color = if (selected) GoldLight else TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No appointments matching filter.", color = TextMuted, fontSize = 13.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { appt ->
                    AdminAppointmentCard(
                        appointment = appt,
                        onApprove = { onUpdateStatus(appt.id, "APPROVED") },
                        onReject = { onUpdateStatus(appt.id, "REJECTED") },
                        onComplete = { onUpdateStatus(appt.id, "COMPLETED") },
                        onDelete = { onDelete(appt.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminAppointmentCard(
    appointment: Appointment,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onComplete: () -> Unit,
    onDelete: () -> Unit
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
            .testTag("admin_appt_card_${appointment.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(BorderGoldMuted.copy(alpha = 0.5f), BorderSubtle))),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${appointment.id} • ${appointment.customerName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 14.sp
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusBg)
                        .border(1.dp, statusColor.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = appointment.status,
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${appointment.serviceName} (Rs ${"%,.0f".format(appointment.servicePrice)})",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = GoldLight,
                fontSize = 13.sp
            )

            Text(
                text = "Staff: ${appointment.barberName} | ${formatDisplayDate(appointment.date)} at ${appointment.timeSlot}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 11.sp
            )

            Text(
                text = "Phone: ${appointment.customerPhone}",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                fontSize = 11.sp
            )

            if (appointment.notes.isNotBlank()) {
                Text(
                    text = "Notes: ${appointment.notes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderSubtle)
            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (appointment.status == "PENDING") {
                    Button(
                        onClick = onApprove,
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .testTag("admin_approve_${appointment.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StatusApproved,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Approve", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .testTag("admin_reject_${appointment.id}"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusCancelled),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(listOf(StatusCancelled, StatusCancelled))
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Reject", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else if (appointment.status == "APPROVED") {
                    Button(
                        onClick = onComplete,
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .testTag("admin_complete_${appointment.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = DarkBackground
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Mark Completed", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Delete Action
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("admin_delete_${appointment.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminServicesTab(
    services: List<BarbershopService>,
    onToggle: (BarbershopService) -> Unit,
    onDelete: (BarbershopService) -> Unit,
    onAddNew: () -> Unit
) {
    Column {
        Button(
            onClick = onAddNew,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("admin_add_service_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = DarkBackground
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add New Grooming Service", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(services, key = { it.id }) { service ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(BorderSubtle, BorderSubtle))),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = service.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (service.isActive) TextPrimary else TextMuted
                            )
                            Text(
                                text = "Rs ${"%,.0f".format(service.price)} • ${service.durationMinutes} mins • ${service.category}",
                                style = MaterialTheme.typography.bodySmall,
                                color = GoldLight,
                                fontSize = 11.sp
                            )
                        }

                        Switch(
                            checked = service.isActive,
                            onCheckedChange = { onToggle(service) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = GoldPrimary,
                                checkedTrackColor = DarkSurfaceHigh
                            ),
                            modifier = Modifier.testTag("toggle_service_${service.id}")
                        )

                        IconButton(
                            onClick = { onDelete(service) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminBarbersTab(
    barbers: List<Barber>,
    onToggle: (Barber) -> Unit,
    onDelete: (Barber) -> Unit,
    onAddNew: () -> Unit
) {
    Column {
        Button(
            onClick = onAddNew,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("admin_add_barber_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = DarkBackground
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add New Barber / Stylist", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(barbers, key = { it.id }) { barber ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(BorderSubtle, BorderSubtle))),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = barber.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (barber.isActive) TextPrimary else TextMuted
                            )
                            Text(
                                text = "${barber.title} • ${barber.experienceYears} yrs exp",
                                style = MaterialTheme.typography.bodySmall,
                                color = GoldPrimary,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "Hours: ${barber.workingHours}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }

                        Switch(
                            checked = barber.isActive,
                            onCheckedChange = { onToggle(barber) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = GoldPrimary,
                                checkedTrackColor = DarkSurfaceHigh
                            ),
                            modifier = Modifier.testTag("toggle_barber_${barber.id}")
                        )

                        IconButton(
                            onClick = { onDelete(barber) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminCustomersTab(customers: List<Customer>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(customers, key = { it.phone }) { cust ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(BorderSubtle, BorderSubtle))),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = cust.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${cust.totalBookings} Visits",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldLight,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Phone: ${cust.phone} | Email: ${cust.email.ifBlank { "N/A" }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Last Visit: ${cust.lastVisit.ifBlank { "Recent" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "Total Investment: Rs ${"%,.0f".format(cust.totalSpent)}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddServiceDialog(
    onDismiss: () -> Unit,
    onConfirm: (BarbershopService) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Haircuts") }
    var priceStr by remember { mutableStateOf("45") }
    var durationStr by remember { mutableStateOf("45") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = { Text("Add Grooming Service", color = GoldPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Service Name") },
                    colors = textFieldColors()
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (e.g. Haircuts, Beard & Shave, Combos)") },
                    colors = textFieldColors()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text("Price (Rs)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = textFieldColors()
                    )
                    OutlinedTextField(
                        value = durationStr,
                        onValueChange = { durationStr = it },
                        label = { Text("Duration (mins)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = textFieldColors()
                    )
                }
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    colors = textFieldColors(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(
                            BarbershopService(
                                id = "s_${System.currentTimeMillis()}",
                                name = name.trim(),
                                category = category.trim(),
                                price = priceStr.toDoubleOrNull() ?: 40.0,
                                durationMinutes = durationStr.toIntOrNull() ?: 30,
                                description = description.trim(),
                                isActive = true
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DarkBackground)
            ) {
                Text("Save Service")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        }
    )
}

@Composable
private fun AddBarberDialog(
    onDismiss: () -> Unit,
    onConfirm: (Barber) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("Senior Barber") }
    var specialties by remember { mutableStateOf("Skin Fades, Scissor Cuts") }
    var hours by remember { mutableStateOf("9:00 AM - 7:00 PM") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = { Text("Add Barber Profile", color = GoldPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    colors = textFieldColors()
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title (e.g. Master Barber)") },
                    colors = textFieldColors()
                )
                OutlinedTextField(
                    value = specialties,
                    onValueChange = { specialties = it },
                    label = { Text("Specialties") },
                    colors = textFieldColors()
                )
                OutlinedTextField(
                    value = hours,
                    onValueChange = { hours = it },
                    label = { Text("Working Hours") },
                    colors = textFieldColors()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(
                            Barber(
                                id = "b_${System.currentTimeMillis()}",
                                name = name.trim(),
                                title = title.trim(),
                                rating = 5.0f,
                                reviewsCount = 1,
                                specialties = specialties.trim(),
                                bio = "Master grooming specialist at Crown & Blade.",
                                drawableName = "barber_elias",
                                experienceYears = 5,
                                isActive = true,
                                workingHours = hours.trim()
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DarkBackground)
            ) {
                Text("Save Barber")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        }
    )
}
