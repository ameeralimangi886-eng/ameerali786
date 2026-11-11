package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderGoldMuted
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceHigh
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AiConsultantViewModel
import com.example.viewmodel.AiConsultationUiState
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BarbershopViewModel
import com.example.viewmodel.HaircutConsultation

@Composable
fun AiConsultantScreen(
    aiViewModel: AiConsultantViewModel,
    barbershopViewModel: BarbershopViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by aiViewModel.uiState.collectAsState()
    val faceShape by aiViewModel.selectedFaceShape.collectAsState()
    val hairTexture by aiViewModel.selectedHairTexture.collectAsState()
    val hairDensity by aiViewModel.selectedHairDensity.collectAsState()
    val beardStyle by aiViewModel.selectedBeardStyle.collectAsState()
    val styleVibe by aiViewModel.selectedStyleVibe.collectAsState()
    val customNotes by aiViewModel.customNotes.collectAsState()

    val faceShapes = listOf("Square", "Oval", "Round", "Diamond", "Heart", "Oblong")
    val textures = listOf("Straight", "Wavy", "Curly", "Coily / Afro")
    val densities = listOf("Thin / Receding", "Medium", "Thick / Dense")
    val beardStyles = listOf("Clean Shaven", "3-Day Stubble", "Sharp Sculpted Beard", "Full Lumberjack", "Taper Fade Beard")
    val vibes = listOf("Classic Old Money", "Modern Streetwear", "Sharp Executive", "Athletic Low-Maintenance")

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .padding(bottom = 90.dp)
    ) {
        // High Thinking Model Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(GoldPrimary.copy(alpha = 0.15f))
                .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "GEMINI 4 CARBON • HIGH THINKING MODE",
                    color = GoldLight,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Crown AI Stylist",
            style = MaterialTheme.typography.displaySmall,
            color = TextPrimary
        )

        Text(
            text = "Deep aesthetic reasoning tailored to your facial geometry, hair follicles, and lifestyle.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        when (val state = uiState) {
            is AiConsultationUiState.Loading -> {
                ThinkingLoadingView()
            }
            is AiConsultationUiState.Success -> {
                ConsultationResultView(
                    consultation = state.consultation,
                    onBookRecommended = {
                        // Find matching service and barber in barbershopViewModel
                        val allServices = barbershopViewModel.services.value
                        val allBarbers = barbershopViewModel.barbers.value

                        val matchedService = allServices.find {
                            it.name.contains(state.consultation.suggestedService, ignoreCase = true)
                        } ?: allServices.firstOrNull()

                        val matchedBarber = allBarbers.find {
                            it.name.contains(state.consultation.recommendedBarber, ignoreCase = true)
                        } ?: allBarbers.firstOrNull()

                        if (matchedService != null) {
                            barbershopViewModel.selectService(matchedService)
                        }
                        if (matchedBarber != null) {
                            barbershopViewModel.selectBarber(matchedBarber)
                        }

                        barbershopViewModel.navigateTo(AppScreen.BOOKING)
                    },
                    onConsultAgain = {
                        aiViewModel.requestConsultation()
                    }
                )
            }
            else -> {
                // Input Form
                ConsultationInputForm(
                    faceShapes = faceShapes,
                    selectedFaceShape = faceShape,
                    onSelectFaceShape = { aiViewModel.selectedFaceShape.value = it },

                    textures = textures,
                    selectedTexture = hairTexture,
                    onSelectTexture = { aiViewModel.selectedHairTexture.value = it },

                    densities = densities,
                    selectedDensity = hairDensity,
                    onSelectDensity = { aiViewModel.selectedHairDensity.value = it },

                    beardStyles = beardStyles,
                    selectedBeardStyle = beardStyle,
                    onSelectBeardStyle = { aiViewModel.selectedBeardStyle.value = it },

                    vibes = vibes,
                    selectedVibe = styleVibe,
                    onSelectVibe = { aiViewModel.selectedStyleVibe.value = it },

                    customNotes = customNotes,
                    onNotesChange = { aiViewModel.customNotes.value = it },

                    onSubmit = { aiViewModel.requestConsultation() }
                )
            }
        }
    }
}

