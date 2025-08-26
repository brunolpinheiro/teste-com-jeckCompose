package com.example.test_2.data_db.products

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Products(
    @PrimaryKey val uid: Int = 0,
    @ColumnInfo(name = "name") val name: String, // Mapeado como 'description'
    @ColumnInfo(name = "sector") val sector: String, // Mapeado como 'category'
 // Mapeado como 'expirationDate'
    @ColumnInfo(name = "sku_code") val skuCode: String,
    @ColumnInfo(name = "price") val price: Float,
    @ColumnInfo(name = "promotional_price") val promotionalPrice: Float?,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "unit_of_measure") val unitOfMeasure: String?,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "barcode") val barcode: String?,
    @ColumnInfo(name = "height") val height: Float?,
    @ColumnInfo(name = "width") val width: Float?,
    @ColumnInfo(name = "length") val length: Float?,
    @ColumnInfo(name = "weight") val weight: Float?,
    @ColumnInfo(name = "color") val color: String?,
    @ColumnInfo(name = "size") val size: String?,
    @ColumnInfo(name = "cost") val cost: Float?,
    @ColumnInfo(name = "tags") val tags: String?,
    @ColumnInfo(name = "supplier") val supplier: String?,
)