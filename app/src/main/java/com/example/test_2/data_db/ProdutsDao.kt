package com.example.test_2.data_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutsDao {
    @Insert
    suspend fun insertProduct(product: Products)

    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<Products>>
    @Query("SELECT * FROM Products WHERE sector = :sector")
    fun getProductsBySector(sector: String): Flow<List<Products>>

    @Query("SELECT EXISTS(SELECT 1 FROM products WHERE name = :name LIMIT 1)")
    fun findByName(name: String): Boolean

    @Update
    suspend fun updateProduct(product: Products)

    @Delete
    suspend fun deleteProduct(product: Products)
}