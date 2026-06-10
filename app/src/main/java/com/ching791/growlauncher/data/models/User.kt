package com.ching791.growlauncher.data.models

data class User(
    val email: String,
    val displayName: String = email.substringBefore('@'),
    val role: String = "Member"
)
