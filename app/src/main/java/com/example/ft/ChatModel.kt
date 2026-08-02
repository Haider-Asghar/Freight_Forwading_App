package com.example.ft

data class ChatModel(
    val refNo: String,
    val userName: String,
    val lastMessage: String,
    val time: Long,
    val unreadCount: Int
)
