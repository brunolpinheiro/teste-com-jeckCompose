package com.example.test_2.data_db.users




// User.kt




import androidx.room.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.room.ColumnInfo

@Entity(
    tableName = "usuarios",)
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "nome")val nome: String,
    @ColumnInfo(name = "senha")val senha: String,
    @ColumnInfo(name = "ativo")val ativo: Boolean = true,
    @ColumnInfo(name = "createdAt")val createdAt: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

)
