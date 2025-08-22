package com.example.carteogest.login

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.carteogest.login.permissoes.Permissao

@Entity(
    tableName = "usuario_permissoes",
    primaryKeys = ["usuarioId", "permissaoId"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["usuarioId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Permissao::class, parentColumns = ["id"], childColumns = ["permissaoId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class UsuarioPermissao(
    val usuarioId: Int,
    val permissaoId: Int
)