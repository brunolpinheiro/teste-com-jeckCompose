package com.example.carteogest.login.permissoes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PermissaoDao {
    @Insert
    suspend fun addPermissao(permissao: Permissao): Long

    @Query("SELECT * FROM permissoes")
    suspend fun getAllPermissoes(): List<Permissao>
}