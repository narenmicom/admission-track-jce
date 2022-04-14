package com.micom.admissiontrackjce

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.micom.admissiontrackjce.destinations.TextInputsDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(start = true)
@Composable
fun LoginScreen(navigator: DestinationsNavigator){
    var mailid by remember{ mutableStateOf("")}
    var password by remember{ mutableStateOf("")}
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
    ) {
        TextField(value = mailid,
            onValueChange = {
            mailid = it
        },
            label = { Text(text = "Mail ID") },
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    Icons.Filled.Email,"EmailIcon"
                )
            }
            )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password,
            onValueChange = {
                password = it
            },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    Icons.Filled.Lock,"LockIcon"
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
           navigator.navigate(TextInputsDestination())   //when the button is clicked to route to enquiry screen
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ) {
            Text(text = "Login")
        }

    }
}