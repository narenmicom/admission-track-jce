package com.micom.admissiontrackjce

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.micom.admissiontrackjce.destinations.RegisterationDestination
import com.micom.admissiontrackjce.destinations.ReportViewDestination
import com.micom.admissiontrackjce.destinations.TextInputsDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

private lateinit var mAuth:FirebaseAuth
@Destination(start = true)
@Composable
fun LoginScreen(navigator: DestinationsNavigator) {

    var mailid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isVisibility by remember { mutableStateOf(false) }

    mAuth = FirebaseAuth.getInstance()
    val currentuser = mAuth.currentUser
    val checkMail = currentuser?.email
    if (currentuser != null) {
        if (checkMail != null) {
            if(checkMail.contains("admin")){
                navigator.navigate(ReportViewDestination())
            }
            else{
                navigator.navigate(TextInputsDestination())
            }
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = mailid,
                onValueChange = {
                    mailid = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                label = { Text("Mail ID") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = { Text(text = "Password") },
                visualTransformation = if (isVisibility) {
                    VisualTransformation.None
                } else {
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
            Button(
                onClick = {
                    mAuth.signInWithEmailAndPassword(mailid, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            if(mailid.contains("admin")){
                                navigator.navigate(ReportViewDestination())
                            }
                            else{
                                navigator.navigate(TextInputsDestination())
                            }
                        } else {
                            Toast.makeText(context, "Incorrect Password or Email", Toast.LENGTH_SHORT).show()
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
            Button(
                onClick = {
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
}