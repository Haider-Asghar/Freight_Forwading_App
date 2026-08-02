package com.example.ft

data class MessageModel(
    val sender: String,
    val message: String,
    val time: Long,
    val isDate: Boolean = false,
    val type: String = "text", // text, image, file
    val fileUrl: String = ""
)
