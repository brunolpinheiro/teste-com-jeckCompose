package com.example.test_2.data_db.supplier






import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao {
    @Insert
    suspend fun insertSuppllier(supplier: Supplier)



    @Query("SELECT * FROM supplier")
    fun getAll(): Flow<List<Supplier>>
    @Query("SELECT * FROM Supplier WHERE cnpj = :cnpj")
    fun getSupplierByCnpj(cnpj: String): Flow<List<Supplier>>

    @Query("SELECT EXISTS(SELECT 1 FROM supplier WHERE LOWER(TRIM(name)) = LOWER(TRIM(:name)) LIMIT 1)")
    suspend fun findByName(name: String): Boolean

    @Update
    suspend fun updateSupplier(supplier: Supplier)

    @Delete
    suspend fun deleteSupplier(supplier: Supplier)
}