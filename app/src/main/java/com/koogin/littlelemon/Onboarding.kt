package com.koogin.littlelemon

import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
fun Onboarding(navController: NavHostController) {
    val context = LocalContext.current
     val sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREFS_NAME, MODE_PRIVATE)

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            val firstName = rememberSaveable { mutableStateOf("") }
            val lastName = rememberSaveable { mutableStateOf("") }
            val email = rememberSaveable { mutableStateOf("") }


            Image(
                painter = painterResource(id = R.drawable.little_lemon_logo),
                contentDescription = "LittleLemon Logo",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .padding(20.dp)
                    .width(185.dp)
                    .height(40.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.15f)
                    .background(color = Color(0xFF495E57))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Let's get to know you", fontSize = 24.sp, color = Color.White)
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
            ) {


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
                    onValueChange = { firstName.value = it }
                )

                CustomTextField (
                    label = "Last Name",
                    placeholder = "Tilly",
                    value = lastName.value,
                    onValueChange = { lastName.value = it }
                )

                CustomTextField (
                    label = "Email",
                    placeholder = "tilly.doe@littlelemon.com",
                    value = email.value,
                    onValueChange = { email.value = it }
                )

            }

            Button(
                colors = ButtonDefaults.buttonColors(Color(0xFFF4CE14)),
                shape = RoundedCornerShape(8.dp),
                onClick = {

                    if(firstName.value.isBlank() || lastName.value.isBlank() || email.value.isBlank()) {
                        Toast.makeText(context, "Registration unsuccessful. Please enter all data.", Toast.LENGTH_SHORT).show()
                    } else {

                        sharedPreferences
                            .edit()
                            .putString(MainActivity.FIRST_NAME_KEY, firstName.value)
                            .putString(MainActivity.LAST_NAME_KEY, lastName.value)
                            .putString(MainActivity.EMAIL_KEY, email.value).apply()

                        Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                        navController.navigate(Profile.route)
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Register", color = Color.Black)
            }

        }
    }

}


@Composable
fun CustomTextField (
    label: String, placeholder: String, value: String, onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
) {

    Column (modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            value = value,
            readOnly = readOnly,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
        )
    }

}