@Composable
private fun ConsultationInputForm(
    faceShapes: List<String>,
    selectedFaceShape: String,
    onSelectFaceShape: (String) -> Unit,

    textures: List<String>,
    selectedTexture: String,
    onSelectTexture: (String) -> Unit,

    densities: List<String>,
    selectedDensity: String,
    onSelectDensity: (String) -> Unit,

    beardStyles: List<String>,
    selectedBeardStyle: String,
    onSelectBeardStyle: (String) -> Unit,

    vibes: List<String>,
    selectedVibe: String,
    onSelectVibe: (String) -> Unit,

    customNotes: String,
    onNotesChange: (String) -> Unit,

    onSubmit: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Face Shape
        FormCategory(title = "1. Facial Geometry") {
            ChipRow(options = faceShapes, selected = selectedFaceShape, onSelect = onSelectFaceShape, tagPrefix = "face")
        }

        // Hair Texture
        FormCategory(title = "2. Hair Texture") {
            ChipRow(options = textures, selected = selectedTexture, onSelect = onSelectTexture, tagPrefix = "texture")
        }

        // Hair Density
        FormCategory(title = "3. Hair Density") {
            ChipRow(options = densities, selected = selectedDensity, onSelect = onSelectDensity, tagPrefix = "density")
        }

        // Beard Style
        FormCategory(title = "4. Beard Goal") {
            ChipRow(options = beardStyles, selected = selectedBeardStyle, onSelect = onSelectBeardStyle, tagPrefix = "beard")
        }

        // Style Vibe
        FormCategory(title = "5. Desired Vibe") {
            ChipRow(options = vibes, selected = selectedVibe, onSelect = onSelectVibe, tagPrefix = "vibe")
        }

        // Custom Notes
        FormCategory(title = "6. Specific Notes or Goals") {
            OutlinedTextField(
                value = customNotes,
                onValueChange = onNotesChange,
                placeholder = { Text("e.g. receding hairline, preparing for gala, prefer low pomade maintenance...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ai_custom_notes_input"),
                maxLines = 3,
                colors = textFieldColors()
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_ai_consultation_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = DarkBackground
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Run High-Thinking Aesthetic Analysis",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun FormCategory(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = GoldLight,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun ChipRow(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    tagPrefix: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val isSelected = selected == option
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) GoldPrimary else DarkSurfaceVariant)
                    .border(
                        1.dp,
                        if (isSelected) GoldPrimary else BorderSubtle,
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { onSelect(option) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("${tagPrefix}_chip_${option.replace(" ", "_").replace("/", "_")}"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) DarkBackground else TextPrimary
                )
            }
        }
    }
}

@Composable
private fun ThinkingLoadingView() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(GoldPrimary.copy(alpha = 0.15f))
                .border(2.dp, GoldPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = "Thinking",
                tint = GoldPrimary,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "High-Thinking Reasoning Active",
            style = MaterialTheme.typography.titleLarge,
            color = GoldPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Gemini 4 Carbon is evaluating facial contours, hairline taper heights, follicle density, and beard angularity...",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        CircularProgressIndicator(
            color = GoldPrimary,
            modifier = Modifier.size(28.dp),
            strokeWidth = 2.5.dp
        )
    }
}

@Composable
private fun ConsultationResultView(
    consultation: HaircutConsultation,
    onBookRecommended: () -> Unit,
    onConsultAgain: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Result Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("ai_consultation_result_card"),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(GoldPrimary, BorderGoldMuted))),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GoldPrimary.copy(alpha = 0.2f))
                            .border(0.5.dp, GoldPrimary, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "AESTHETIC VERDICT",
                            color = GoldLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = consultation.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = GoldLight,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = BorderSubtle)
                Spacer(modifier = Modifier.height(16.dp))

                // Haircut Recommendation
                ConsultationDetailBlock(
                    icon = Icons.Default.ContentCut,
                    title = "HAIRCUT & SILHOUETTE",
                    content = consultation.haircutRecommendation
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Fade / Taper specs
                ConsultationDetailBlock(
                    icon = Icons.Default.Science,
                    title = "TAPER & FADE SPECIFICATION",
                    content = consultation.taperFadeDetails
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Beard styling
                ConsultationDetailBlock(
                    icon = Icons.Default.Face,
                    title = "BEARD & JAWLINE PAIRING",
                    content = consultation.beardStyleRecommendation
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Products
                ConsultationDetailBlock(
                    icon = Icons.Default.Spa,
                    title = "PRODUCT PRESCRIPTION",
                    content = consultation.recommendedProduct
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = BorderSubtle)
                Spacer(modifier = Modifier.height(16.dp))

                // Recommended Service & Barber Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceHigh)
                        .border(1.dp, BorderGoldMuted, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "PARLOUR MATCH",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Service: ${consultation.suggestedService}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Master Barber: ${consultation.recommendedBarber}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GoldLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Maintenance Tips
                Text(
                    text = "DAILY GROOMING ROUTINE",
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldPrimary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                consultation.maintenanceTips.forEach { tip ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier
                                .size(14.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tip,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Action Buttons
        Button(
            onClick = onBookRecommended,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("book_ai_recommended_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = DarkBackground
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.ContentCut, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Book This Recommended Look",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

        OutlinedButton(
            onClick = onConsultAgain,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("ai_reconsult_button"),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = Brush.horizontalGradient(listOf(BorderGoldMuted, GoldPrimary))
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Re-analyze with Different Options",
                color = GoldLight,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun ConsultationDetailBlock(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = GoldLight,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            lineHeight = 18.sp
        )
    }
}
