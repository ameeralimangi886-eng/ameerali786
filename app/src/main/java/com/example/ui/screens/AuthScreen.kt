package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderGoldMuted
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.util.PasswordStrength
import com.example.viewmodel.AppScreen
import com.example.viewmodel.AuthMode
import com.example.viewmodel.AuthViewModel
import com.example.viewmodel.BarbershopViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    barbershopViewModel: BarbershopViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val authMode by authViewModel.authMode.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val successMessage by authViewModel.successMessage.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { barbershopViewModel.navigateTo(AppScreen.HOME) },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DarkSurface)
                    .border(1.dp, BorderGoldMuted, CircleShape)
                    .testTag("auth_back_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to Parlour",
                    tint = GoldPrimary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = if (currentUser != null) "CLIENT PROFILE" else "MEMBER AUTHENTICATION",
                    style = MaterialTheme.typography.titleLarge,
                    color = GoldPrimary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "CROWN & BLADE VAULT • SECURE CREDENTIALS",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Feedback Messages
        AnimatedVisibility(
            visible = errorMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            errorMessage?.let { msg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("auth_error_card"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF381014)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE53935))),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = "Error",
                            tint = Color(0xFFFF8A80),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = msg,
                            color = Color(0xFFFFEBEE),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = successMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            successMessage?.let { msg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("auth_success_card"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F3018)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StatusSuccess)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = StatusSuccess,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = msg,
                            color = Color(0xFFE8F5E9),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // Main Content based on state
        if (currentUser != null && authMode != AuthMode.CHANGE_PASSWORD && authMode != AuthMode.VERIFY_EMAIL) {
            UserProfileContent(
                user = currentUser!!,
                authViewModel = authViewModel,
                barbershopViewModel = barbershopViewModel
            )
        } else {
            when (authMode) {
                AuthMode.LOGIN -> LoginContent(authViewModel = authViewModel, isLoading = isLoading)
                AuthMode.SIGNUP -> SignupContent(authViewModel = authViewModel, isLoading = isLoading)
                AuthMode.FORGOT_PASSWORD -> ForgotPasswordContent(authViewModel = authViewModel, isLoading = isLoading)
                AuthMode.RESET_PASSWORD -> ResetPasswordContent(authViewModel = authViewModel, isLoading = isLoading)
                AuthMode.CHANGE_PASSWORD -> ChangePasswordContent(authViewModel = authViewModel, isLoading = isLoading)
                AuthMode.VERIFY_EMAIL -> VerifyEmailContent(authViewModel = authViewModel, isLoading = isLoading)
                AuthMode.PROFILE -> {
                    if (currentUser != null) {
                        UserProfileContent(
                            user = currentUser!!,
                            authViewModel = authViewModel,
                            barbershopViewModel = barbershopViewModel
                        )
                    } else {
                        LoginContent(authViewModel = authViewModel, isLoading = isLoading)
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginContent(
    authViewModel: AuthViewModel,
    isLoading: Boolean
) {
    val email by authViewModel.loginEmail.collectAsState()
    val password by authViewModel.loginPassword.collectAsState()
    val isPasswordVisible by authViewModel.isLoginPasswordVisible.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderGoldMuted, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mode Switcher Tabs
            TabRow(
                selectedTabIndex = 0,
                containerColor = DarkSurfaceVariant,
                contentColor = GoldPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[0]),
                        color = GoldPrimary,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(bottom = 20.dp)
            ) {
                Tab(
                    selected = true,
                    onClick = { authViewModel.setAuthMode(AuthMode.LOGIN) },
                    text = {
                        Text(
                            "Sign In",
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                    }
                )
                Tab(
                    selected = false,
                    onClick = { authViewModel.setAuthMode(AuthMode.SIGNUP) },
                    text = {
                        Text(
                            "Create Account",
                            fontWeight = FontWeight.Medium,
                            color = TextMuted
                        )
                    }
                )
            }

            Text(
                text = "Welcome Back, Gentleman",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Sign in to manage appointments and access bespoke styling.",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { authViewModel.loginEmail.value = it },
                label = { Text("Email Address") },
                placeholder = { Text("your.email@example.com") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = GoldPrimary
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = outlinedTextFieldGoldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_email_input")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Password Field with Show/Hide Toggle (Requirement 4)
            OutlinedTextField(
                value = password,
                onValueChange = { authViewModel.loginPassword.value = it },
                label = { Text("Password") },
                placeholder = { Text("Enter your password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = GoldPrimary
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { authViewModel.toggleLoginPasswordVisibility() },
                        modifier = Modifier.testTag("login_password_toggle")
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = if (isPasswordVisible) GoldPrimary else TextMuted
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = outlinedTextFieldGoldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_password_input")
            )

            // Forgot Password Link (Requirement 6)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        authViewModel.resetEmailInput.value = email
                        authViewModel.setAuthMode(AuthMode.FORGOT_PASSWORD)
                    },
                    modifier = Modifier.testTag("forgot_password_button")
                ) {
                    Text(
                        text = "Forgot Password?",
                        color = GoldLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Submit Button
            Button(
                onClick = { authViewModel.login() },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("login_submit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sign In Securely",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // Quick Demo Prefill for Convenience
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    authViewModel.loginEmail.value = "ameeralimangi886@gmail.com"
                    authViewModel.loginPassword.value = "CrownBlade2026!"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fill_demo_credentials_button"),
                shape = RoundedCornerShape(10.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(BorderGoldMuted)
                )
            ) {
                Text(
                    text = "Quick Demo: Fill Ali Murtaza Account",
                    color = GoldLight,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun SignupContent(
    authViewModel: AuthViewModel,
    isLoading: Boolean
) {
    val name by authViewModel.signupName.collectAsState()
    val email by authViewModel.signupEmail.collectAsState()
    val password by authViewModel.signupPassword.collectAsState()
    val confirmPassword by authViewModel.signupConfirmPassword.collectAsState()
    val isPasswordVisible by authViewModel.isSignupPasswordVisible.collectAsState()
    val isConfirmPasswordVisible by authViewModel.isSignupConfirmPasswordVisible.collectAsState()

    val passwordStrength = authViewModel.getPasswordStrength(password)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderGoldMuted, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mode Switcher Tabs
            TabRow(
                selectedTabIndex = 1,
                containerColor = DarkSurfaceVariant,
                contentColor = GoldPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[1]),
                        color = GoldPrimary,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(bottom = 20.dp)
            ) {
                Tab(
                    selected = false,
                    onClick = { authViewModel.setAuthMode(AuthMode.LOGIN) },
                    text = {
                        Text(
                            "Sign In",
                            fontWeight = FontWeight.Medium,
                            color = TextMuted
                        )
                    }
                )
                Tab(
                    selected = true,
                    onClick = { authViewModel.setAuthMode(AuthMode.SIGNUP) },
                    text = {
                        Text(
                            "Create Account",
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                    }
                )
            }

            Text(
                text = "Join Crown & Blade",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Create your personal gentleman's grooming vault.",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
            )

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { authViewModel.signupName.value = it },
                label = { Text("Full Name") },
                placeholder = { Text("e.g. Ali Murtaza") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Name",
                        tint = GoldPrimary
                    )
                },
                singleLine = true,
                colors = outlinedTextFieldGoldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("signup_name_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email Field (Requirement 1: Users enter their own email)
            OutlinedTextField(
                value = email,
                onValueChange = { authViewModel.signupEmail.value = it },
                label = { Text("Email Address") },
                placeholder = { Text("your.own.email@example.com") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = GoldPrimary
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = outlinedTextFieldGoldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("signup_email_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password Field (Requirement 2 & 3: Users create their own password & can choose any password)
            OutlinedTextField(
                value = password,
                onValueChange = { authViewModel.signupPassword.value = it },
                label = { Text("Choose Password") },
                placeholder = { Text("Any password you want") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = GoldPrimary
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { authViewModel.toggleSignupPasswordVisibility() },
                        modifier = Modifier.testTag("signup_password_toggle")
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = if (isPasswordVisible) GoldPrimary else TextMuted
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = outlinedTextFieldGoldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("signup_password_input")
            )

            // Password Strength Feedback Bar
            if (password.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                PasswordStrengthBar(strength = passwordStrength)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm Password Field (Requirement 4: Show/Hide toggle)
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { authViewModel.signupConfirmPassword.value = it },
                label = { Text("Confirm Password") },
                placeholder = { Text("Re-enter your chosen password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = "Confirm Password",
                        tint = GoldPrimary
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { authViewModel.toggleSignupConfirmPasswordVisibility() },
                        modifier = Modifier.testTag("signup_confirm_password_toggle")
                    ) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password",
                            tint = if (isConfirmPasswordVisible) GoldPrimary else TextMuted
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = outlinedTextFieldGoldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("signup_confirm_password_input")
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = { authViewModel.signUp() },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("signup_submit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Create My Account",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ForgotPasswordContent(
    authViewModel: AuthViewModel,
    isLoading: Boolean
) {
    val email by authViewModel.resetEmailInput.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderGoldMuted, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(GoldPrimary.copy(alpha = 0.15f))
                    .border(1.dp, GoldPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LockReset,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Forgot Password?",
                style = MaterialTheme.typography.titleLarge,
                color = GoldPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Enter your registered email address. We will verify your account and allow you to set your new password of choice.",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { authViewModel.resetEmailInput.value = it },
                label = { Text("Registered Email") },
                placeholder = { Text("your.email@example.com") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = GoldPrimary
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = outlinedTextFieldGoldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reset_email_input")
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = { authViewModel.requestPasswordReset() },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("send_reset_email_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Send Password Reset Email",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = { authViewModel.setAuthMode(AuthMode.LOGIN) },
                modifier = Modifier.testTag("back_to_login_button")
            ) {
                Text("Back to Sign In", color = GoldLight)
            }
        }
    }
}

@Composable
private fun ResetPasswordContent(
    authViewModel: AuthViewModel,
    isLoading: Boolean
) {
    val email by authViewModel.resetEmailInput.collectAsState()
    val newPassword by authViewModel.resetNewPasswordInput.collectAsState()
    val confirmPassword by authViewModel.resetConfirmPasswordInput.collectAsState()
    val isPasswordVisible by authViewModel.isResetPasswordVisible.collectAsState()
    val isConfirmPasswordVisible by authViewModel.isResetConfirmPasswordVisible.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderGoldMuted, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Set New Password",
                style = MaterialTheme.typography.titleLarge,
                color = GoldPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Resetting password for $email.\nYou control your new password — pick any password you wish.",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
            )

            // New Password (Requirement 2, 3, 4)
            OutlinedTextField(
                value = newPassword,
                onValueChange = { authViewModel.resetNewPasswordInput.value = it },
                label = { Text("Choose New Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "New Password",
                        tint = GoldPrimary
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { authViewModel.toggleResetPasswordVisibility() },
                        modifier = Modifier.testTag("reset_new_password_toggle")
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = if (isPasswordVisible) GoldPrimary else TextMuted
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = outlinedTextFieldGoldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reset_new_password_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm New Password (Requirement 4)
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { authViewModel.resetConfirmPasswordInput.value = it },
                label = { Text("Confirm New Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = "Confirm New Password",
                        tint = GoldPrimary
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { authViewModel.toggleResetConfirmPasswordVisibility() },
                        modifier = Modifier.testTag("reset_confirm_password_toggle")
                    ) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password",
                            tint = if (isConfirmPasswordVisible) GoldPrimary else TextMuted
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = outlinedTextFieldGoldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reset_confirm_password_input")
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { authViewModel.submitPasswordReset() },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_reset_password_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Save New Password",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ChangePasswordContent(
    authViewModel: AuthViewModel,
    isLoading: Boolean
) {
    val currentPassword by authViewModel.currentPasswordInput.collectAsState()
    val newPassword by authViewModel.newPasswordInput.collectAsState()
    val confirmPassword by authViewModel.confirmNewPasswordInput.collectAsState()
    val isCurrentVisible by authViewModel.isCurrentPasswordVisible.collectAsState()
    val isNewVisible by authViewModel.isNewPasswordVisible.collectAsState()
    val isConfirmVisible by authViewModel.isConfirmNewPasswordVisible.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderGoldMuted, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Change Your Password",
                style = MaterialTheme.typography.titleLarge,
                color = GoldPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Update your password whenever you wish. You maintain complete control of your credentials.",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            // Current Password
            OutlinedTextField(
                value = currentPassword,
                onValueChange = { authViewModel.currentPasswordInput.value = it },
                label = { Text("Current Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Current Password",
                        tint = GoldPrimary
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { authViewModel.toggleCurrentPasswordVisibility() },
                        modifier = Modifier.testTag("current_password_toggle")
                    ) {
                        Icon(
                            imageVector = if (isCurrentVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isCurrentVisible) "Hide password" else "Show password",
                            tint = if (isCurrentVisible) GoldPrimary else TextMuted
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (isCurrentVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = outlinedTextFieldGoldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("current_password_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // New Password
            OutlinedTextField(
                value = newPassword,
                onValueChange = { authViewModel.newPasswordInput.value = it },
                label = { Text("New Password") },
                placeholder = { Text("Enter any new password you choose") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = "New Password",
                        tint = GoldPrimary
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { authViewModel.toggleNewPasswordVisibility() },
                        modifier = Modifier.testTag("new_password_toggle")
                    ) {
                        Icon(
                            imageVector = if (isNewVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isNewVisible) "Hide password" else "Show password",
                            tint = if (isNewVisible) GoldPrimary else TextMuted
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (isNewVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = outlinedTextFieldGoldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("new_password_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm New Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { authViewModel.confirmNewPasswordInput.value = it },
                label = { Text("Confirm New Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LockReset,
                        contentDescription = "Confirm New Password",
                        tint = GoldPrimary
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { authViewModel.toggleConfirmNewPasswordVisibility() },
                        modifier = Modifier.testTag("confirm_new_password_toggle")
                    ) {
                        Icon(
                            imageVector = if (isConfirmVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isConfirmVisible) "Hide password" else "Show password",
                            tint = if (isConfirmVisible) GoldPrimary else TextMuted
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (isConfirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = outlinedTextFieldGoldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("confirm_new_password_input")
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { authViewModel.changePassword() },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_change_password_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Update My Password",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = { authViewModel.setAuthMode(AuthMode.PROFILE) },
                modifier = Modifier.testTag("cancel_change_password_button")
            ) {
                Text("Cancel", color = TextMuted)
            }
        }
    }
}

@Composable
private fun VerifyEmailContent(
    authViewModel: AuthViewModel,
    isLoading: Boolean
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val code by authViewModel.verificationCodeInput.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderGoldMuted, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(GoldPrimary.copy(alpha = 0.15f))
                    .border(1.dp, GoldPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MarkEmailRead,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Verify Your Email",
                style = MaterialTheme.typography.titleLarge,
                color = GoldPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "We sent an email verification message to ${currentUser?.email ?: "your address"}.\nEnter the 6-digit verification code below:",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            OutlinedTextField(
                value = code,
                onValueChange = { authViewModel.verificationCodeInput.value = it },
                label = { Text("6-Digit Code") },
                placeholder = { Text("e.g. 786786") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = outlinedTextFieldGoldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("verification_code_input")
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = { authViewModel.verifyEmail() },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_verify_email_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Verify Email Address",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { authViewModel.resendVerificationEmail() },
                    modifier = Modifier.testTag("resend_verification_email_button")
                ) {
                    Text("Resend Email", color = GoldLight)
                }

                TextButton(
                    onClick = { authViewModel.setAuthMode(AuthMode.PROFILE) }
                ) {
                    Text("Back to Profile", color = TextMuted)
                }
            }
        }
    }
}

@Composable
private fun UserProfileContent(
    user: com.example.data.model.UserAccount,
    authViewModel: AuthViewModel,
    barbershopViewModel: BarbershopViewModel
) {
    val appointments by barbershopViewModel.appointments.collectAsState()
    val userAppointments = appointments.filter { it.customerEmail.equals(user.email, ignoreCase = true) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // User Profile Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGoldMuted, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar with gold border
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(GoldPrimary.copy(alpha = 0.2f))
                        .border(2.dp, GoldPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                        color = GoldPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Verification Badge (Requirement 8)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (user.isEmailVerified) StatusSuccess.copy(alpha = 0.15f)
                            else Color(0xFFF57C00).copy(alpha = 0.15f)
                        )
                        .border(
                            1.dp,
                            if (user.isEmailVerified) StatusSuccess else Color(0xFFF57C00),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("verification_status_chip")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (user.isEmailVerified) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = if (user.isEmailVerified) StatusSuccess else Color(0xFFFFA726),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (user.isEmailVerified) "Email Verified" else "Email Pending Verification",
                            color = if (user.isEmailVerified) StatusSuccess else Color(0xFFFFA726),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (!user.isEmailVerified) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { authViewModel.setAuthMode(AuthMode.VERIFY_EMAIL) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("start_email_verification_button")
                    ) {
                        Text("Verify Email Now", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Security Actions Card (Requirement 5 & 11: Change Password Option)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGoldMuted, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Account Security & Credentials",
                        style = MaterialTheme.typography.titleMedium,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Change Password Row Action
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurfaceVariant)
                        .clickable { authViewModel.setAuthMode(AuthMode.CHANGE_PASSWORD) }
                        .padding(14.dp)
                        .testTag("change_password_row"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Change Password",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Update your password anytime (always under your control)",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = "Change Password",
                        tint = GoldPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Passwords & Encryption Info (Requirement 14)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF141416))
                        .border(1.dp, BorderGoldMuted.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "🔒 Zero Plaintext Storage: Your password is encrypted using salted SHA-256 with 5,000 key-stretching iterations. Passwords are never auto-generated or modified without your direct command.",
                        color = TextMuted,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Client Bookings Summary
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGoldMuted, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Appointments (${userAppointments.size})",
                        style = MaterialTheme.typography.titleMedium,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(onClick = { barbershopViewModel.navigateTo(AppScreen.MY_BOOKINGS) }) {
                        Text("View Passes", color = GoldLight, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Log Out Button (Requirement 12)
        Button(
            onClick = { authViewModel.logout() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("logout_button"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C1014)),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE53935).copy(alpha = 0.6f))
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Log Out",
                tint = Color(0xFFFF8A80),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Log Out of Session",
                color = Color(0xFFFF8A80),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PasswordStrengthBar(strength: PasswordStrength) {
    val color = when (strength) {
        PasswordStrength.VERY_WEAK -> Color(0xFFE53935)
        PasswordStrength.WEAK -> Color(0xFFFB8C00)
        PasswordStrength.FAIR -> Color(0xFFFDD835)
        PasswordStrength.STRONG -> Color(0xFF43A047)
        PasswordStrength.VERY_STRONG -> StatusSuccess
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Password Strength (Optional Guide):",
                color = TextMuted,
                fontSize = 11.sp
            )
            Text(
                text = strength.label,
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in 1..5) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (i <= strength.score) color else DarkSurfaceVariant)
                )
            }
        }
    }
}

@Composable
private fun outlinedTextFieldGoldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GoldPrimary,
    unfocusedBorderColor = BorderGoldMuted,
    focusedLabelColor = GoldPrimary,
    unfocusedLabelColor = TextMuted,
    cursorColor = GoldPrimary,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedContainerColor = DarkSurfaceVariant,
    unfocusedContainerColor = DarkSurfaceVariant
)
