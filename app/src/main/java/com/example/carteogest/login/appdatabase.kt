/* AppDatabase.kt
package com.example.carteogest.datadb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.carteogest.login.permissoes.PermissaoDao
import com.example.carteogest.login.UserDao
import com.example.carteogest.login.User
import com.example.carteogest.login.permissoes.Permissao
import com.example.carteogest.login.hashSenha
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/*
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao?
    abstract fun permissaoDao(): PermissaoDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meu_banco.db"
                )
                    .build()
            }
        }
    }
}*/
@Database(entities = [User::class, Permissao::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun permissaoDao(): PermissaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meu_banco.db"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Aqui você pode acessar o DAO via instance
                            INSTANCE?.let { database ->
                                val userDao = database.userDao()
                                CoroutineScope(Dispatchers.IO).launch {
                                    userDao.addUser(User(nome = "admin", senha = hashSenha("1234")))
                                }
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}



*/