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
                                    dao?.insertProduct(Products(uid = 0, name = "Suco de Laranja", sector = "Copa"))
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