package com.example.carteogest.datadb.data_db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.carteogest.datadb.data_db.products.ProdutsDao
import com.example.carteogest.datadb.data_db.products.Products
import com.example.carteogest.datadb.data_db.supplier.Supplier
import com.example.carteogest.datadb.data_db.supplier.SupplierDao
import com.example.carteogest.login.User
import com.example.carteogest.login.UserDao
import com.example.carteogest.login.hashSenha
import com.example.carteogest.login.permissoes.Permissao
import com.example.carteogest.login.permissoes.PermissaoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type


@Database(entities = [Products::class, Supplier::class,User::class, Permissao::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productsDao(): ProdutsDao  // Corrigido o erro de digitação
    abstract fun supplierDao(): SupplierDao
    abstract fun userDao(): UserDao
    abstract fun permissaoDao(): PermissaoDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?:
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                scope.launch(Dispatchers.IO) {
                                    try {
                                        INSTANCE?.let { database ->
                                            val userDao = database.userDao()
                                            val productsDao = database.productsDao()
                                            val supplierDao = database.supplierDao()
                                            userDao.addUser(User(nome = "admin", senha ="1234"))

                                        }
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