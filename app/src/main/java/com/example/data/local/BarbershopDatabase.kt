package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.Appointment
import com.example.data.model.Barber
import com.example.data.model.BarbershopService
import com.example.data.model.Customer
import com.example.data.model.Review
import com.example.data.model.UserAccount
import com.example.util.SecurityUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Barber::class,
        BarbershopService::class,
        Appointment::class,
        Review::class,
        Customer::class,
        UserAccount::class
    ],
    version = 2,
    exportSchema = false
)
abstract class BarbershopDatabase : RoomDatabase() {
    abstract fun barbershopDao(): BarbershopDao

    companion object {
        @Volatile
        private var INSTANCE: BarbershopDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BarbershopDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BarbershopDatabase::class.java,
                    "crown_blade_barbershop.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(BarbershopDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class BarbershopDatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.barbershopDao())
                    }
                }
            }

            suspend fun populateDatabase(dao: BarbershopDao) {
                dao.insertBarbers(SampleData.barbers)
                dao.insertServices(SampleData.services)
                dao.insertReviews(SampleData.reviews)
                for (appointment in SampleData.initialAppointments) {
                    dao.insertAppointment(appointment)
                }
                dao.insertCustomers(SampleData.customers)

                // Pre-populate verified gentleman account for Ali Murtaza
                val initialSalt = SecurityUtils.generateSalt()
                val initialHash = SecurityUtils.hashPassword("CrownBlade2026!", initialSalt)
                val initialAccount = UserAccount(
                    email = "ameeralimangi886@gmail.com",
                    name = "Ali Murtaza",
                    passwordHash = initialHash,
                    salt = initialSalt,
                    isEmailVerified = true,
                    verificationCode = "786786"
                )
                dao.insertUserAccount(initialAccount)
            }
        }
    }
}
