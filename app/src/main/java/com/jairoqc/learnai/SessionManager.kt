package com.jairoqc.learnai


import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "user_session"
    private const val TOKEN_KEY = "auth_token"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    // Guarda el token
    fun saveToken(token: String) {
        editor.putString(TOKEN_KEY, token)
        editor.apply()  // Usar apply en lugar de commit
    }

    // Obtiene el token
    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    // Limpia el token
    fun clearToken() {
        editor.remove(TOKEN_KEY)
        editor.apply()
    }
}
