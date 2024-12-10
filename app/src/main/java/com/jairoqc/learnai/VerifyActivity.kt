package com.jairoqc.learnai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jairoqc.learnai.http.RetrofitClient
import com.jairoqc.learnai.http.models.VerifyRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)


        val etVerificationCode: EditText = findViewById(R.id.etVerificationCode)
        val btnSubmitCode: Button = findViewById(R.id.btnSubmitCode)


        btnSubmitCode.setOnClickListener {
            val verificationCode = etVerificationCode.text.toString().trim()

            if (verificationCode.isNotEmpty()) {
                verifyCode(verificationCode)
            } else {
                Toast.makeText(this, "Por favor, ingresa el código de verificación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyCode(code: String) {

        val apiService = RetrofitClient.instance


        val verifyRequest = VerifyRequest(code)


        apiService.verifyCode(verifyRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {

                    Toast.makeText(this@VerifyActivity, "Verificación exitosa", Toast.LENGTH_LONG).show()


                    val intent = Intent(this@VerifyActivity, LoginActivity::class.java)
                    startActivity(intent)


                    finish()
                } else {
                    Toast.makeText(this@VerifyActivity, "Error en la verificación", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@VerifyActivity, "Error de conexión: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
