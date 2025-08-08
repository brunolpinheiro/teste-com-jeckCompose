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

    @Query("SELECT * FROM Products WHERE sector = :sector")
    fun getProductsBySector(sector: String): Flow<List<Products>>

    @Update
    suspend fun updateProduct(product: Products)

    @Delete
    suspend fun deleteProduct(product: Products)
}