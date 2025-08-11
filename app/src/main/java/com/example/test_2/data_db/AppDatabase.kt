package com.example.test_2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.test_2.data_db.Products
import com.example.test_2.data_db.ProdutsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log

@Database(entities = [Products::class], version = 1, exportSchema = false)
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
                    ).addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            scope.launch(Dispatchers.IO) {
                                try {
                                    val dao = INSTANCE?.productDao()
                                    dao?.insertProduct(Products(uid = 0, name = "Salmão", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Tilapia", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Prego", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Atum", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Povo", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Nigiri", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Couve", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Doritos", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Batata", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Alho Poro", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Wasabi", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Gengibre", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Cebola cebolinha", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Morango", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Castinha", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Goiabada", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Abacaxi", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Nori", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Filadelfia", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Ceviti", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Molho Carpatio", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Vinho", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Maracuja", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Geleia", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Mel", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Gegilin", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Pimenta", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Leite Condensado", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Tare", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Romeu", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Ovos", sector = "Sushi"))
                                    dao?.insertProduct(Products(uid = 0, name = "Creme Chees", sector = "Sushi"))




                                } catch (e: Exception) {
                                    Log.e("AppDatabase", "Erro ao inserir dados iniciais: ${e.message}")
                                }
                            }
                        }
                    }).build()
                    INSTANCE = instance
                    instance
                }
            } catch (e: Exception) {
                Log.e("AppDatabase", "Falha ao criar o banco de dados: ${e.message}")
                null
            }
        }
    }
}