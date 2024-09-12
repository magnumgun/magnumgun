package com.example.uberclone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = DatabaseHelper(this)

        val phoneNumber: EditText = findViewById(R.id.phone_number)
        val loginButton: Button = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val phone = phoneNumber.text.toString()

            if (phone.isNotEmpty()) {
                val userExists = db.checkUserByPhone(phone)
                if (userExists) {
                    Toast.makeText(this, "Добро пожаловать!", Toast.LENGTH_SHORT).show()
                    // Перенаправляем на главный экран после успешной авторизации
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("phone", phone)  // Передаём телефон для приветствия
                    startActivity(intent)
                    finish()  // Закрываем экран авторизации
                } else {
                    Toast.makeText(this, "Пользователь не найден. Пожалуйста, зарегистрируйтесь.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, RegistrationActivity::class.java)
                    intent.putExtra("phone", phone)  // Передаём телефон для регистрации
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
