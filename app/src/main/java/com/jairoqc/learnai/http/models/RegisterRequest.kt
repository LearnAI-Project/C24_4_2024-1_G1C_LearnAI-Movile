package com.jairoqc.learnai.http.models

data class RegisterRequest(
    val username: String,
    val correo: String,
    val contrasena: String
)