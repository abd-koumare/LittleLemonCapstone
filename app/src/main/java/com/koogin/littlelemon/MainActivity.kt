package com.koogin.littlelemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.koogin.littlelemon.ui.theme.LittleLemonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()


            val context = applicationContext.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
            val firstName = context.getString(FIRST_NAME_KEY, "").toString()
            val lastName = context.getString(LAST_NAME_KEY, "").toString()
            val email = context.getString(EMAIL_KEY, "").toString()


            LittleLemonTheme {

                if ( email.isNotBlank() && lastName.isNotBlank() && firstName.isNotBlank()) {
                    Navigation(navController = navController, startDestination = Home.route)
                } else {
                    Navigation(navController = navController, startDestination = Onboarding.route)
                }
            }
        }
    }


    companion object {
        const val SHARED_PREFS_NAME = "LittleLemonPrefs"
        const val EMAIL_KEY = "email"
        const val FIRST_NAME_KEY = "firstName"
        const val LAST_NAME_KEY = "lastName"
    }
}
