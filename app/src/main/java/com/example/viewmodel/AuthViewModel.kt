package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BarbershopDatabase
import com.example.data.model.UserAccount
import com.example.data.repository.AuthRepository
import com.example.util.PasswordStrength
import com.example.util.SecurityUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AuthMode {
    LOGIN,
    SIGNUP,
    FORGOT_PASSWORD,
    RESET_PASSWORD,
    PROFILE,
    CHANGE_PASSWORD,
    VERIFY_EMAIL
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuthRepository

    init {
        val database = BarbershopDatabase.getDatabase(application, viewModelScope)
        repository = AuthRepository(database.barbershopDao())
    }

    // Active session state
    private val _currentUser = MutableStateFlow<UserAccount?>(null)
    val currentUser: StateFlow<UserAccount?> = _currentUser.asStateFlow()

    private val _authMode = MutableStateFlow(AuthMode.LOGIN)
    val authMode: StateFlow<AuthMode> = _authMode.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    // Login Form State
    val loginEmail = MutableStateFlow("ameeralimangi886@gmail.com")
    val loginPassword = MutableStateFlow("")
    val isLoginPasswordVisible = MutableStateFlow(false)

    // Sign Up Form State
    val signupName = MutableStateFlow("")
    val signupEmail = MutableStateFlow("")
    val signupPassword = MutableStateFlow("")
    val signupConfirmPassword = MutableStateFlow("")
    val isSignupPasswordVisible = MutableStateFlow(false)
    val isSignupConfirmPasswordVisible = MutableStateFlow(false)

    // Change Password Form State
    val currentPasswordInput = MutableStateFlow("")
    val newPasswordInput = MutableStateFlow("")
    val confirmNewPasswordInput = MutableStateFlow("")
    val isCurrentPasswordVisible = MutableStateFlow(false)
    val isNewPasswordVisible = MutableStateFlow(false)
    val isConfirmNewPasswordVisible = MutableStateFlow(false)

    // Forgot & Reset Password Form State
    val resetEmailInput = MutableStateFlow("")
    val resetToken = MutableStateFlow<String?>(null)
    val resetNewPasswordInput = MutableStateFlow("")
    val resetConfirmPasswordInput = MutableStateFlow("")
    val isResetPasswordVisible = MutableStateFlow(false)
    val isResetConfirmPasswordVisible = MutableStateFlow(false)

    // Email Verification Code Input
    val verificationCodeInput = MutableStateFlow("")

    fun setAuthMode(mode: AuthMode) {
        _authMode.value = mode
        clearFeedback()
    }

    fun clearFeedback() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    fun toggleLoginPasswordVisibility() {
        isLoginPasswordVisible.value = !isLoginPasswordVisible.value
    }

    fun toggleSignupPasswordVisibility() {
        isSignupPasswordVisible.value = !isSignupPasswordVisible.value
    }

    fun toggleSignupConfirmPasswordVisibility() {
        isSignupConfirmPasswordVisible.value = !isSignupConfirmPasswordVisible.value
    }

    fun toggleCurrentPasswordVisibility() {
        isCurrentPasswordVisible.value = !isCurrentPasswordVisible.value
    }

    fun toggleNewPasswordVisibility() {
        isNewPasswordVisible.value = !isNewPasswordVisible.value
    }

    fun toggleConfirmNewPasswordVisibility() {
        isConfirmNewPasswordVisible.value = !isConfirmNewPasswordVisible.value
    }

    fun toggleResetPasswordVisibility() {
        isResetPasswordVisible.value = !isResetPasswordVisible.value
    }

    fun toggleResetConfirmPasswordVisibility() {
        isResetConfirmPasswordVisible.value = !isResetConfirmPasswordVisible.value
    }

    fun getPasswordStrength(password: String): PasswordStrength {
        return SecurityUtils.checkStrength(password)
    }

    fun login() {
        clearFeedback()
        val email = loginEmail.value.trim()
        val password = loginPassword.value

        if (email.isBlank()) {
            _errorMessage.value = "Please enter your email address."
            return
        }
        if (password.isEmpty()) {
            _errorMessage.value = "Please enter your password."
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.login(email, password)
            _isLoading.value = false
            result.fold(
                onSuccess = { user ->
                    _currentUser.value = user
                    _authMode.value = AuthMode.PROFILE
                    _successMessage.value = "Welcome back, ${user.name}!"
                    loginPassword.value = ""
                },
                onFailure = { error ->
                    _errorMessage.value = error.localizedMessage ?: "Login failed. Please check your credentials."
                }
            )
        }
    }

    fun signUp() {
        clearFeedback()
        val name = signupName.value.trim()
        val email = signupEmail.value.trim()
        val password = signupPassword.value
        val confirm = signupConfirmPassword.value

        if (name.isBlank()) {
            _errorMessage.value = "Please enter your full name."
            return
        }
        if (email.isBlank()) {
            _errorMessage.value = "Please enter your email address."
            return
        }
        if (password.isEmpty()) {
            _errorMessage.value = "Please choose a password."
            return
        }
        if (password != confirm) {
            _errorMessage.value = "Passwords do not match. Please ensure both fields are identical."
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.signUp(name, email, password)
            _isLoading.value = false
            result.fold(
                onSuccess = { user ->
                    _currentUser.value = user
                    _authMode.value = AuthMode.PROFILE
                    _successMessage.value = "Account created successfully! Welcome to Crown & Blade."
                    // Clear inputs
                    signupName.value = ""
                    signupEmail.value = ""
                    signupPassword.value = ""
                    signupConfirmPassword.value = ""
                },
                onFailure = { error ->
                    _errorMessage.value = error.localizedMessage ?: "Sign up failed."
                }
            )
        }
    }

    fun changePassword() {
        clearFeedback()
        val user = _currentUser.value
        if (user == null) {
            _errorMessage.value = "No active user session found. Please sign in."
            return
        }

        val current = currentPasswordInput.value
        val newPass = newPasswordInput.value
        val confirm = confirmNewPasswordInput.value

        if (current.isEmpty()) {
            _errorMessage.value = "Please enter your current password."
            return
        }
        if (newPass.isEmpty()) {
            _errorMessage.value = "Please enter your new password."
            return
        }
        if (newPass != confirm) {
            _errorMessage.value = "New password and confirmation do not match."
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.changePassword(user.email, current, newPass)
            _isLoading.value = false
            result.fold(
                onSuccess = {
                    _successMessage.value = "Your password has been changed successfully."
                    currentPasswordInput.value = ""
                    newPasswordInput.value = ""
                    confirmNewPasswordInput.value = ""
                    _authMode.value = AuthMode.PROFILE
                },
                onFailure = { error ->
                    _errorMessage.value = error.localizedMessage ?: "Failed to change password."
                }
            )
        }
    }

    fun requestPasswordReset() {
        clearFeedback()
        val email = resetEmailInput.value.trim()
        if (email.isBlank()) {
            _errorMessage.value = "Please enter your registered email address."
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.requestPasswordReset(email)
            _isLoading.value = false
            result.fold(
                onSuccess = { token ->
                    resetToken.value = token
                    _authMode.value = AuthMode.RESET_PASSWORD
                    _successMessage.value = "Reset instructions sent to $email. Enter your new password below."
                },
                onFailure = { error ->
                    _errorMessage.value = error.localizedMessage ?: "Failed to send reset email."
                }
            )
        }
    }

    fun submitPasswordReset() {
        clearFeedback()
        val email = resetEmailInput.value.trim()
        val newPass = resetNewPasswordInput.value
        val confirm = resetConfirmPasswordInput.value

        if (newPass.isEmpty()) {
            _errorMessage.value = "Please enter your new password."
            return
        }
        if (newPass != confirm) {
            _errorMessage.value = "New passwords do not match."
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.resetPasswordWithToken(email, newPass)
            _isLoading.value = false
            result.fold(
                onSuccess = {
                    _successMessage.value = "Password reset successfully! You can now sign in with your new password."
                    resetNewPasswordInput.value = ""
                    resetConfirmPasswordInput.value = ""
                    loginEmail.value = email
                    _authMode.value = AuthMode.LOGIN
                },
                onFailure = { error ->
                    _errorMessage.value = error.localizedMessage ?: "Failed to reset password."
                }
            )
        }
    }

    fun verifyEmail() {
        clearFeedback()
        val user = _currentUser.value
        if (user == null) {
            _errorMessage.value = "Please sign in to verify your email."
            return
        }

        val code = verificationCodeInput.value.trim()
        if (code.isBlank()) {
            _errorMessage.value = "Please enter the 6-digit verification code sent to ${user.email}."
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.verifyEmail(user.email, code)
            _isLoading.value = false
            result.fold(
                onSuccess = {
                    _currentUser.value = user.copy(isEmailVerified = true)
                    _successMessage.value = "Email verified successfully! Your account is fully activated."
                    verificationCodeInput.value = ""
                    _authMode.value = AuthMode.PROFILE
                },
                onFailure = { error ->
                    _errorMessage.value = error.localizedMessage ?: "Email verification failed."
                }
            )
        }
    }

    fun resendVerificationEmail() {
        clearFeedback()
        val user = _currentUser.value ?: return

        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.resendVerificationEmail(user.email)
            _isLoading.value = false
            result.fold(
                onSuccess = { code ->
                    _successMessage.value = "New verification email sent to ${user.email}! (Simulated code: $code)"
                },
                onFailure = { error ->
                    _errorMessage.value = error.localizedMessage ?: "Failed to resend verification email."
                }
            )
        }
    }

    fun logout() {
        _currentUser.value = null
        clearFeedback()
        _authMode.value = AuthMode.LOGIN
        loginPassword.value = ""
        _successMessage.value = "You have been logged out securely."
    }
}
