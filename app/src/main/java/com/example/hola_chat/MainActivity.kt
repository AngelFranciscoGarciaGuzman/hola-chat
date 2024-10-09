package com.example.hola_chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    // Crear variables para las entradas de usuario.
    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var loginButton: Button
    lateinit var signUpButton: Button
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Asignar las vistas a las variables
        emailInput = findViewById(R.id.user_email_input)
        passwordInput = findViewById(R.id.user_password_input)
        loginButton = findViewById(R.id.login_button)
        signUpButton = findViewById(R.id.sign_up_button)

        // Lógica para iniciar sesión
        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Verificar si los campos no están vacíos
            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(email, password)
            } else {
                showError("Por favor, ingrese su correo y contraseña")
            }
        }

        // Lógica para registrarse
        signUpButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signUp(email, password)
            } else {
                showError("Por favor, ingrese su correo y contraseña")
            }
        }
    }

    private fun reload() {
        // Recargar la vista o realizar acciones cuando el usuario ya está autenticado
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Login", "signInWithEmail:success")
                    showError("Inicio de sesión exitoso")

                    // Redirige a HomeActivity después de un inicio de sesión exitoso
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // Finaliza la actividad actual para evitar volver a ella con el botón de retroceso
                } else {
                    Log.w("Login", "signInWithEmail:failure", task.exception)
                    showError("Error al iniciar sesión")
                }
            }
    }


    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    showError("Registro exitoso")
                    reload()
                } else {
                    showError("Error en el registro")
                }
            }
    }

    // Función para mostrar un mensaje de error
    private fun showError(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }
}
