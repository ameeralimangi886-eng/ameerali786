package com.example.util

import java.security.MessageDigest
import java.security.SecureRandom

enum class PasswordStrength(val label: String, val score: Int) {
    VERY_WEAK("Very Weak", 1),
    WEAK("Weak", 2),
    FAIR("Fair", 3),
    STRONG("Strong", 4),
    VERY_STRONG("Very Strong", 5)
}

object SecurityUtils {

    private val secureRandom = SecureRandom()

    /**
     * Generates a 16-byte cryptographically secure random salt in hexadecimal.
     */
    fun generateSalt(): String {
        val saltBytes = ByteArray(16)
        secureRandom.nextBytes(saltBytes)
        return saltBytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Generates a secure random 6-digit verification code or token.
     */
    fun generateVerificationCode(): String {
        val number = secureRandom.nextInt(900000) + 100000
        return number.toString()
    }

    /**
     * Generates a secure random hex token for password reset links.
     */
    fun generateResetToken(): String {
        val tokenBytes = ByteArray(24)
        secureRandom.nextBytes(tokenBytes)
        return tokenBytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Securely hashes a plain-text password with the user's unique salt using SHA-256 with 5,000 iterations.
     * The password is NEVER stored in plain text.
     */
    fun hashPassword(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        var hash = digest.digest((password + salt).toByteArray(Charsets.UTF_8))
        // Key stretching iterations
        for (i in 0 until 5000) {
            digest.reset()
            digest.update(salt.toByteArray(Charsets.UTF_8))
            hash = digest.digest(hash)
        }
        return hash.joinToString("") { "%02x".format(it) }
    }

    /**
     * Verifies a candidate password against the stored salt and hash using constant-time comparison.
     */
    fun verifyPassword(candidatePassword: String, salt: String, storedHash: String): Boolean {
        val candidateHash = hashPassword(candidatePassword, salt)
        return MessageDigest.isEqual(
            candidateHash.toByteArray(Charsets.UTF_8),
            storedHash.toByteArray(Charsets.UTF_8)
        )
    }

    /**
     * Evaluates password strength to provide visual feedback to the user,
     * while respecting their choice to pick any password they want.
     */
    fun checkStrength(password: String): PasswordStrength {
        if (password.isEmpty()) return PasswordStrength.VERY_WEAK
        var score = 0
        if (password.length >= 6) score++
        if (password.length >= 10) score++
        if (password.any { it.isUpperCase() } && password.any { it.isLowerCase() }) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++

        return when {
            score <= 1 -> PasswordStrength.VERY_WEAK
            score == 2 -> PasswordStrength.WEAK
            score == 3 -> PasswordStrength.FAIR
            score == 4 -> PasswordStrength.STRONG
            else -> PasswordStrength.VERY_STRONG
        }
    }
}
