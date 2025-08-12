package com.example.test_2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.test_2.data_db.Products
import com.example.test_2.data_db.ProdutsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log

@Database(entities = [Products::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProdutsDao

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
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                scope.launch(Dispatchers.IO) {
                                    try {
                                        val dao = INSTANCE?.productDao()
                                        dao?.insertProduct(Products(name = "Salmão", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Tilapia", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Prego", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Atum", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Povo", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Nigiri", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Couve", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Doritos", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Batata", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Alho Poro", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Wasabi", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Gengibre", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Cebola cebolinha", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Morango", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Castinha", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Goiabada", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Abacaxi", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Nori", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Filadelfia", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Ceviti", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Molho Carpatio", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Vinho", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Maracuja", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Geleia", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Mel", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Gegilin", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Pimenta", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Leite Condensado", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Tare", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Romeu", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Ovos", sector = "Sushi", fabrication = null, validity = null))
                                        dao?.insertProduct(Products(name = "Creme Chees", sector = "Sushi", fabrication = null, validity = null))
                                    } catch (e: Exception) {
                                        Log.e("AppDatabase", "Erro ao inserir dados iniciais: ${e.message}")
                                    }
                                }
                            }
                        })
                        .addMigrations(MIGRATION_1_2)
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

        fun resetDatabase(context: Context, scope: CoroutineScope) {
            try {
                context.deleteDatabase("app_database")
                INSTANCE = null
                getDatabase(context, scope) // Recria o banco, acionando onCreate
            } catch (e: Exception) {
                Log.e("AppDatabase", "Erro ao resetar o banco de dados: ${e.message}")
            }
        }
    }
}