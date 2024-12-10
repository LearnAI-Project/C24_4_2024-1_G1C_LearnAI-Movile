package com.jairoqc.learnai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jairoqc.learnai.http.RetrofitClient
import com.jairoqc.learnai.http.models.ForgotPasswordRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ForgetActivity : AppCompatActivity() {

    private lateinit var btnReturn: Button
    private lateinit var btnSubmit: Button
    private lateinit var etEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)


        btnReturn = findViewById(R.id.btnReturn)
        btnSubmit = findViewById(R.id.btnSubmit)
        etEmail = findViewById(R.id.etEmail)


        btnReturn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSubmit.setOnClickListener {
            val email = etEmail.text.toString()

            if (email.isNotEmpty()) {
                sendForgotPasswordRequest(email)
            } else {
                Toast.makeText(this, "Por favor ingresa un correo electrónico", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendForgotPasswordRequest(email: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val request = ForgotPasswordRequest(email)

                val response: Response<Void> = RetrofitClient.instance.forgotPassword(request)

                if (response.isSuccessful) {
                    Toast.makeText(this@ForgetActivity, "Correo enviado correctamente", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@ForgetActivity, RecoveryActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@ForgetActivity, "Error al enviar el correo", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {

                Toast.makeText(this@ForgetActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

