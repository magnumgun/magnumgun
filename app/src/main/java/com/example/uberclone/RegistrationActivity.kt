package com.example.uberclone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        db = DatabaseHelper(this)

        val phone = intent.getStringExtra("phone") ?: ""
        val phoneNumber: EditText = findViewById(R.id.phone_number)
        phoneNumber.setText(phone)

        val username: EditText = findViewById(R.id.username)
        val roleGroup: RadioGroup = findViewById(R.id.role_group)
        val registerButton: Button = findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            val user = username.text.toString()
            val selectedRoleId = roleGroup.checkedRadioButtonId
            val role = if (selectedRoleId == R.id.driver_radio) "Водитель" else "Пассажир"

            if (user.isNotEmpty()) {
                val isInserted = db.insertUser(phone, user, role)
                if (isInserted) {
                    Toast.makeText(this, "Регистрация успешна как $role!", Toast.LENGTH_SHORT).show()
                    // Перенаправляем на главный экран после успешной регистрации
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("phone", phone)  // Передаём телефон для приветствия
                    intent.putExtra("role", role)  // Передаём роль
                    startActivity(intent)
                    finish()  // Закрываем экран регистрации
                } else {
                    Toast.makeText(this, "Ошибка при регистрации", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
