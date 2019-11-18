package com.example.unittesting

import android.content.SharedPreferences
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.core.Is
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

//@RunWith instructs IDE to initialize mockito library
@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesHelperTest {
    private var mSharedPreferenceEntry: SharedPreferenceEntry? = null
    private var mMockSharedPreferencesHelper: SharedPreferencesHelper? = null
    private var mMockBrokenSharedPreferencesHelper: SharedPreferencesHelper? = null
// @Mock creates dummy object needed by the class to be tested
    @Mock
    var mMockSharedPreferences: SharedPreferences? = null
    @Mock
    var mMockBrokenSharedPreferences: SharedPreferences? = null
    @Mock
    var mMockEditor: SharedPreferences.Editor? = null
    @Mock
    var mMockBrokenEditor: SharedPreferences.Editor? = null
//  @Before will execute before the test cases
    @Before
    fun initMocks() {
        // Create SharedPreferenceEntry to persist.
        mSharedPreferenceEntry = SharedPreferenceEntry(
            TEST_NAME, TEST_DATE_OF_BIRTH!!,
            TEST_EMAIL
        )
        mMockSharedPreferencesHelper = createMockSharedPreference()
        mMockBrokenSharedPreferencesHelper = createBrokenMockSharedPreference()
    }

    @Test
    fun sharedPreferencesHelper_SaveAndReadPersonalInformation() {
        // Save the personal information to SharedPreferences

        val success =
            mMockSharedPreferencesHelper!!.savePersonalInfo(mSharedPreferenceEntry!!)
        assertThat(
            "Checking that SharedPreferenceEntry.save... returns true",
            success, Is<Boolean>(equalTo(true))
        )
        // Read personal information from SharedPreferences


        val (name, dateOfBirth, email) = mMockSharedPreferencesHelper!!.getPersonalInfo()
        // Make sure both written and retrieved personal information are equal.


        assertThat(
            "Checking that SharedPreferenceEntry.name has been persisted and read correctly",
            mSharedPreferenceEntry!!.name,
            Is<String>(equalTo(name))
        )
        assertThat(
            "Checking that SharedPreferenceEntry.dateOfBirth has been persisted and read "
                    + "correctly",
            mSharedPreferenceEntry!!.dateOfBirth,
            Is<Calendar>(equalTo(dateOfBirth))
        )
        assertThat(
            "Checking that SharedPreferenceEntry.email has been persisted and read "
                    + "correctly",
            mSharedPreferenceEntry!!.email,
            Is<String>(equalTo(email))
        )
    }

    @Test
    fun sharedPreferencesHelper_SavePersonalInformationFailed_ReturnsFalse() {
        // Read personal information from a broken SharedPreferencesHelper

        val success =
            mMockBrokenSharedPreferencesHelper!!.savePersonalInfo(mSharedPreferenceEntry!!)
        assertThat(
            "Makes sure writing to a broken SharedPreferencesHelper returns false", success,
            Is<Boolean>(equalTo(false))
        )
    }

    /**
     * Creates a mocked SharedPreferences.
     */
    private fun createMockSharedPreference(): SharedPreferencesHelper {
        // Mocking reading the SharedPreferences as if mMockSharedPreferences was previously written
        // correctly.

        `when`(
            mMockSharedPreferences!!.getString(
                eq(SharedPreferencesHelper.KEY_NAME),
                anyString()
            )
        )
            .thenReturn(mSharedPreferenceEntry!!.name)
        `when`(
            mMockSharedPreferences!!.getString(
                eq(SharedPreferencesHelper.KEY_EMAIL),
                anyString()
            )
        )
            .thenReturn(mSharedPreferenceEntry!!.email)
        `when`(
            mMockSharedPreferences!!.getLong(
                eq(SharedPreferencesHelper.KEY_DOB),
                anyLong()
            )
        )
            .thenReturn(mSharedPreferenceEntry!!.dateOfBirth.timeInMillis)
        // Mocking a successful commit.


        `when`(mMockEditor!!.commit()).thenReturn(true)
        // Return the MockEditor when requesting it.


        `when`(mMockSharedPreferences!!.edit())
            .thenReturn(mMockEditor)
        return SharedPreferencesHelper(mMockSharedPreferences!!)
    }

    /**
     * Creates a mocked SharedPreferences that fails when writing.
     */
    private fun createBrokenMockSharedPreference(): SharedPreferencesHelper {
        // Mocking a commit that fails.

        `when`(mMockBrokenEditor!!.commit()).thenReturn(false)
        // Return the broken MockEditor when requesting it.


        `when`(
            mMockBrokenSharedPreferences!!.edit()
        ).thenReturn(mMockBrokenEditor)
        return SharedPreferencesHelper(mMockBrokenSharedPreferences!!)
    }

    companion object {
        private const val TEST_NAME = "Test name"
        private const val TEST_EMAIL = "test@email.com"
        private val TEST_DATE_OF_BIRTH: Calendar? = Calendar.getInstance()

        init {
            TEST_DATE_OF_BIRTH?.set(1980, 1, 1)

        }
    }
}