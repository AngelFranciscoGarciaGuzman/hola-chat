package com.example.hola_chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {
    // Create the Variables for the inputs.
    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth;


    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)



// Assign the inputs to the variables.
        emailInput = findViewById(R.id.user_email_input)
        passwordInput = findViewById(R.id.user_password_input)
        loginButton = findViewById(R.id.login_button)
        loginButton.setOnClickListener {
// Get the email and password from the inputs.
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
// Print the email and password.
            println("Email: $email")
            println("Password: $password")
            Log.i("Test Credentials", "Email: $email and Password:$password")
            signUp(email, password)
        }
    }
    // Function to validate email format
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
    // Function to show error message in a notification banner
    private fun showError(message: String) {
        val toast = Toast.makeText(this, message,
            Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }
    private fun reload() {
    }
    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    showError("Authentication Successfull.")
                    reload()
                } else {
                    showError("Authentication failed.")
                }
            }
    }
}