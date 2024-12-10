package com.jairoqc.learnai.http.models

data class LoginResponse(
    val id: Int,
    val username: String,
    val email: String,
    val token: String
)