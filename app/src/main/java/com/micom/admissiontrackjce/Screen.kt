package com.micom.admissiontrackjce

sealed class Screen(val route:String){
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object EnquiryScreen : Screen("enquiry_screen")
    object ReportScreen : Screen("report_screen")
}