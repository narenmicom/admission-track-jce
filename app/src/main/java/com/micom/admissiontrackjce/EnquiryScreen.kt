package com.micom.admissiontrackjce

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.micom.admissiontrackjce.destinations.LoginScreenDestination
import com.micom.admissiontrackjce.destinations.TextInputsDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

private lateinit var auth: FirebaseAuth
fun sendData(    category: String,
                 name: String,
                 number: String,
                 enquirytype: String,
                 dept: String,
                 area: String,
                 pincode: String,
                 status: String) {

        val db = Firebase.firestore
        val et = Enquirydetail(category, name, number, enquirytype, dept, area,pincode, status)
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

            status = dropDownMenu(suggestions = listOf("Enquiry","Positive","May be Admitted","May be Converted","Rejected"), listName = "Status")

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                sendData(category,name,number,enquirytype,department,area,pincode,status)
                Toast.makeText(context,"Submitted Successfully",Toast.LENGTH_LONG).show()
                navigator.navigate(TextInputsDestination())
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
fun dropDownMenu(suggestions: List<String>, listName: String): String {



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
/*

@Composable
fun MenuBar(){
    TopAppBar() {

    }
}*/
