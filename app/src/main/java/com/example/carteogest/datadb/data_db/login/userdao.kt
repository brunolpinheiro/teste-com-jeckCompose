// UserDao.kt
package com.example.carteogest.datadb.data_db.login


import androidx.room.*


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

        @Query("SELECT * FROM usuarios WHERE nome = :nome LIMIT 1")
        suspend fun findUserByName(nome: String): User?


    @Query("SELECT permissao FROM usuarios WHERE nome = :nome LIMIT 1")
    suspend fun getPermissaoByName(nome: String): String?

}

