package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Appointment
import com.example.data.model.Barber
import com.example.data.model.BarbershopService
import com.example.data.model.Customer
import com.example.data.model.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface BarbershopDao {
    // Barbers
    @Query("SELECT * FROM barbers ORDER BY rating DESC")
    fun getAllBarbers(): Flow<List<Barber>>

    @Query("SELECT * FROM barbers WHERE isActive = 1 ORDER BY rating DESC")
    fun getActiveBarbers(): Flow<List<Barber>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarber(barber: Barber)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarbers(barbers: List<Barber>)

    @Update
    suspend fun updateBarber(barber: Barber)

    @Delete
    suspend fun deleteBarber(barber: Barber)

    // Services
    @Query("SELECT * FROM services ORDER BY price ASC")
    fun getAllServices(): Flow<List<BarbershopService>>

    @Query("SELECT * FROM services WHERE isActive = 1 ORDER BY price ASC")
    fun getActiveServices(): Flow<List<BarbershopService>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: BarbershopService)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<BarbershopService>)

    @Update
    suspend fun updateService(service: BarbershopService)

    @Delete
    suspend fun deleteService(service: BarbershopService)

    // Appointments
    @Query("SELECT * FROM appointments ORDER BY createdAt DESC")
    fun getAllAppointments(): Flow<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE customerPhone = :phone ORDER BY createdAt DESC")
    fun getAppointmentsForCustomer(phone: String): Flow<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE date = :date AND status != 'CANCELLED' AND status != 'REJECTED'")
    suspend fun getBookedAppointmentsForDate(date: String): List<Appointment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment)

    @Update
    suspend fun updateAppointment(appointment: Appointment)

    @Query("UPDATE appointments SET status = :status WHERE id = :id")
    suspend fun updateAppointmentStatus(id: String, status: String)

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

    @Query("DELETE FROM appointments WHERE id = :id")
    suspend fun deleteAppointmentById(id: String)

    // Reviews
    @Query("SELECT * FROM reviews ORDER BY id ASC")
    fun getAllReviews(): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<Review>)

    // Customers
    @Query("SELECT * FROM customers ORDER BY totalBookings DESC")
    fun getAllCustomers(): Flow<List<Customer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCustomer(customer: Customer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomers(customers: List<Customer>)

    // User Accounts Authentication
    @Query("SELECT * FROM user_accounts WHERE email = :email LIMIT 1")
    fun getUserAccount(email: String): Flow<com.example.data.model.UserAccount?>

    @Query("SELECT * FROM user_accounts WHERE email = :email LIMIT 1")
    suspend fun getUserAccountSync(email: String): com.example.data.model.UserAccount?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserAccount(user: com.example.data.model.UserAccount)

    @Update
    suspend fun updateUserAccount(user: com.example.data.model.UserAccount)

    @Query("UPDATE user_accounts SET passwordHash = :passwordHash, salt = :salt, resetToken = NULL WHERE email = :email")
    suspend fun updatePassword(email: String, passwordHash: String, salt: String)

    @Query("UPDATE user_accounts SET isEmailVerified = :isVerified WHERE email = :email")
    suspend fun setEmailVerified(email: String, isVerified: Boolean)

    @Query("UPDATE user_accounts SET resetToken = :token WHERE email = :email")
    suspend fun setPasswordResetToken(email: String, token: String?)

    @Query("UPDATE user_accounts SET verificationCode = :code WHERE email = :email")
    suspend fun setVerificationCode(email: String, code: String)

    @Query("UPDATE user_accounts SET lastLoginAt = :timestamp WHERE email = :email")
    suspend fun updateLastLogin(email: String, timestamp: Long)

    @Query("DELETE FROM user_accounts WHERE email = :email")
    suspend fun deleteUserAccount(email: String)
}
