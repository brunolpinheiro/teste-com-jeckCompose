package com.example.carteogest.datadb.dbrom

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "roles")
data class Role(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String
)