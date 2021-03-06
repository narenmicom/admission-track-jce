package com.micom.admissiontrackjce

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.micom.admissiontrackjce.destinations.LoginScreenDestination
import com.micom.admissiontrackjce.destinations.TextInputsDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.sql.Timestamp
import java.util.*

val stamp = Timestamp(System.currentTimeMillis())
val currentDate = Date(stamp.time)

private lateinit var auth: FirebaseAuth
fun sendData(    category: String,
                 name: String,
                 number: String,
                 enquirytype: String,
                 dept: String,
                 area: String,
                 pincode: String,
                 status: String){

        val db = Firebase.firestore

        val et = Enquirydetail(category, name, number, enquirytype, dept, area,pincode,status,currentDate)
        db.collection("et").add(et).addOnCompleteListener {
            Log.d("Firebase","Submitted Succesfully")
        }.addOnFailureListener {
            Log.d("Firebase","Save Failed $it")
        }
}

@Destination
@Composable
fun TextInputs(navigator: DestinationsNavigator) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ){
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            var name by remember { mutableStateOf("") }
            var area by remember { mutableStateOf("") }
            var status by remember { mutableStateOf("") }
            var number by remember { mutableStateOf("") }
            var enquirytype by remember { mutableStateOf("") }
            var department by remember { mutableStateOf("") }
            var category by remember { mutableStateOf("") }
            var pincode by remember { mutableStateOf("") }
            val context = LocalContext.current

            auth = FirebaseAuth.getInstance()

            // for preview add same text to all the fields
            // Normal Text Input field with floating label
            // placeholder is same as hint in xml of edit text
            TopAppBar(
                title = {
                    Text(text = "Admission Enquiry",color = Color.White)
                },
                actions = {
                    IconButton(onClick = {
                        auth.signOut()
                        navigator.navigate(LoginScreenDestination())
                    }) {
                        Icon(imageVector = Icons.Filled.Logout,"")
                    }
                }
            )
            OutlinedTextField(
                value = name,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text(text = "Name") },
                placeholder = { Text(text = "Name") },
                onValueChange = {
                    name = it
                }
            )


            // Outlined Text input field with input type number
            // It will open the number keyboard
            OutlinedTextField(value = number,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "Phone number") },
                placeholder = { Text(text = "") },
                onValueChange = {
                    number = it
                }
            )

            enquirytype = dropDownMenu(suggestions = listOf("Phone Call", "Whatsapp", "Chatbot", "Direct","Email","Consultant"), listName = "Enquiry Type")

            category = dropDownMenu(suggestions = listOf("UG","Lateral","PG"), listName = "Category")

            department = dropDownMenu(suggestions = listOf("IT","CSE","ECE","EEE","CIVIL","MECH","BME","AI & DS","CS & BS","AI & ML","Cyber Secuirty"), listName = "Department")

            OutlinedTextField(
                value = area,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text(text = "Area") },
                placeholder = { Text(text = "") },
                onValueChange = {
                    area = it
                }
            )
            OutlinedTextField(
                value = pincode,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "Pincode") },
                placeholder = { Text(text = "") },
                onValueChange = {
                    pincode = it
                }
            )

            status = dropDownMenu(suggestions = listOf("Enquiry","Positive","May be Admitted","May be Converted","Admitted","Rejected"), listName = "Status")

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {if (category!="" && name!="" && number!="" && enquirytype!="" && department!="" && area!="" && pincode!="" && status!=""){
                sendData(category,name,number,enquirytype,department,area,pincode,status)
                Toast.makeText(context,"Submitted Successfully",Toast.LENGTH_LONG).show()
                navigator.navigate(TextInputsDestination())
            }
                else {
                Toast.makeText(context,"Fill all the Fields correctly",Toast.LENGTH_LONG).show()
            }
            },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
            ) {
                Text("Submit")
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun dropDownMenu(suggestions: List<String>, listName: String): String {

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded} ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            readOnly = true,
            value = selectedText,
            label = { Text(listName)},
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            })
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }) {
                suggestions.forEach{
                    selectedOption ->
                    DropdownMenuItem(onClick = {
                        selectedText = selectedOption
                        expanded = false
                    }) {
                        Text(text = selectedOption)
                    }
                }
            }
    }
    return selectedText
}

