package com.example.data.local

import com.example.data.model.Appointment
import com.example.data.model.Barber
import com.example.data.model.BarbershopService
import com.example.data.model.Customer
import com.example.data.model.Review

object SampleData {
    val barbers = listOf(
        Barber(
            id = "b1",
            name = "Elias Thorne",
            title = "Master Barber & Founder",
            rating = 4.95f,
            reviewsCount = 184,
            specialties = "Signature Hot Towel Shave, Classic Scissor Work, Textured Quiff",
            bio = "With over 14 years behind the chair in London and New York, Elias blends Old World barbering traditions with bespoke modern styling.",
            drawableName = "barber_elias",
            experienceYears = 14,
            isActive = true,
            workingHours = "9:00 AM - 7:00 PM"
        ),
        Barber(
            id = "b2",
            name = "Sophia Cruz",
            title = "Senior Stylist & Texture Specialist",
            rating = 4.98f,
            reviewsCount = 142,
            specialties = "Precision Skin Taper, Razor Line-up, Modern Mullet & Burst Fade",
            bio = "Award-winning stylist renowned for geometric fades and textured scissor cuts. Sophia elevates grooming to contemporary artistry.",
            drawableName = "barber_sophia",
            experienceYears = 9,
            isActive = true,
            workingHours = "10:00 AM - 8:00 PM"
        ),
        Barber(
            id = "b3",
            name = "Julian Hayes",
            title = "Artisan Barber & Beard Crafter",
            rating = 4.91f,
            reviewsCount = 119,
            specialties = "Full Beard Sculpting, Straight Razor Fades, Scalp Therapy",
            bio = "Master craftsman dedicated to the ritual of men's grooming. Specializes in beard design tailored to individual facial anatomy.",
            drawableName = "barber_elias",
            experienceYears = 11,
            isActive = true,
            workingHours = "9:30 AM - 6:30 PM"
        ),
        Barber(
            id = "b4",
            name = "Liam Vance",
            title = "Precision Barber",
            rating = 4.88f,
            reviewsCount = 76,
            specialties = "Taper Fade, Buzz Cut Styling, Hair Tattoo Designs",
            bio = "Detail-obsessed barber known for sharp lines and lightning-quick precision. Liam keeps clients looking runway-ready.",
            drawableName = "barber_sophia",
            experienceYears = 6,
            isActive = true,
            workingHours = "11:00 AM - 8:00 PM"
        )
    )

    val services = listOf(
        BarbershopService(
            id = "s1",
            name = "The Royal Experience",
            category = "Combos & Spa",
            description = "Signature haircut, steam towel facial, straight-razor neck shave, beard sculpt, scalp massage, and single malt or espresso.",
            durationMinutes = 60,
            price = 4500.0,
            isPopular = true,
            isActive = true
        ),
        BarbershopService(
            id = "s2",
            name = "Executive Haircut",
            category = "Haircuts",
            description = "Bespoke consultation, precision shear & clipper cut, energizing hair wash, hot lather neck shave, and premium styling finish.",
            durationMinutes = 40,
            price = 2500.0,
            isPopular = true,
            isActive = true
        ),
        BarbershopService(
            id = "s3",
            name = "Beard Sculpt & Hot Towel Shave",
            category = "Beard & Shave",
            description = "Beard shape contouring, botanical pre-shave oil, double hot steamed towels, straight razor precision lineup, and nourishing balm.",
            durationMinutes = 35,
            price = 1800.0,
            isPopular = true,
            isActive = true
        ),
        BarbershopService(
            id = "s4",
            name = "Signature Skin Fade & Line-Up",
            category = "Haircuts",
            description = "Seamless zero/foil skin fade, clipper fading, crisp razor edge lineup, hair wash, and matte styling clay.",
            durationMinutes = 45,
            price = 2200.0,
            isPopular = false,
            isActive = true
        ),
        BarbershopService(
            id = "s5",
            name = "Gentleman's Charcoal Facial",
            category = "Combos & Spa",
            description = "Deep pore purifying activated charcoal mask, eucalyptus steam towel treatment, and anti-fatigue peptide moisturizer.",
            durationMinutes = 30,
            price = 2000.0,
            isPopular = false,
            isActive = true
        ),
        BarbershopService(
            id = "s6",
            name = "Father & Son Duo Cut",
            category = "Specialty",
            description = "Side-by-side executive cuts for two gentlemen. Includes complimentary beverages and custom styling for both.",
            durationMinutes = 75,
            price = 4000.0,
            isPopular = false,
            isActive = true
        ),
        BarbershopService(
            id = "s7",
            name = "Grey Blending & Camo",
            category = "Specialty",
            description = "Subtle, natural demi-permanent hair or beard grey reduction in just 10 minutes. Leaves a natural, youthful look.",
            durationMinutes = 25,
            price = 1800.0,
            isPopular = false,
            isActive = true
        )
    )

