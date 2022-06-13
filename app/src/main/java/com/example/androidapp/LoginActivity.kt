package com.example.androidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var edit_email: EditText
    private lateinit var edit_password: EditText
    private lateinit var checkbox_keep: CheckBox
    private lateinit var button_signin: Button
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        edit_email = findViewById(R.id.login_email)
        edit_password = findViewById(R.id.login_password)
        checkbox_keep = findViewById(R.id.login_keep)
        button_signin = findViewById(R.id.login_button_signin)
        setUp()
    }

    private fun setUp() {
        button_signin.setOnClickListener {
            if(edit_email.text.isNotEmpty() && edit_password.text.isNotEmpty()){
                val email = edit_email.text
                val password = edit_password.text
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener{
                        if(it.isSuccessful) {
                            this.email = it.result?.user?.email.toString()
                            showHome(it.result?.user?.email?: "", ProviderType.BASIC)
                        }
                        else {
                            showAlert()
                        }
                    }

            }
        }
    }

    private fun showAlert() {
        val builer = AlertDialog.Builder(this)
        builer.setTitle("Error")
        builer.setMessage("Se ha producido un error de autenticando al usuario")
        builer.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builer.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

    public override fun onResume() {
        super.onResume()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            showHome(FirebaseAuth.getInstance().currentUser?.email.toString(), ProviderType.BASIC)
        } else {
            setUp()
        }
    }
}