package com.dv.comfortly.data.managers

import android.content.Context
import androidx.preference.PreferenceManager

interface UserManager {
    fun getUserId(): String

    class Default(context: Context) : UserManager {
        companion object {
            private const val USER_ID_PREF = "USER_ID_PREF"
            private const val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
            private const val USER_ID_LENGTH = 10
        }

        private val sharedPrefs by lazy {
            PreferenceManager.getDefaultSharedPreferences(context)
        }

        override fun getUserId(): String {
            return if (sharedPrefs.contains(USER_ID_PREF)) {
                sharedPrefs.getString(USER_ID_PREF, null) ?: "unknown"
            } else {
                val userId = getRandomString(USER_ID_LENGTH)

                val editor = sharedPrefs.edit()
                editor.putString(USER_ID_PREF, userId)
                editor.commit()

                userId
            }
        }

        private fun getRandomString(sizeOfRandomString: Int): String =
            (0 until sizeOfRandomString)
                .map {
                    ALLOWED_CHARACTERS.random()
                }.joinToString("")
    }
}
