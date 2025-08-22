// User.kt
package com.example.carteogest.login



import androidx.room.*

@Entity(
    tableName = "usuarios",)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val senha: String,
    val ativo: Boolean = true

)
