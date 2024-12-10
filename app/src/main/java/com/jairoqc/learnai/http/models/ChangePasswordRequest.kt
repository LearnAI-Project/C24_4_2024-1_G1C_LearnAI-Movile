package com.jairoqc.learnai.http.models

data class ChangePasswordRequest(
    val code: String,
    val newPassword: String
)