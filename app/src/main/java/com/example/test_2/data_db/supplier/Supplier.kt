package com.example.test_2.data_db.supplier



import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "supplier")
data class  Supplier(
    @PrimaryKey val uid: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "cnpj") val cnpj: String,
    @ColumnInfo(name = "adress") val adress: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "phone") val phone: String?
)