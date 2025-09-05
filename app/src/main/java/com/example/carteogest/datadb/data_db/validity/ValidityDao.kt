package com.example.carteogest.datadb.data_db.validity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface  ValidityDao{
    @Insert
    suspend fun addValidityToProduct(validity: ValidityAndFabrication)

    @Query("SELECT * FROM validity")
    fun getAll(): Flow<List<ValidityAndFabrication>>

    @Update
    suspend fun updateValidity(validity: ValidityAndFabrication)

    @Delete
    suspend fun deleteValidity(validity: ValidityAndFabrication)

    @Insert
    suspend fun inserirValidade(validade: ValidityAndFabrication)

    @Update
    suspend fun atualizarValidade(validade: ValidityAndFabrication)

    @Delete
    suspend fun excluirValidade(validade: ValidityAndFabrication)

    @Query("SELECT * FROM validity WHERE produtoId = :produtoId")
    suspend fun listarValidadesDoProduto(produtoId: Int): List<ValidityAndFabrication>
}