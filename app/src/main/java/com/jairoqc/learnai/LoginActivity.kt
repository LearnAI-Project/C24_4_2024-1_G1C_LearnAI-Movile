package com.jairoqc.learnai

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jairoqc.learnai.http.RetrofitClient
import com.jairoqc.learnai.http.models.LoginRequest
import com.jairoqc.learnai.http.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    // Variables de UI
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var tvForgotPassword: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referencias a los elementos de la UI
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        tvForgotPassword=findViewById(R.id.tvForgotPassword)
        // Acción al presionar el botón de login
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Toast.makeText(this, "Por favor, ingresa el username y la contraseña", Toast.LENGTH_SHORT).show()
            }
        }
        tvRegister.setOnClickListener {
            // Navegar a LoginActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        tvForgotPassword.setOnClickListener {
            // Navegar a LoginActivity
            val intent = Intent(this, ForgetActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Función para realizar el login
    private fun loginUser(username: String, password: String) {
        // Crear el objeto LoginRequest
        val loginRequest = LoginRequest(username, password)

        // Llamada al método login del ApiService
        RetrofitClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    // Almacenar el token en SharedPreferences
                    val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("auth_token", loginResponse?.token)
                    editor.apply()

                    Toast.makeText(this@LoginActivity, "Login exitoso: ${loginResponse?.username}", Toast.LENGTH_LONG).show()

                    // Redirigir a la siguiente actividad (HomeActivity o Dashboard)
                    val intent = Intent(this@LoginActivity, ChatActivity::class.java)
                    startActivity(intent)

                    // Finalizar la actividad actual para evitar regresar a la pantalla de login
                    finish()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(this@LoginActivity, "Error en el login: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error de conexión: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

