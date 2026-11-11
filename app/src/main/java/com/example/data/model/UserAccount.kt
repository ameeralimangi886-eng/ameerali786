package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_accounts")
data class UserAccount(
    @PrimaryKey
    val email: String, // Normalized lowercase email
    val name: String,
    val passwordHash: String, // Cryptographically salted SHA-256 hash. Plaintext password is NEVER stored.
    val salt: String, // Unique cryptographic salt generated per user
    val isEmailVerified: Boolean = false,
    val verificationCode: String = "",
    val resetToken: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
)
