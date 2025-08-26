package com.example.test_2.data_db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.test_2.data_db.products.Products
import com.example.test_2.data_db.products.ProdutsDao
import com.example.test_2.data_db.supplier.Supplier
import com.example.test_2.data_db.supplier.SupplierDao
import com.example.test_2.data_db.validity.ValidityAndFabrication
import com.example.test_2.data_db.validity.ValidityDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Products::class, Supplier::class, ValidityAndFabrication::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productsDao(): ProdutsDao
    abstract fun supplierDao(): SupplierDao
    abstract fun validityDao(): ValidityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase? {
            return INSTANCE ?: try {
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                scope.launch(Dispatchers.IO) {
                                    try {
                                        val productsDao = INSTANCE?.productsDao()
                                        val supplierDao = INSTANCE?.supplierDao()
                                        val validityDao = INSTANCE?.validityDao()
                                        // Insira dados iniciais aqui, se necessário
                                    } catch (e: Exception) {
                                        Log.e("AppDatabase", "Erro ao inserir dados iniciais: ${e.message}")
                                    }
                                }
                            }
                        })
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .build()
                    INSTANCE = instance
                    instance
                }
            } catch (e: Exception) {
                Log.e("AppDatabase", "Falha ao criar o banco de dados: ${e.message}")
                null
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Criação da tabela Products com a estrutura alterada
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS Products (
                        uid INTEGER PRIMARY KEY NOT NULL,
                        name TEXT NOT NULL,
                        sector TEXT NOT NULL,
                        sku_code TEXT NOT NULL,
                        price REAL NOT NULL,
                        promotional_price REAL,
                        quantity INTEGER NOT NULL,
                        unit_of_measure TEXT,
                        brand TEXT NOT NULL,
                        status TEXT,
                        barcode TEXT,
                        height REAL,
                        width REAL,
                        length REAL,
                        weight REAL,
                        color TEXT,
                        size TEXT,
                        cost REAL,
                        tags TEXT,
                        supplier TEXT
                    )
                """.trimIndent())
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Criação da tabela Supplier com estrutura similar (adaptada aos campos conhecidos)
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS Supplier (
                        uid INTEGER PRIMARY KEY NOT NULL,
                        name TEXT NOT NULL,
                        cnpj TEXT NOT NULL,
                        adress TEXT,
                        email TEXT,
                        phone TEXT
                    )
                """.trimIndent())

                // Criação da tabela ValidityAndFabrication com estrutura similar (adaptada aos campos conhecidos)
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS ValidityAndFabrication (
                        uid INTEGER PRIMARY KEY NOT NULL,
                        nameOfProduct TEXT NOT NULL,
                        fabrication TEXT NOT NULL,
                        validity TEXT NOT NULL
                    )
                """.trimIndent())
            }
        }

        fun resetDatabase(context: Context, scope: CoroutineScope) {
            try {
                context.deleteDatabase("app_database")
                INSTANCE = null
                getDatabase(context, scope) // Recria o banco
            } catch (e: Exception) {
                Log.e("AppDatabase", "Erro ao resetar o banco de dados: ${e.message}")
            }
        }
    }
}