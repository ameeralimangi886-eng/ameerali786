package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Barber
import com.example.data.model.BarbershopService
import com.example.data.model.Review
import com.example.ui.components.openWhatsApp
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
import com.example.ui.theme.WhatsAppGreen
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BarbershopViewModel

@Composable
fun HomeScreen(
    viewModel: BarbershopViewModel,
    modifier: Modifier = Modifier
) {
    val services by viewModel.services.collectAsState()
    val barbers by viewModel.barbers.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val context = LocalContext.current

    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Combos & Spa", "Haircuts", "Beard & Shave", "Specialty")

    val filteredServices = if (selectedCategory == "All") {
        services
    } else {
        services.filter { it.category == selectedCategory }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(bottom = 90.dp)
    ) {
        // 1. HERO SECTION
        HeroSection(
            onBookClick = { viewModel.navigateTo(AppScreen.BOOKING) },
            onAiConsultClick = { viewModel.navigateTo(AppScreen.AI_STYLIST) }
        )

        // 2. HIGHLIGHTS STRIP
        HighlightsStrip()

        // 3. ABOUT US SECTION
        AboutUsSection()

        // 4. SERVICES & PRICING SECTION
        ServicesSection(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelect = { selectedCategory = it },
            services = filteredServices,
            onBookService = { service ->
                viewModel.startBookingWithService(service)
            }
        )

        // 5. MASTER BARBERS SECTION
        BarbersSection(
            barbers = barbers,
            onBookBarber = { barber ->
                viewModel.startBookingWithBarber(barber)
            }
        )

        // 6. CLIENT TESTIMONIALS SECTION
        ReviewsSection(reviews = reviews)

        // 7. LOCATION & CONTACT / WHATSAPP SECTION
        ContactSection(context = context)
    }
}

@Composable
private fun HeroSection(
    onBookClick: () -> Unit,
    onAiConsultClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
    ) {
        // Background Hero Image
        Image(
            painter = painterResource(id = R.drawable.hero_barbershop),
            contentDescription = "Crown & Blade Luxury Parlour Interior",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay for luxury ambiance
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            DarkBackground.copy(alpha = 0.4f),
                            DarkBackground.copy(alpha = 0.75f),
                            DarkBackground
                        )
                    )
                )
        )

        // Hero Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Gold Tagline Chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(BorderGoldMuted.copy(alpha = 0.6f))
                    .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(30.dp))
                    .padding(horizontal = 14.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "PRECISION • TRADITION • LUXURY",
                    color = GoldLight,
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "The Art of Timeless\nMen's Grooming",
                style = MaterialTheme.typography.displayMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Experience master shear work, straight-razor hot steam lather, and bespoke styling in our London-inspired gentlemen's sanctuary.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onBookClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("hero_book_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = DarkBackground
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCut,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Book Your Chair",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                OutlinedButton(
                    onClick = onAiConsultClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("hero_ai_consult_button"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = GoldLight
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(listOf(GoldPrimary, GoldLight))
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "AI Stylist",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun HighlightsStrip() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurfaceVariant)
            .border(
                width = 1.dp,
                color = BorderGoldMuted.copy(alpha = 0.3f)
            )
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HighlightItem(icon = Icons.Default.WorkspacePremium, title = "10+ Yrs", subtitle = "Mastery")
        Box(modifier = Modifier.size(width = 1.dp, height = 24.dp).background(BorderSubtle))
        HighlightItem(icon = Icons.Default.Star, title = "4.95 ★", subtitle = "500+ Reviews")
        Box(modifier = Modifier.size(width = 1.dp, height = 24.dp).background(BorderSubtle))
        HighlightItem(icon = Icons.Default.Shield, title = "100%", subtitle = "Artisans")
    }
}

@Composable
private fun HighlightItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GoldPrimary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                fontSize = 14.sp
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun AboutUsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(BorderGoldMuted, BorderSubtle))),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "ABOUT THE SANCTUARY",
                style = MaterialTheme.typography.labelSmall,
                color = GoldPrimary,
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Where Classic Heritage Meets Modern Precision",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Founded in 2014, Crown & Blade is crafted for the discerning gentleman who views grooming not as a chore, but as an essential ritual of distinction. Every appointment includes our signature eucalyptus hot-towel steam, straight-edge razor nape cleanup, and complimentary single malt Scotch or double-pulled espresso.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AboutFeatureItem(label = "Eucalyptus Hot Towels")
                AboutFeatureItem(label = "Artisan Straight Shaves")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AboutFeatureItem(label = "Bespoke Whiskey Bar")
                AboutFeatureItem(label = "Organic Beard Elixirs")
            }
        }
    }
}

