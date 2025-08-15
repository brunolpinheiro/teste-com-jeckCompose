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