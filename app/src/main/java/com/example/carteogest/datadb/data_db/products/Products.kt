package com.example.carteogest.datadb.data_db.products


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "products")
data class Products(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0, // <- auto incrementa
    @ColumnInfo(name = "Nome") val name: String, // Mapeado como 'description'
    @ColumnInfo(name = "refencia") val skuCode: String,
    @ColumnInfo(name = "Setor") val sector: String, // Mapeado como 'category'
    @ColumnInfo(name = "Preço") val price: Float,
    @ColumnInfo(name = "Preço Promocional") val promotionalPrice: Float?,
    @ColumnInfo(name = "Quantidade") val quantity: Int,
    @ColumnInfo(name = "Marca") val brand: String,
    @ColumnInfo(name = "Fornecedor") val supplier: String?,
    @ColumnInfo(name = "status") val status: Boolean,
    @ColumnInfo(name = "barcode") val barcode: String?,
    @ColumnInfo(name = "cost") val cost: Float?,
    @ColumnInfo(name = "unit_of_measure") val unitOfMeasure: String?,
    val createdAt: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

)