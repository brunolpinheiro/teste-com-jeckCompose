package com.example.carteogest.datadb.dbrom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRole(role: Role)

    @Query("SELECT * FROM roles")
    suspend fun getRoles(): List<Role>
}