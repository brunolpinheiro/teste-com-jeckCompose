package com.example.test_2.data_db.validity



import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(tableName = "validity")
data class ValidityAndFabrication(
    @PrimaryKey (autoGenerate = true) val uid:Int = 0,
    @ColumnInfo(name = "nameOfProduct") val nameOfProduct:String,
    @ColumnInfo(name = "fabrication") val fabrication: String ,
    @ColumnInfo(name = "validity") val validity:String
)