package com.example.carteogest.datadb.data_db.supplier






import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.carteogest.datadb.data_db.products.Products
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

    @Query("SELECT * FROM Supplier WHERE uid = :id LIMIT 1")
    suspend fun getById(id: Int): Supplier
}