package com.micom.admissiontrackjce

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.micom.admissiontrackjce.destinations.RegisterationDestination
import com.micom.admissiontrackjce.destinations.TextInputsDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

private lateinit var mAuth:FirebaseAuth
@Destination(start = true)
@Composable
fun LoginScreen(navigator: DestinationsNavigator){

    var mailid by remember{ mutableStateOf("")}
    var password by remember{ mutableStateOf("")}
    val auth = Firebase.auth
    val context = LocalContext.current
    mAuth = FirebaseAuth.getInstance()
    var isVisibility by remember { mutableStateOf(false)}

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
    ) {
        OutlinedTextField(value = mailid,
            onValueChange = {
            mailid = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = { Text( "Mail ID") },
            modifier = Modifier
                .fillMaxWidth()
            )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password,
            onValueChange = {
                password = it
            },
            label = { Text(text = "Password") },
            visualTransformation = if(isVisibility){
                VisualTransformation.None
                }else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { isVisibility = !isVisibility }) {
                    Icon(
                        imageVector = if (isVisibility) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        }, contentDescription = null
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
                         mAuth.signInWithEmailAndPassword(mailid,password).addOnCompleteListener {
                             if (it.isSuccessful) {
                                 navigator.navigate(TextInputsDestination())
                             } else {
                                 Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                             }
                         }
             //when the button is clicked to route to enquiry screen*/
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
                         navigator.navigate(RegisterationDestination())
            //when the button is clicked to route to Register Screen
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Register")
        }
    }
}