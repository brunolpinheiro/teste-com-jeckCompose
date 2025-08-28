package com.example.test_2.data_db.validity



import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(tableName = "validity")
data class ValidityAndFabrication(
    @PrimaryKey (autoGenerate = true) val productId:Int = 0,
    @ColumnInfo(name = "fabrication") val fabrication: String?,
    @ColumnInfo(name = "validity") val validity:String?
)