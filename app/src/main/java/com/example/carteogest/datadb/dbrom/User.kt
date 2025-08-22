/*package com.example.carteogest.datadb.dbrom

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.carteogest.datadb.dbrom.Role

@Entity(
    tableName = "usuarios",
    foreignKeys = [
        ForeignKey(
            entity = Role::class,
            parentColumns = ["id"],
            childColumns = ["roleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val login: String,
    val senhaHash: String,
    val email: String?,
    val roleId: Int,
    val ativo: Boolean = true,
    val roleName: String
)
*/