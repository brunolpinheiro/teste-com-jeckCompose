/*package com.example.carteogest.datadb.dbrom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.carteogest.datadb.dbrom.User


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM usuarios")
    suspend fun getUsers(): List<User>

    @Query("SELECT nome FROM roles WHERE id = :roleId")
    suspend fun getRoleName(roleId: Int): String?

    @Query("""
        SELECT u.* FROM usuarios u
        INNER JOIN roles r ON u.roleId = r.id
        WHERE u.login = :login AND u.senhaHash = :senhaHash AND u.ativo = 1
        LIMIT 1
    """)
    suspend fun autenticarUsuario(login: String, senhaHash: String): User?
}*/