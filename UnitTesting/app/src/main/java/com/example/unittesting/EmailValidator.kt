package com.example.unittesting

import android.text.Editable
import android.text.TextWatcher
import java.util.regex.Pattern

class EmailValidator : TextWatcher {


    /**
     * Validates if the given input is a valid email address.
     *
     * @param email        The email to validate.
     * @return `true` if the input is a valid email. `false` otherwise.
     */
     fun isValidEmail(email: CharSequence?): Boolean {
        return email != null && EMAIL_PATTERN!!.matcher(email).matches()
    }

    override fun afterTextChanged(s: Editable?) {
        misValid = isValidEmail(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
    }

    companion object {
        /**
         * Email validation pattern.
         */
        val EMAIL_PATTERN: Pattern? = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        var misValid = false
        fun isValidEmailTest(email: CharSequence?): Boolean {
            return email != null && EMAIL_PATTERN!!.matcher(email).matches()
        }
    }
}