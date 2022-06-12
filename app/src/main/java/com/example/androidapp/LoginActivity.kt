package com.example.androidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText

class LoginActivity : AppCompatActivity() {

    private lateinit var edit_email: EditText
    private lateinit var edit_password: EditText
    private lateinit var checkbox_keep: CheckBox
    private lateinit var button_signin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        edit_email = findViewById(R.id.login_email)
        edit_password = findViewById(R.id.login_password)
        checkbox_keep = findViewById(R.id.login_keep)
        button_signin = findViewById(R.id.login_button_signin)
    }
}