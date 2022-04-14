package com.micom.admissiontrackjce

sealed class Screen(val route:String){
    object LoginScreen : Screen("login_screen")
    object EnquiryScreen : Screen("enquiry_screen")
}