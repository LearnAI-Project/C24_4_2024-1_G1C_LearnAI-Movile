package com.jairoqc.learnai.http

import com.jairoqc.learnai.http.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("auth/register")
    fun registerUser(@Body request: RegisterRequest): Call<Void>

    @PUT("auth/verify")
    fun verifyCode(@Body verifyRequest: VerifyRequest): Call<Void>

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<Void>

    @PUT("auth/recover-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<Void>

    @POST("api-ai/get_presentation/")
    fun getPresentation(
        @Header("Authorization") token: String,
        @Body presentationRequest: PresentationRequest
    ): Call<PresentationResponse>
}
