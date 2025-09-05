package com.example.carteogest.datadb.data_db

import androidx.room.Embedded
import androidx.room.Relation
import com.example.carteogest.datadb.data_db.products.Products
import com.example.carteogest.datadb.data_db.products.ProdutsDao
import com.example.carteogest.datadb.data_db.supplier.Supplier
import com.example.carteogest.datadb.data_db.validity.ValidityAndFabrication
import com.example.carteogest.datadb.data_db.validity.ValidityDao


data class ProductWithValidities(
    @Embedded val product: Products,

    @Relation(
        parentColumn = "uid",
        entityColumn = "produtoId"
    )
    val validades: List<ValidityAndFabrication>
)

data class ProdutoComFornecedor(
    @Embedded val produto: Products,
    @Relation(
        parentColumn = "supplierId",
        entityColumn = "id"
    )
    val Supplier: Supplier?
)

