package com.micom.admissiontrackjce

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

fun sendData(name: String,
                 number: String,
                 enquirytype: String,
                 dept: String,
                 address: String,
                 status: String) {

        val db = Firebase.firestore
        val et = Enquirydetail(name, number, enquirytype, dept, address, status)
        db.collection("et").add(et).addOnCompleteListener {
            Log.d("Firebase","Document Saved")
        }.addOnFailureListener {
            Log.d("Firebase","Save Failed $it")

        }
}



@Destination
@Composable
fun TextInputs(navigator: DestinationsNavigator) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ){

        Column {
            Text(text = "Admission Enquiry", style = MaterialTheme.typography.h5, modifier = Modifier.padding(8.dp))
            var name by remember { mutableStateOf("") }
            var address by remember { mutableStateOf("") }
            var status by remember { mutableStateOf("") }
            var number by remember { mutableStateOf("") }
            var enquirytype by remember { mutableStateOf("") }
            var department by remember { mutableStateOf("") }
            val context = LocalContext.current

            // for preview add same text to all the fields

            // Normal Text Input field with floating label
            // placeholder is same as hint in xml of edit text
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

            enquirytype = DropDownMenu(suggestions = listOf("Phone Call", "Whatsapp", "Chatbot", "Direct","Email","Consultant"), listName = "Enquiry Type")



            department = DropDownMenu(suggestions = listOf("IT","CSE","ECE","EEE","CIVIL","MECH","BME"), listName = "Department")

            OutlinedTextField(
                value = address,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text(text = "Address") },
                placeholder = { Text(text = "") },
                onValueChange = {
                    address = it
                }
            )
            OutlinedTextField(
                value = status,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text(text = "Status") },
                placeholder = { Text(text = "") },
                onValueChange = {
                    status = it
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                             sendData(name,number,enquirytype,department,address,status)
                Toast.makeText(context,
                    "Submitted Successfully",Toast.LENGTH_LONG).show()

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

@Composable
fun DropDownMenu(suggestions: List<String>,listName: String): String {

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    OutlinedTextField(
        value = selectedText,
        onValueChange = { selectedText = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .onGloballyPositioned { coordinates ->
                //This value is used to assign to the DropDown the same width
                textfieldSize = coordinates.size.toSize()
            },
        label = { Text(listName) },
        trailingIcon = {
            Icon(icon,"",
                Modifier.clickable { expanded = !expanded })
        }
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .width(with(LocalDensity.current){textfieldSize.width.toDp()})
    ) {
        suggestions.forEach { label ->
            DropdownMenuItem(onClick = {
                selectedText = label
                expanded = false
            }) {
                Text(text = label)
            }
        }
    }
    return selectedText
}
