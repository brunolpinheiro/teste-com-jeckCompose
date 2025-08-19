package com.example.test_2.data_db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.test_2.data_db.products.Products
import com.example.test_2.data_db.supplier.Supplier
import com.example.test_2.data_db.supplier.SupplierDao
import com.example.test_2.data_db.products.ProdutsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Products::class, Supplier::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productsDao(): ProdutsDao  // Corrigido o erro de digitação
    abstract fun supplierDao(): SupplierDao

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
                                        // Insira dados iniciais aqui, se necessário
                                    } catch (e: Exception) {
                                        Log.e("AppDatabase", "Erro ao inserir dados iniciais: ${e.message}")
                                    }
                                }
                            }
                        })
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)  // Corrigido para MIGRATION_2_3
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
                database.execSQL("ALTER TABLE Products ADD COLUMN fabrication TEXT")
                database.execSQL("ALTER TABLE Products ADD COLUMN validity TEXT")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {  // Corrigido de 2_4 para 2_3
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS supplier (
                        uid INTEGER PRIMARY KEY NOT NULL,
                        name TEXT NOT NULL,
                        cnpj TEXT NOT NULL,
                        adress TEXT,
                        email TEXT,
                        phone TEXT
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