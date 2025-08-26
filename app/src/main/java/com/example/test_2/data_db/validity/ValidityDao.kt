package com.example.test_2.data_db.validity



import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.test_2.data_db.products.Products
import com.example.test_2.data_db.validity.ValidityAndFabrication
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface  ValidityDao{
    @Insert
    suspend fun addValidityToProduct(validity: ValidityAndFabrication)



    @Query("SELECT * FROM validity")
    fun getAll(): Flow<List<ValidityAndFabrication>>

    @Query("SELECT * FROM validity WHERE nameOfProduct = :nameProduct")
    fun getByProductName(nameProduct: String): Boolean

    @Update
    suspend fun updateValidity(validity: ValidityAndFabrication)

    @Delete
    suspend fun deleteValidity(validity: ValidityAndFabrication)
}
