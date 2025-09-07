package com.example.gest.datadb.data_db.validity



import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*
import androidx.room.ForeignKey
import com.example.gest.datadb.data_db.products.Products





@Entity(
    tableName = "validity",
    foreignKeys = [ForeignKey(
        entity = Products::class,                  // Tabela "pai"
        parentColumns = ["uid"],                   // Coluna da tabela pai
        childColumns = ["produtoId"],             // Coluna nesta tabela
        onDelete = ForeignKey.CASCADE             // Se o produto for apagado, apaga as validades
    )],
    indices = [Index("produtoId")]                // Cria índice para consultas rápidas
)
data class ValidityAndFabrication(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,

    @ColumnInfo(name = "produtoId") val produtoId: Int,   // 🔗 referência ao Produto

    @ColumnInfo(name = "fabrication") val fabrication: String,

    @ColumnInfo(name = "validity") val validity: String
)