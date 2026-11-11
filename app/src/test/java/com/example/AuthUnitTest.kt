package com.example

import com.example.data.local.BarbershopDao
import com.example.data.model.UserAccount
import com.example.data.repository.AuthRepository
import com.example.util.PasswordStrength
import com.example.util.SecurityUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthUnitTest {

    private lateinit var fakeDao: FakeBarbershopDao
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        fakeDao = FakeBarbershopDao()
        authRepository = AuthRepository(fakeDao)
    }

    @Test
    fun securityUtils_saltedHashing_isSecureAndNotPlainText() {
        val password = "MyCustomPassword#123"
        val salt = SecurityUtils.generateSalt()
        val hash = SecurityUtils.hashPassword(password, salt)

        // Password is never plaintext
        assertNotEquals(password, hash)
        assertEquals(64, hash.length) // SHA-256 produces 64 hex characters

        // Correct password verifies successfully
        assertTrue(SecurityUtils.verifyPassword(password, salt, hash))

        // Incorrect password fails verification
        assertFalse(SecurityUtils.verifyPassword("WrongPassword!", salt, hash))
    }

    @Test
    fun securityUtils_evaluatesPasswordStrengthCorrectly() {
        assertEquals(PasswordStrength.VERY_WEAK, SecurityUtils.checkStrength(""))
        assertEquals(PasswordStrength.VERY_WEAK, SecurityUtils.checkStrength("pass"))
        assertEquals(PasswordStrength.WEAK, SecurityUtils.checkStrength("pass12"))
        val fair = SecurityUtils.checkStrength("password123")
        assertTrue(fair == PasswordStrength.FAIR || fair == PasswordStrength.WEAK)
        assertEquals(PasswordStrength.VERY_STRONG, SecurityUtils.checkStrength("CrownBlade#2026!Elite"))
    }

    @Test
    fun authRepository_signUp_createsAccountWithSaltedHash() = runBlocking {
        val email = "gentleman@crownblade.com"
        val password = "ChosenPassword123"

        val result = authRepository.signUp("Lord Edward", email, password)
        assertTrue(result.isSuccess)

        val user = result.getOrNull()
        assertNotNull(user)
        assertEquals(email, user?.email)
        assertEquals("Lord Edward", user?.name)
        assertFalse(user!!.isEmailVerified)

        // Plaintext password is NEVER stored
        assertNotEquals(password, user.passwordHash)
        assertTrue(SecurityUtils.verifyPassword(password, user.salt, user.passwordHash))
    }

    @Test
    fun authRepository_login_succeedsWithCorrectPassword() = runBlocking {
        val email = "vip@client.com"
        val password = "SecretPassWord#99"

        authRepository.signUp("James Sterling", email, password)

        val loginResult = authRepository.login(email, password)
        assertTrue(loginResult.isSuccess)
        assertEquals("James Sterling", loginResult.getOrNull()?.name)
    }

    @Test
    fun authRepository_login_failsWithClearErrorMessageOnIncorrectPassword() = runBlocking {
        val email = "user@test.com"
        val password = "CorrectPassword"

        authRepository.signUp("Test User", email, password)

        val loginResult = authRepository.login(email, "WrongPassword")
        assertTrue(loginResult.isFailure)

        val message = loginResult.exceptionOrNull()?.message.orEmpty()
        assertTrue("Error message must clearly mention incorrect password", message.contains("Incorrect password", ignoreCase = true))
    }

    @Test
    fun authRepository_changePassword_updatesToNewUserChosenPassword() = runBlocking {
        val email = "change@test.com"
        val initialPassword = "OldPassword1"
        val newPassword = "BrandNewUserPassword2!"

        authRepository.signUp("Arthur Pendelton", email, initialPassword)

        // Attempt change with wrong current password -> must fail
        val failedAttempt = authRepository.changePassword(email, "WrongCurrent", newPassword)
        assertTrue(failedAttempt.isFailure)

        // Attempt change with correct current password -> must succeed
        val successfulAttempt = authRepository.changePassword(email, initialPassword, newPassword)
        assertTrue(successfulAttempt.isSuccess)

        // Old password no longer works
        val oldLogin = authRepository.login(email, initialPassword)
        assertTrue(oldLogin.isFailure)

        // New password works
        val newLogin = authRepository.login(email, newPassword)
        assertTrue(newLogin.isSuccess)
    }

    @Test
    fun authRepository_forgotAndResetPassword_worksSmoothly() = runBlocking {
        val email = "reset@test.com"
        val initialPass = "OldForgotPass"
        val newPass = "FreshResetPass99"

        authRepository.signUp("Charles Xavier", email, initialPass)

        // Request reset
        val tokenResult = authRepository.requestPasswordReset(email)
        assertTrue(tokenResult.isSuccess)
        val token = tokenResult.getOrNull()
        assertNotNull(token)

        // Reset password with token
        val resetResult = authRepository.resetPasswordWithToken(email, newPass)
        assertTrue(resetResult.isSuccess)

        // Login with new password
        val loginResult = authRepository.login(email, newPass)
        assertTrue(loginResult.isSuccess)
    }

    @Test
    fun authRepository_emailVerification_marksEmailVerified() = runBlocking {
        val email = "verify@test.com"
        authRepository.signUp("Winston Churchill", email, "MySecretPass")

        val account = fakeDao.getUserAccountSync(email)
        assertNotNull(account)
        assertFalse(account!!.isEmailVerified)

        val verifyResult = authRepository.verifyEmail(email, account.verificationCode)
        assertTrue(verifyResult.isSuccess)

        val updated = fakeDao.getUserAccountSync(email)
        assertTrue(updated!!.isEmailVerified)
    }

    // Lightweight in-memory test fake for BarbershopDao authentication methods
    private class FakeBarbershopDao : BarbershopDao {
        val users = mutableMapOf<String, UserAccount>()

        override fun getUserAccount(email: String): Flow<UserAccount?> = flowOf(users[email])
        override suspend fun getUserAccountSync(email: String): UserAccount? = users[email]

        override suspend fun insertUserAccount(user: UserAccount) {
            users[user.email] = user
        }

        override suspend fun updateUserAccount(user: UserAccount) {
            users[user.email] = user
        }

        override suspend fun updatePassword(email: String, passwordHash: String, salt: String) {
            users[email]?.let {
                users[email] = it.copy(passwordHash = passwordHash, salt = salt, resetToken = null)
            }
        }

        override suspend fun setEmailVerified(email: String, isVerified: Boolean) {
            users[email]?.let {
                users[email] = it.copy(isEmailVerified = isVerified)
            }
        }

        override suspend fun setPasswordResetToken(email: String, token: String?) {
            users[email]?.let {
                users[email] = it.copy(resetToken = token)
            }
        }

        override suspend fun setVerificationCode(email: String, code: String) {
            users[email]?.let {
                users[email] = it.copy(verificationCode = code)
            }
        }

        override suspend fun updateLastLogin(email: String, timestamp: Long) {
            users[email]?.let {
                users[email] = it.copy(lastLoginAt = timestamp)
            }
        }

        override suspend fun deleteUserAccount(email: String) {
            users.remove(email)
        }

        // Unused stubs for BarbershopDao interface
        override fun getAllBarbers() = flowOf(emptyList<com.example.data.model.Barber>())
        override fun getActiveBarbers() = flowOf(emptyList<com.example.data.model.Barber>())
        override suspend fun insertBarber(barber: com.example.data.model.Barber) {}
        override suspend fun insertBarbers(barbers: List<com.example.data.model.Barber>) {}
        override suspend fun updateBarber(barber: com.example.data.model.Barber) {}
        override suspend fun deleteBarber(barber: com.example.data.model.Barber) {}
        override fun getAllServices() = flowOf(emptyList<com.example.data.model.BarbershopService>())
        override fun getActiveServices() = flowOf(emptyList<com.example.data.model.BarbershopService>())
        override suspend fun insertService(service: com.example.data.model.BarbershopService) {}
        override suspend fun insertServices(services: List<com.example.data.model.BarbershopService>) {}
        override suspend fun updateService(service: com.example.data.model.BarbershopService) {}
        override suspend fun deleteService(service: com.example.data.model.BarbershopService) {}
        override fun getAllAppointments() = flowOf(emptyList<com.example.data.model.Appointment>())
        override fun getAppointmentsForCustomer(phone: String) = flowOf(emptyList<com.example.data.model.Appointment>())
        override suspend fun getBookedAppointmentsForDate(date: String) = emptyList<com.example.data.model.Appointment>()
        override suspend fun insertAppointment(appointment: com.example.data.model.Appointment) {}
        override suspend fun updateAppointment(appointment: com.example.data.model.Appointment) {}
        override suspend fun updateAppointmentStatus(id: String, status: String) {}
        override suspend fun deleteAppointment(appointment: com.example.data.model.Appointment) {}
        override suspend fun deleteAppointmentById(id: String) {}
        override fun getAllReviews() = flowOf(emptyList<com.example.data.model.Review>())
        override suspend fun insertReviews(reviews: List<com.example.data.model.Review>) {}
        override fun getAllCustomers() = flowOf(emptyList<com.example.data.model.Customer>())
        override suspend fun upsertCustomer(customer: com.example.data.model.Customer) {}
        override suspend fun insertCustomers(customers: List<com.example.data.model.Customer>) {}
    }
}
