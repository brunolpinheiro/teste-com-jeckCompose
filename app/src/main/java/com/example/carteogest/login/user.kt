// User.kt
package com.example.carteogest.login



import androidx.room.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(
    tableName = "usuarios",)
data class User(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val nome: String,
    val senha: String,
    val ativo: Boolean = true,
    val createdAt: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

)
