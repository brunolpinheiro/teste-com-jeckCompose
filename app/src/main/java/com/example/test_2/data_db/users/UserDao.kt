package com.example.test_2.data_db.users



// UserDao.kt



import androidx.room.*
import com.example.test_2.data_db.supplier.Supplier

import com.example.test_2.data_db.users.User


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

    @Query("SELECT * FROM usuarios WHERE uid = :uid LIMIT 1")
    suspend fun getById(uid: Int): User

    @Query("SELECT EXISTS(SELECT 1 FROM usuarios WHERE LOWER(TRIM(nome)) = LOWER(TRIM(:nome)) LIMIT 1)")
    suspend fun findByName(nome: String): Boolean



}