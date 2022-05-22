package com.micom.admissiontrackjce

import java.util.*

data class Enquirydetail(
    val category:String = " ",
    val name: String = " ",
    val number: String = "",
    val enquirytype: String = " ",
    val dept: String = " ",
    val area: String = " ",
    val pincode: String = " ",
    val status: String = " ",
    val date: Date? = null)


data class Count(
    var phoncallCount: Int = 0,
    var whatsappCount: Int = 0,
    var directCount: Int = 0,
    var emailCount: Int = 0,
    var chatbotcCount: Int = 0,
    var consultantCount: Int = 0,
    var totalCount: Int = 0
)
