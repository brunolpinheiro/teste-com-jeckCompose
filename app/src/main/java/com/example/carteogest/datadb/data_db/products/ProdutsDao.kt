package com.example.carteogest.datadb.data_db.products


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.carteogest.datadb.data_db.ProductWithValidities
import com.example.carteogest.datadb.data_db.ProdutoComFornecedor
import kotlinx.coroutines.flow.Flow


@Dao
interface ProdutsDao {


    @Insert
    suspend fun insertProduct(product: Products)


    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<Products>>
    @Query("SELECT * FROM Products WHERE setor = :sector")
    fun getProductsBySector(sector: String): Flow<List<Products>>

    @Query("SELECT EXISTS(SELECT 1 FROM products WHERE LOWER(TRIM(nome)) = LOWER(TRIM(:name)) LIMIT 1)")
    suspend fun findByName(name: String): Boolean

    @Update
    suspend fun updateProduct(product: Products)

    @Delete
    suspend fun deleteProduct(product: Products)

    @Query("SELECT DISTINCT Setor FROM Products")
    fun getSetor(): Flow<List<String>>
    @Query("SELECT * FROM products WHERE uid = :id LIMIT 1")
    suspend fun getById(id: Int): Products

    @Transaction
    @Query("SELECT * FROM products") // sua tabela de produtos
    fun getProdutosComValidades(): Flow<List<ProductWithValidities>>

    //fornecedores
    @Transaction
    @Query("SELECT * FROM products WHERE Fornecedor = :supplierId")
    suspend fun getProductsBySupplier(supplierId: Long): List<Products>

}