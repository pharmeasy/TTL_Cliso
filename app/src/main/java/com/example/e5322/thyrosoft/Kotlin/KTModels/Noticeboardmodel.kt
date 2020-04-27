package com.example.e5322.thyrosoft.Kotlin.KTModels

data class Noticeboardmodel(
        val messages: List<Message>,
        val resId: String,
        val response: String
)

data class Message(
        val enterBy: String,
        val isAcknowledged: String,
        val messageCode: String,
        val noticeDate: String,
        val noticeMessage: String
)