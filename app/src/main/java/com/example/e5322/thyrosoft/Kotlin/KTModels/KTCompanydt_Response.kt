package com.example.e5322.thyrosoft.Kotlin.KTModels

data class KTCompanydt_Response(
    val Contact_Array_list: List<ContactArray>,
    val response: String
) {
    data class ContactArray(
        val CATEGORY: String,
        val DESIGNATION: String,
        val ECODE: String,
        val EMAIL_ID: String,
        val IMG: Any,
        val MOBILE: Any,
        val NAME: String,
        val ROLE: String
    )
}