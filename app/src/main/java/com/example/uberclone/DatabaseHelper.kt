package com.example.uberclone

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Класс DatabaseHelper для работы с локальной базой данных SQLite
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    // Создание таблицы для хранения пользователей
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, PHONE TEXT, USERNAME TEXT, ROLE TEXT)")
    }

    // Обновление структуры базы данных
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Вставка данных о пользователе в базу
    fun insertUser(phone: String, username: String, role: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_2, phone)
        contentValues.put(COL_3, username)
        contentValues.put(COL_4, role)
        val result = db.insert(TABLE_NAME, null, contentValues)
        return result != -1L // Если результат -1, значит вставка не удалась
    }

    // Проверка, существует ли пользователь по номеру телефона
    fun checkUserByPhone(phone: String): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE PHONE = ?", arrayOf(phone))
        return cursor.count > 0
    }

    companion object {
        const val DATABASE_NAME = "UberClone.db"
        const val TABLE_NAME = "users"
        const val COL_1 = "ID"
        const val COL_2 = "PHONE"
        const val COL_3 = "USERNAME"
        const val COL_4 = "ROLE"
    }
}