    val reviews = listOf(
        Review(
            id = "r1",
            customerName = "Alexander Wright",
            barberName = "Elias Thorne",
            rating = 5.0f,
            comment = "Without question the finest barbershop experience in the city. Elias's attention to detail on the hot towel shave is unmatched.",
            date = "2 days ago",
            initials = "AW"
        ),
        Review(
            id = "r2",
            customerName = "Ali Murtaza",
            barberName = "Sophia Cruz",
            rating = 5.0f,
            comment = "Exceptional service by Crown & Blade. Sophia's precision fade and beard styling is pure perfection. Highly recommended!",
            date = "Yesterday",
            initials = "AM"
        ),
        Review(
            id = "r3",
            customerName = "Marcus Sterling",
            barberName = "Sophia Cruz",
            rating = 5.0f,
            comment = "Sophia gave me the cleanest taper fade I've ever had. The shop atmosphere with dark wood and complimentary espresso is pure class.",
            date = "1 week ago",
            initials = "MS"
        ),
        Review(
            id = "r4",
            customerName = "David Kim",
            barberName = "Julian Hayes",
            rating = 5.0f,
            comment = "Julian sculpted my unruly beard into a masterpiece. He taught me the right oils and comb techniques. Definitely my permanent barber now.",
            date = "2 weeks ago",
            initials = "DK"
        )
    )

    val initialAppointments = listOf(
        Appointment(
            id = "CB-7860",
            customerName = "Ali Murtaza",
            customerPhone = "03413274132",
            customerEmail = "ameeralimangi886@gmail.com",
            barberId = "b1",
            barberName = "Elias Thorne",
            serviceId = "s1",
            serviceName = "The Royal Experience",
            servicePrice = 4500.0,
            date = "2026-11-12",
            timeSlot = "11:15 AM",
            status = "APPROVED",
            notes = "VIP Client appointment. Executive treatment."
        ),
        Appointment(
            id = "CB-1001",
            customerName = "David Kim",
            customerPhone = "+1 (555) 234-8901",
            customerEmail = "david.kim@example.com",
            barberId = "b1",
            barberName = "Elias Thorne",
            serviceId = "s1",
            serviceName = "The Royal Experience",
            servicePrice = 4500.0,
            date = "2026-11-12",
            timeSlot = "10:00 AM",
            status = "APPROVED",
            notes = "First visit, prefers lighter cologne finish."
        ),
        Appointment(
            id = "CB-1002",
            customerName = "Marcus Sterling",
            customerPhone = "+1 (555) 456-7890",
            customerEmail = "marcus.s@example.com",
            barberId = "b2",
            barberName = "Sophia Cruz",
            serviceId = "s4",
            serviceName = "Signature Skin Fade & Line-Up",
            servicePrice = 2200.0,
            date = "2026-11-12",
            timeSlot = "02:15 PM",
            status = "PENDING",
            notes = "Wants high skin fade on sides."
        ),
        Appointment(
            id = "CB-1003",
            customerName = "Alexander Wright",
            customerPhone = "+1 (555) 987-6543",
            customerEmail = "alex.w@example.com",
            barberId = "b3",
            barberName = "Julian Hayes",
            serviceId = "s3",
            serviceName = "Beard Sculpt & Hot Towel Shave",
            servicePrice = 1800.0,
            date = "2026-11-11",
            timeSlot = "12:00 PM",
            status = "COMPLETED",
            notes = "Regular client."
        )
    )

    val customers = listOf(
        Customer(
            phone = "03413274132",
            name = "Ali Murtaza",
            email = "ameeralimangi886@gmail.com",
            totalBookings = 6,
            totalSpent = 22500.0,
            lastVisit = "2026-11-11"
        ),
        Customer(
            phone = "+1 (555) 234-8901",
            name = "David Kim",
            email = "david.kim@example.com",
            totalBookings = 3,
            totalSpent = 11500.0,
            lastVisit = "2026-11-04"
        ),
        Customer(
            phone = "+1 (555) 456-7890",
            name = "Marcus Sterling",
            email = "marcus.s@example.com",
            totalBookings = 5,
            totalSpent = 11000.0,
            lastVisit = "2026-10-28"
        ),
        Customer(
            phone = "+1 (555) 987-6543",
            name = "Alexander Wright",
            email = "alex.w@example.com",
            totalBookings = 8,
            totalSpent = 32000.0,
            lastVisit = "2026-11-11"
        )
    )
}
