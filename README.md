# Crown & Blade — Luxury Barbershop & Admin Management Platform

Crown & Blade is a luxury gentlemen's barbershop appointment booking and management platform built for Android with Jetpack Compose, Kotlin Coroutines & Flow, Room Database persistence, and Gemini 3.8 Flash AI consultation.

---

## 📲 How to Download & Install the App (APK)

### Option 1: Direct Download via AI Studio
1. In the upper-right corner of the **Google AI Studio** window, click the **Settings / More Options** menu (or the **Export / Share** button).
2. Select **"Download APK"** (or **"Generate APK / AAB"**).
3. The compiled `app-debug.apk` file will download directly to your computer or mobile device.
4. Transfer or open the `.apk` file on your Android smartphone (Android 7.0+ / API 24+).
5. When prompted by Android, enable **"Install unknown apps"** or allow installation from your browser/file manager.
6. Tap **Install** and launch **Crown & Blade**!

### Option 2: Export Project ZIP & Build Locally
1. In the AI Studio menu, select **Export Project** / **Download as ZIP**.
2. Extract the ZIP on your computer.
3. Open a terminal inside the project directory and build the APK using Gradle:
   ```bash
   gradle :app:assembleDebug
   ```
4. The generated APK will be located at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```
5. Install directly to a connected Android phone or emulator via:
   ```bash
   gradle :app:installDebug
   ```

---

## 🌟 Key Features

### 1. Frontend Client Experience
- **Luxury Aesthetic**: Deep pitch black (`#0D0D0E`), charcoal surfaces, and rich metallic gold accents (`#D4AF37`) paired with Cinzel and Montserrat typography.
- **Hero Showcase**: Cinematic dark parlour hero photography with glowing arch mirrors, heritage highlights ("10+ Years Mastery", "4.95 ★ Rating", "100% Artisans").
- **Heritage & About Us**: The story of Crown & Blade, complimentary single-malt Scotch and espresso bar, hot towel treatments, and artisanal barbering rituals.
- **Grooming Menu & Pricing**: Categorized menu ("Combos & Spa", "Haircuts", "Beard & Shave", "Specialty") with duration badges, price breakdown, and 1-tap chair selection.
- **Master Barber Profiles**: Detailed artisan cards featuring Elias Thorne, Sophia Cruz, Julian Hayes, and Liam Vance with portrait imagery, star ratings, craft years, and specialties.
- **Client Testimonials**: Verified client reviews and star ratings.
- **Direct Contact & WhatsApp**: 1-tap WhatsApp chat integration (`wa.me`) with pre-composed inquiry message and direct phone dialer.

### 2. Interactive Appointment Booking
- **Step 1: Service Selection**: Choose from signature haircuts, beard sculpting, or royal combos.
- **Step 2: Master Barber Selection**: Select specific artisan or "Any Available Master Barber".
- **Step 3: Dynamic Date Picker**: 14-day rolling calendar with day-of-week and month indicators.
- **Step 4: Real-time Slot Availability**: Time slots dynamically update; booked slots are automatically disabled and marked.
- **Step 5: Gentleman's Details**: Full name, phone number, email, and grooming notes.
- **Digital Ticket Pass**: Instant confirmation card displaying Booking ID (`CB-XXXX`), date/time, barber, service price, and live status (`PENDING`).

### 3. Gemini 3.8 Flash AI Stylist
- **Model**: `gemini-3.8-flash` via Firebase AI Logic with on-device expert consultation engine fallback.
- **Deep Cranial & Follicle Reasoning**: Analyzes facial geometry (Square, Oval, Round, Diamond, Heart, Oblong), hair texture (Straight, Wavy, Curly, Coily), density, and beard goals.
- **Output Architecture**:
  - Title and aesthetic silhouette recommendation
  - Exact guard lengths and fade/taper specifications
  - Beard and jawline contouring advice
  - Product prescription (pomade, clay, sea salt spray, beard elixirs)
  - Direct 1-tap "Book This Recommended Look" button that pre-populates the booking system!

### 4. Executive Admin Dashboard
- **Secure PIN Authentication**: Protected access with master PIN (default demo PIN: `1234` or 1-tap quick access).
- **Real-Time Parlour Metrics**:
  - Total Appointments
  - Pending Approvals badge count
  - Approved & Upcoming Bookings
  - Completed Visits
  - Total Realized Revenue & Projected Revenue
- **Booking Management**: Filter appointments by `ALL`, `PENDING`, `APPROVED`, `COMPLETED`, `CANCELLED`. 1-tap Approve, Reject, Complete, or Delete.
- **Service & Price Management**: Add new grooming services, update prices/durations, toggle active status, delete services.
- **Barber Staff Management**: Add new artisans, adjust working hours, toggle active status.
- **Customer Directory**: View clients, phone numbers, email, total visit counts, lifetime spending, and last visit dates.

---

## 🛠 Tech Stack & Architecture

- **UI Framework**: Android Jetpack Compose (Material Design 3)
- **Language**: Kotlin 2.2.10
- **Database**: Android Room 2.7.0 with SQLite & Kotlin Flow reactive queries
- **Concurrency**: Kotlin Coroutines (`StateFlow`, `SharingStarted.WhileSubscribed`)
- **AI Engine**: Firebase AI Logic (`Firebase.ai`) with `gemini-3.8-flash` and built-in expert consultation engine
- **Typography**: Google Fonts "Cinzel" & "Montserrat" bundled locally in `res/font/`
- **Images**: Coil Compose 2.7.0 & bespoke local drawables

---

## 🚀 Running Locally & Testing

1. Open the project root in **Android Studio Ladybug (or newer)**.
2. Allow Gradle to sync dependencies (`gradle build`).
3. Run on an Android emulator (API 34+) or physical device:
   ```bash
   gradle :app:installDebug
   ```
4. Run automated unit tests:
   ```bash
   gradle :app:testDebugUnitTest
   ```
