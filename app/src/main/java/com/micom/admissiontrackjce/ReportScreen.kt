package com.micom.admissiontrackjce

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.micom.admissiontrackjce.destinations.LoginScreenDestination
import com.micom.admissiontrackjce.destinations.ReportViewDestination
import com.micom.admissiontrackjce.ui.theme.Shapes
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

private lateinit var auth: FirebaseAuth
var enquirydetails = listOf<Enquirydetail>()
val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
var filteredenquirydeatils = listOf<Enquirydetail>()


fun getData(){

    val db = FirebaseFirestore.getInstance()
    GlobalScope.launch(Dispatchers.Default){
        db.collection("et").get().addOnSuccessListener { documentSnapshot ->
            enquirydetails = documentSnapshot.toObjects()
        }
    }
}

fun filterdate(month: String, day: Int, year: Int){

    getData()
    Log.d("DATE","$day $month $year")
    filteredenquirydeatils = enquirydetails.filter {
        it.date.toString().contains("$month $day") && it.date.toString().contains(year.toString())
    }
    for (item in filteredenquirydeatils) {
        Log.d("D", item.toString())
    }
    enquiryCount()
}


fun enquiryCount(): Count {

    val counts = Count(0,0,0,0,0,0,0)
    GlobalScope.launch (Dispatchers.IO){
            for(item in filteredenquirydeatils){
                when(item.enquirytype){
                    "Phone Call" -> counts.phoncallCount++
                    "Whatsapp" -> counts.whatsappCount++
                    "Direct" -> counts.directCount++
                    "Chatbot" -> counts.chatbotcCount++
                    "Email" -> counts.emailCount++
                    "Consultant" -> counts.consultantCount++
                }
                counts.totalCount++
            }
        }
    return counts
}



@com.ramcosta.composedestinations.annotation.Destination
@Composable
fun ReportView(navigator: DestinationsNavigator) {

    getData()
    auth = FirebaseAuth.getInstance()
    val count = enquiryCount()
    val context = LocalContext.current


        TopAppBar(
            title = {
                Text(text = "Report", color = Color.White)
            },
            actions = {
                IconButton(onClick = {
                    auth.signOut()
                    navigator.navigate(LoginScreenDestination())
                }) {
                    Icon(imageVector = Icons.Filled.Logout, "")
                }
            }
        )
        Column(
            modifier = Modifier
                .padding(vertical = 40.dp, horizontal = 15.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 20.dp),
                    text = "Sort By :"
                )
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
//
                showDatePicker()
//                val date = SimpleDateFormat("dd-MM-yyyy").parse(daqte)
//                Log.d("TAG","$date")

            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ){

                ExpandCard(title = "Phone Call", etcount = count.phoncallCount)
                ExpandCard(title = "Whatsapp", etcount = count.whatsappCount)
                ExpandCard(title = "Direct", etcount = count.directCount)
                ExpandCard(title = "Chatbot", etcount = count.chatbotcCount)
                ExpandCard(title = "Consultant", etcount = count.consultantCount)
                ExpandCard(title = "Email", etcount = count.emailCount)

                Button(
                    onClick = {
                        navigator.navigate(ReportViewDestination())
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text(text = "Refresh")
                }
            }
        }
    }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandCard(
    title: String,
    etcount: Int,
    descriptionFontSize: TextUnit = MaterialTheme.typography.subtitle1.fontSize,
    descriptionFontWeight: FontWeight = FontWeight.Normal,
    ) {
    var expandedState by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(5.dp),
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$title",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(8.dp),
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "$etcount",
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(8.dp),
                )
                Text(text = "5", textAlign = TextAlign.End, modifier = Modifier.padding(8.dp))
            }
            if (expandedState) {
                for (item in filteredenquirydeatils) {
                    if (item.enquirytype == title ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = item.name,
                                fontSize = descriptionFontSize,
                                fontWeight = descriptionFontWeight,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = item.number,
                                fontSize = descriptionFontSize,
                                fontWeight = descriptionFontWeight,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun showDatePicker() {

    val mContext = LocalContext.current
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDate = remember { mutableStateOf("") }

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth}/$mYear"
            filterdate(months[mMonth],mDayOfMonth,mYear)
        }, mYear, mMonth, mDay
    )

    Column {
            Button(onClick = { mDatePickerDialog.show() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58)) ) {

            Text(text = "Open Date Picker", color = Color.White)
            Text(text = mDate.value, color = Color.White)

        }
    }

}
/*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableCard(
    titleFontSize: TextUnit = MaterialTheme.typography.h6.fontSize,
    titleFontWeight: FontWeight = FontWeight.Bold,
    descriptionFontSize: TextUnit = MaterialTheme.typography.subtitle1.fontSize,
    descriptionFontWeight: FontWeight = FontWeight.Normal,
    descriptionMaxLines: Int = 4,
    shape: Shape = Shapes.medium,
    padding: Dp = 12.dp
) {
    var expandedState by remember { mutableStateOf(false) }
    var expandedStatewhatsapp by remember { mutableStateOf(false) }
    var expandedStatedirect by remember { mutableStateOf(false) }

    getData()
    var count = enquiryCount()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = shape,
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Phone Call", textAlign = TextAlign.Start,modifier = Modifier.padding(8.dp),overflow = TextOverflow.Ellipsis)
                Text(text = "${count.phoncallCount}", textAlign = TextAlign.End,modifier = Modifier.padding(8.dp))
                Text(text = "5", textAlign = TextAlign.End,modifier = Modifier.padding(8.dp))
            }
            if (expandedState) {
                for (item in enquirydetails ){
                    if(item.enquirytype == "Phone Call"){
                        Row(modifier = Modifier.fillMaxWidth()){
                            Text(
                                text = item.name,
                                fontSize = descriptionFontSize,
                                fontWeight = descriptionFontWeight,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = item.number,
                                fontSize = descriptionFontSize,
                                fontWeight = descriptionFontWeight,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = shape,
        onClick = {
            expandedStatewhatsapp = !expandedStatewhatsapp
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Whatsapp", textAlign = TextAlign.Start,modifier = Modifier.padding(8.dp),overflow = TextOverflow.Ellipsis)
                Text(text = "${count.whatsappCount}", textAlign = TextAlign.End,modifier = Modifier.padding(8.dp),overflow = TextOverflow.Ellipsis)
            }
            if (expandedStatewhatsapp) {
                for (item in enquirydetails){
                    if(item.enquirytype == "Whatsapp"){
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "${item.name}",
                                fontSize = descriptionFontSize,
                                fontWeight = descriptionFontWeight,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.width(50.dp))
                            Text(
                                text = item.number,
                                fontSize = descriptionFontSize,
                                fontWeight = descriptionFontWeight,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = shape,
        onClick = {
             expandedStatedirect = !expandedStatedirect
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Direct", textAlign = TextAlign.Start,modifier = Modifier.padding(8.dp),overflow = TextOverflow.Ellipsis)
                Text(text = "${count.directCount}", textAlign = TextAlign.End,modifier = Modifier.padding(8.dp),overflow = TextOverflow.Ellipsis)
            }
            if (expandedStatedirect) {
                for (item in enquirydetails){
                    if(item.enquirytype == "Direct"){
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "${item.name}",
                                fontSize = descriptionFontSize,
                                fontWeight = descriptionFontWeight,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = item.number,
                                fontSize = descriptionFontSize,
                                fontWeight = descriptionFontWeight,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }


}*/
