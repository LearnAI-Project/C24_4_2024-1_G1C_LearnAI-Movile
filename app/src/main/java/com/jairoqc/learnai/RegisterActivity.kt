package com.jairoqc.learnai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText
    private lateinit var etRepetirContrasena: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvIniciaSesion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etUsername = findViewById(R.id.etUsername)
        etCorreo = findViewById(R.id.etCorreo)
        etContrasena = findViewById(R.id.etContrasena)
        etRepetirContrasena = findViewById(R.id.etRepetirContrasena)
        btnRegister = findViewById(R.id.btnRegister)
        tvIniciaSesion = findViewById(R.id.tvIniciaSesion)


        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val contrasena = etContrasena.text.toString()
            val repetirContrasena = etRepetirContrasena.text.toString()


            if (username.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || repetirContrasena.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contrasena != repetirContrasena) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(username, correo, contrasena)
        }

        // Acción para "Inicia Sesión"
        tvIniciaSesion.setOnClickListener {
            // Navegar a LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser(username: String, correo: String, contrasena: String) {
        val client = OkHttpClient()

        // Crear el cuerpo de la solicitud en formato JSON
        val json = JSONObject().apply {
            put("username", username)
            put("email", correo)
            put("password", contrasena)
        }

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            json.toString()
        )

        // Crear la solicitud HTTP
        val request = Request.Builder()
            .url("http://159.203.186.177:8080/auth/register")
            .post(requestBody)
            .build()

        // Ejecutar la solicitud
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        // Navegar a LoginActivity o cerrar esta actividad
                        val intent = Intent(this@RegisterActivity, VerifyActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorMessage = try {
                            JSONObject(responseBody ?: "{}").getString("error")
                        } catch (e: Exception) {
                            "Error desconocido"
                        }
                        Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
