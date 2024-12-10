package com.jairoqc.learnai

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jairoqc.learnai.http.RetrofitClient
import com.jairoqc.learnai.http.models.PresentationRequest
import com.jairoqc.learnai.http.models.PresentationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val etQuery = findViewById<EditText>(R.id.etQuery)
        val btnGenerate = findViewById<Button>(R.id.btnGenerate)

        btnGenerate.setOnClickListener {
            val query = etQuery.text.toString()

            if (query.isEmpty()) {
                Toast.makeText(this, "El tema no puede estar vacío", Toast.LENGTH_SHORT).show()
            } else {
                sendRequest(query)
            }
        }
    }

    private fun sendRequest(topic: String) {
        // Recuperar el token desde SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val apiService = RetrofitClient.instance
        val request = PresentationRequest(topic)

        apiService.getPresentation("Bearer $token", request)
            .enqueue(object : Callback<PresentationResponse> {
                override fun onResponse(
                    call: Call<PresentationResponse>,
                    response: Response<PresentationResponse>
                ) {
                    if (response.isSuccessful) {
                        val presentationResponse = response.body()
                        presentationResponse?.let {
                            Log.d("CHAT_ACTIVITY", "URL de la presentación: ${it.url}")
                            Toast.makeText(
                                this@ChatActivity,
                                "URL recibida: ${it.url}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Log.e("CHAT_ACTIVITY", "Error del servidor: ${response.errorBody()?.string()}")
                        Toast.makeText(
                            this@ChatActivity,
                            "Error en la solicitud: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<PresentationResponse>, t: Throwable) {
                    Log.e("CHAT_ACTIVITY", "Error de red: ${t.message}")
                    Toast.makeText(
                        this@ChatActivity,
                        "Error de red: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}
