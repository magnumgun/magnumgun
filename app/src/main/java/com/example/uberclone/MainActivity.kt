package com.example.uberclone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val welcomeText: TextView = findViewById(R.id.welcome_text)

        // Получаем данные, переданные через Intent
        val role = intent.getStringExtra("role")

        // Приветствие для пользователя в зависимости от роли
        if (role != null) {
            welcomeText.text = "Добро пожаловать, $role!"
        } else {
            welcomeText.text = "Добро пожаловать!"
        }

        // Находим кнопку и добавляем логику для открытия карты
        val mapButton: Button = findViewById(R.id.map_button)
        mapButton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent) // Переход к MapActivity
        }
    }
}
