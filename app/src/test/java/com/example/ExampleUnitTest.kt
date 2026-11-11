package com.example

import com.example.data.local.SampleData
import com.example.ui.screens.formatDisplayDate
import com.example.ui.screens.generateUpcomingDates
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun sampleData_isPopulated() {
        assertTrue(SampleData.barbers.isNotEmpty())
        assertTrue(SampleData.services.isNotEmpty())
        assertTrue(SampleData.reviews.isNotEmpty())
        assertTrue(SampleData.initialAppointments.isNotEmpty())
        assertTrue(SampleData.customers.isNotEmpty())

        assertEquals(4, SampleData.barbers.size)
        assertTrue(SampleData.services.any { it.isPopular })
    }

    @Test
    fun aliMurtaza_profileAndAppointment_arePresent() {
        val aliCustomer = SampleData.customers.find { it.phone == "03413274132" }
        assertNotNull(aliCustomer)
        assertEquals("Ali Murtaza", aliCustomer?.name)
        assertEquals("ameeralimangi886@gmail.com", aliCustomer?.email)

        val aliAppt = SampleData.initialAppointments.find { it.customerPhone == "03413274132" }
        assertNotNull(aliAppt)
        assertEquals("Ali Murtaza", aliAppt?.customerName)
        assertEquals("APPROVED", aliAppt?.status)
    }

    @Test
    fun upcomingDates_generation_isCorrect() {
        val dates = generateUpcomingDates(14)
        assertEquals(14, dates.size)
        assertNotNull(dates.first().formattedDate)
        assertNotNull(dates.first().dayOfWeek)
    }

    @Test
    fun formatDisplayDate_formatsProperly() {
        val formatted = formatDisplayDate("2026-11-12")
        assertTrue(formatted.contains("Nov") || formatted.contains("12"))
    }
}
