package com.example.carteogest.login.permissoes

import androidx.room.*

@Entity(
    tableName = "permissoes",)
data class Permissao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val permissao: String

)