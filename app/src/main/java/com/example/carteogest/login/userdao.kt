// UserDao.kt
package com.example.carteogest.login


import androidx.room.*

import com.example.carteogest.login.User


@Dao
interface UserDao {

    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM usuarios")
    suspend fun getUsers(): List<User>

    @Query("SELECT * FROM usuarios WHERE nome = :nome AND senha = :senha LIMIT 1")
    suspend fun autenticarUsuario(nome: String, senha: String) : User?

}

