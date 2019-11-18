package com.example.unittesting

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        // Logger for this class.
        const val TAG = "MainActivity"
    }

    // The helper that manages writing to SharedPreferences.
    private lateinit var mSharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup field validators.
        val mEmailValidator = EmailValidator()
        emailInput.addTextChangedListener(mEmailValidator)
        // Instantiate a SharedPreferencesHelper.
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        mSharedPreferencesHelper = SharedPreferencesHelper(sharedPreferences)
        // Fill input fields from data retrieved from the SharedPreferences.
        populateUi()
    }


    /**
     * Initialize all fields from the personal info saved in the SharedPreferences.
     */
    private fun populateUi() {
        val sharedPreferenceEntry: SharedPreferenceEntry = mSharedPreferencesHelper.getPersonalInfo()
        userNameInput.setText(sharedPreferenceEntry.name)
        val dateOfBirth: Calendar = sharedPreferenceEntry.dateOfBirth
        dateOfBirthInput.init(
            dateOfBirth.get(Calendar.YEAR), dateOfBirth.get(Calendar.MONTH),
            dateOfBirth.get(Calendar.DAY_OF_MONTH), null
        )
        emailInput.setText(sharedPreferenceEntry.email)
    }

    /**
     * Called when the "Save" button is clicked.
     */

    fun onSaveClick(view: View) {
        if (!EmailValidator.misValid) {
            emailInput.setText("Invalid Email")
            Log.w(TAG, "Not saving personal information: Invalid email")
            return
        }
        // Get the text from the input fields.
        val name: String = userNameInput.text.toString()
        val dateOfBirth: Calendar = Calendar.getInstance()
        dateOfBirth.set(dateOfBirthInput.year, dateOfBirthInput.month, dateOfBirthInput.dayOfMonth)
        val email: String = emailInput.text.toString()

        // Create a Setting model class to persist.
        val sharedPreferenceEntry =            SharedPreferenceEntry(name, dateOfBirth, email)

        // Persist the personal information.
        val isSuccess: Boolean =
            mSharedPreferencesHelper.savePersonalInfo(sharedPreferenceEntry)
        if (isSuccess) {
            Toast.makeText(this, "Personal information saved", Toast.LENGTH_LONG)
                .show()
            Log.i(TAG, "Personal information saved")
        } else {
            Log.e(TAG, "Failed to write personal information to SharedPreferences")
        }
    }

    /**
     * Called when the "Revert" button is clicked.
     */
    fun onRevertClick(view: View?) {
        populateUi()
        Toast.makeText(this, "Personal information reverted", Toast.LENGTH_LONG).show()
        Log.i(
            TAG, "Personal information reverted"
        )
    }


}
