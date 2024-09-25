package com.koogin.littlelemon

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun Profile(navController: NavHostController) {


    val context = LocalContext.current

    val sharedPreferences by lazy {
        context.getSharedPreferences(MainActivity.SHARED_PREFS_NAME, MODE_PRIVATE)
    }



    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.little_lemon_logo),
                contentDescription = "LittleLemon Logo",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .padding(20.dp)
                    .width(185.dp)
                    .height(40.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
                verticalArrangement = Arrangement.Center,
            ) {
                val firstName = rememberSaveable {
                    mutableStateOf(sharedPreferences.getString(MainActivity.FIRST_NAME_KEY, "").toString())
                }

                val lastName = rememberSaveable {
                    mutableStateOf(sharedPreferences.getString(MainActivity.LAST_NAME_KEY, "").toString())
                }
                val email = rememberSaveable {
                    mutableStateOf(sharedPreferences.getString(MainActivity.EMAIL_KEY, "").toString())
                }


                Text(
                    text = "Personal Information",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 40.dp)
                )

                CustomTextField (
                    label = "First Name",
                    placeholder = "Tilly",
                    value = firstName.value,
                    readOnly = true,
                    onValueChange = { firstName.value = it }
                )

                CustomTextField (
                    label = "Last Name",
                    placeholder = "Tilly",
                    readOnly = true,
                    value = lastName.value,
                    onValueChange = { lastName.value = it }
                )

                CustomTextField (
                    label = "Email",
                    placeholder = "tilly.doe@littlelemon.com",
                    readOnly = true,
                    value = email.value,
                    onValueChange = { email.value = it }
                )

            }

            Button(
                colors = ButtonDefaults.buttonColors(Color(0xFFF4CE14)),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    sharedPreferences.edit().clear().apply()
                    navController.navigate(Onboarding.route)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Logout", color = Color.Black)
            }

        }
    }
}