package com.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.BarbershopBottomNav
import com.example.ui.components.BarbershopTopBar
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AiConsultantScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.BookingScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyAppointmentsScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AiConsultantViewModel
import com.example.viewmodel.AppScreen
import com.example.viewmodel.AuthViewModel
import com.example.viewmodel.BarbershopViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.recaptcha.RecaptchaAppCheckProviderFactory

private const val TAG = "AppCheckInit"
private const val DEBUG_TOKEN_EXTRA = "FIREBASE_APPCHECK_DEBUG_TOKEN"
private const val DEBUG_STORE_PREFIX = "com.google.firebase.appcheck.debug.store."
private const val DEBUG_SECRET_KEY = "com.google.firebase.appcheck.debug.DEBUG_SECRET"
private const val RECAPTCHA_SITE_KEY_RES = "recaptcha_site_key"

fun configureAppCheck(context: Context, intent: Intent?) {
    try {
        val app = if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        } else {
            FirebaseApp.getInstance()
        }

        if (app == null) {
            Log.d(TAG, "FirebaseApp is not configured (google-services.json not present). Skipping App Check configuration.")
            return
        }

        if (BuildConfig.DEBUG) {
            val debugToken = intent?.getStringExtra(DEBUG_TOKEN_EXTRA)
            if (!debugToken.isNullOrEmpty()) {
                val persistenceKey = app.persistenceKey
                val prefs = context.getSharedPreferences(
                    "$DEBUG_STORE_PREFIX$persistenceKey",
                    Context.MODE_PRIVATE
                )
                prefs.edit().putString(DEBUG_SECRET_KEY, debugToken).commit()
                Log.d(TAG, "App Check debug secret configured successfully.")
            } else {
                Log.w(TAG, "No debug token found in Intent extra.")
            }

            FirebaseAppCheck.getInstance(app).installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance()
            )
            Log.d(TAG, "DebugAppCheckProviderFactory installed.")
        } else {
            val resId = context.resources.getIdentifier(RECAPTCHA_SITE_KEY_RES, "string", context.packageName)
            if (resId != 0) {
                val siteKey = context.getString(resId)
                if (siteKey.isNotBlank()) {
                    FirebaseAppCheck.getInstance(app).installAppCheckProviderFactory(
                        RecaptchaAppCheckProviderFactory.getInstance(siteKey)
                    )
                    Log.d(TAG, "RecaptchaAppCheckProviderFactory installed.")
                }
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Failed to initialize Firebase App Check", e)
    }
}

class MainActivity : ComponentActivity() {

    private val barbershopViewModel: BarbershopViewModel by viewModels()
    private val aiViewModel: AiConsultantViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureAppCheck(this, intent)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MainAppContent(
                    barbershopViewModel = barbershopViewModel,
                    aiViewModel = aiViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}

@Composable
fun MainAppContent(
    barbershopViewModel: BarbershopViewModel,
    aiViewModel: AiConsultantViewModel,
    authViewModel: AuthViewModel
) {
    val currentScreen by barbershopViewModel.currentScreen.collectAsState()
    val isAdminLoggedIn by barbershopViewModel.isAdminLoggedIn.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    // Sync logged in user profile with booking form
    androidx.compose.runtime.LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            barbershopViewModel.customerName.value = user.name
            barbershopViewModel.customerEmail.value = user.email
        }
    }

    // Handle back button on sub-screens to return to Home
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        barbershopViewModel.navigateTo(AppScreen.HOME)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BarbershopTopBar(
                currentScreen = currentScreen,
                isAdminLoggedIn = isAdminLoggedIn,
                onNavigateToAdmin = {
                    barbershopViewModel.navigateTo(AppScreen.ADMIN)
                },
                onNavigateToAccount = {
                    barbershopViewModel.navigateTo(AppScreen.ACCOUNT)
                },
                isUserLoggedIn = currentUser != null
            )
        },
        bottomBar = {
            BarbershopBottomNav(
                currentScreen = currentScreen,
                onNavigate = { screen ->
                    barbershopViewModel.navigateTo(screen)
                }
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
        ) {
            Crossfade(
                targetState = currentScreen,
                label = "screen_crossfade"
            ) { screen ->
                when (screen) {
                    AppScreen.HOME -> HomeScreen(viewModel = barbershopViewModel)
                    AppScreen.BOOKING -> BookingScreen(viewModel = barbershopViewModel)
                    AppScreen.AI_STYLIST -> AiConsultantScreen(
                        aiViewModel = aiViewModel,
                        barbershopViewModel = barbershopViewModel
                    )
                    AppScreen.MY_BOOKINGS -> MyAppointmentsScreen(viewModel = barbershopViewModel)
                    AppScreen.ADMIN -> AdminDashboardScreen(viewModel = barbershopViewModel)
                    AppScreen.ACCOUNT -> AuthScreen(
                        authViewModel = authViewModel,
                        barbershopViewModel = barbershopViewModel
                    )
                }
            }
        }
    }
}
