package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barbers")
data class Barber(
    @PrimaryKey val id: String,
    val name: String,
    val title: String,
    val rating: Float,
    val reviewsCount: Int,
    val specialties: String,
    val bio: String,
    val drawableName: String,
    val experienceYears: Int,
    val isActive: Boolean = true,
    val workingHours: String = "9:00 AM - 7:00 PM"
)

@Entity(tableName = "services")
data class BarbershopService(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // "Haircuts", "Beard & Shave", "Combos & Spa", "Specialty"
    val description: String,
    val durationMinutes: Int,
    val price: Double,
    val isPopular: Boolean = false,
    val isActive: Boolean = true
)

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey val id: String,
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String = "",
    val barberId: String,
    val barberName: String,
    val serviceId: String,
    val serviceName: String,
    val servicePrice: Double,
    val date: String, // "YYYY-MM-DD" e.g. "2026-11-12"
    val timeSlot: String, // "10:00 AM"
    val status: String = "PENDING", // PENDING, APPROVED, COMPLETED, REJECTED, CANCELLED
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey val id: String,
    val customerName: String,
    val barberName: String,
    val rating: Float,
    val comment: String,
    val date: String,
    val initials: String
)

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey val phone: String,
    val name: String,
    val email: String = "",
    val totalBookings: Int = 1,
    val totalSpent: Double = 0.0,
    val lastVisit: String = ""
)