@Composable
private fun AboutFeatureItem(label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(GoldPrimary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(10.dp)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ServicesSection(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    services: List<BarbershopService>,
    onBookService: (BarbershopService) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SERVICES & PRICING",
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldPrimary,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Crafted Grooming Menu",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Categories Scroll
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) GoldPrimary else DarkSurfaceVariant)
                        .border(
                            1.dp,
                            if (isSelected) GoldPrimary else BorderSubtle,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { onCategorySelect(category) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("category_tab_$category")
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) DarkBackground else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Service Cards List
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            services.forEach { service ->
                ServiceCard(service = service, onBook = { onBookService(service) })
            }
        }
    }
}

@Composable
private fun ServiceCard(
    service: BarbershopService,
    onBook: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("service_card_${service.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (service.isPopular) listOf(GoldPrimary.copy(alpha = 0.8f), BorderGoldMuted)
                else listOf(BorderSubtle, BorderSubtle)
            )
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = service.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        if (service.isPopular) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GoldPrimary.copy(alpha = 0.2f))
                                    .border(0.5.dp, GoldPrimary, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "POPULAR",
                                    color = GoldPrimary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${service.durationMinutes} mins • ${service.category}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }

                // Price Tag
                Text(
                    text = "Rs ${"%,.0f".format(service.price)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = service.description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onBook,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .testTag("book_service_btn_${service.id}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkSurfaceHigh,
                    contentColor = GoldLight
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(BorderGoldMuted, GoldPrimary))
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Select & Book Appointment",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun BarbersSection(
    barbers: List<Barber>,
    onBookBarber: (Barber) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        Text(
            text = "THE ARTISANS",
            style = MaterialTheme.typography.labelSmall,
            color = GoldPrimary,
            letterSpacing = 1.5.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Master Barber Profiles",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            barbers.forEach { barber ->
                BarberProfileCard(barber = barber, onBook = { onBookBarber(barber) })
            }
        }
    }
}

@Composable
private fun BarberProfileCard(
    barber: Barber,
    onBook: () -> Unit
) {
    val imageRes = when (barber.drawableName) {
        "barber_sophia" -> R.drawable.barber_sophia
        else -> R.drawable.barber_elias
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("barber_card_${barber.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(BorderGoldMuted, BorderSubtle))),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barber Portrait
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = barber.name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.5.dp, GoldPrimary, RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = barber.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "%.2f".format(barber.rating),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )
                    }
                }

                Text(
                    text = barber.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${barber.experienceYears} Years Craft • ${barber.workingHours}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = barber.specialties,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onBook,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .testTag("book_with_${barber.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = DarkBackground
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Book with ${barber.name.split(" ").first()}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewsSection(reviews: List<Review>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            text = "GENTLEMEN'S VERDICT",
            style = MaterialTheme.typography.labelSmall,
            color = GoldPrimary,
            letterSpacing = 1.5.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Client Testimonials",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            reviews.forEach { review ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(BorderSubtle, BorderSubtle))),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(BorderGoldMuted),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = review.initials,
                                        color = GoldLight,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = review.customerName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Client of ${review.barberName} • ${review.date}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            Row {
                                repeat(5) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = GoldAccent,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "\"${review.comment}\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactSection(context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(BorderGoldMuted, BorderSubtle))),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "PARLOUR DETAILS",
                style = MaterialTheme.typography.labelSmall,
                color = GoldPrimary,
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Location & Direct Booking",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Crown & Blade Sanctuary • Dir. Ali Murtaza",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "0341-3274132 • ameeralimangi886@gmail.com",
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Monday - Saturday: 9:00 AM - 8:00 PM",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = "Sunday: 10:00 AM - 5:00 PM",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Call phone
                OutlinedButton(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+923413274132"))
                        context.startActivity(dialIntent)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("call_parlour_button"),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(listOf(BorderGoldMuted, GoldPrimary))
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Call Parlour",
                        color = GoldLight,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // WhatsApp
                Button(
                    onClick = { openWhatsApp(context) },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("whatsapp_bottom_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WhatsAppGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "WhatsApp Chat",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
