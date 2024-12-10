package com.jairoqc.learnai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jairoqc.learnai.http.RetrofitClient
import com.jairoqc.learnai.http.models.ChangePasswordRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class RecoveryActivity : AppCompatActivity() {

    private lateinit var btnReturn: Button
    private lateinit var etVerificationCode: EditText
    private lateinit var etNewPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery)

        btnReturn = findViewById(R.id.btnReturn)
        etVerificationCode = findViewById(R.id.etVerificationCode)
        etNewPassword = findViewById(R.id.etNewPassword)


        btnReturn.setOnClickListener {
            val intent = Intent(this, ForgetActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.btnRecover).setOnClickListener {
            val code = etVerificationCode.text.toString()
            val newPassword = etNewPassword.text.toString()


            if (code.isNotEmpty() && newPassword.isNotEmpty()) {
                sendPasswordChangeRequest(code, newPassword)
            } else {
                Toast.makeText(this, "Por favor ingresa el código y la nueva contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendPasswordChangeRequest(code: String, newPassword: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {

                val request = ChangePasswordRequest(code, newPassword)

                val response: Response<Void> = RetrofitClient.instance.changePassword(request)


                if (response.isSuccessful) {
                    Toast.makeText(this@RecoveryActivity, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@RecoveryActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@RecoveryActivity, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RecoveryActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
