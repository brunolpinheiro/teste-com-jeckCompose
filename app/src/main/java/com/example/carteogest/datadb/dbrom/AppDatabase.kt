/*
package com.example.carteogest.datadb.dbuser

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.carteogest.datadb.dbrom.Role
import com.example.carteogest.datadb.dbrom.RoleDao
import com.example.carteogest.datadb.dbrom.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.carteogest.datadb.dbrom.User
import androidx.compose.runtime.getValue

@Database(entities = [User::class, Role::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun roleDao(): RoleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "controle_estoque.db"
                )
                    .addCallback(RoomCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class RoomCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Insere roles e usuário admin inicial após criar o DB
            CoroutineScope(Dispatchers.IO).launch {
                INSTANCE?.let { database ->
                    val roleDao = database.roleDao()
                    val userDao = database.userDao()

                    // Insere roles iniciais
                    val adminRoleId = roleDao.insertRole(Role(nome = "Administrador")).toInt()
                    val userRoleId = roleDao.insertRole(Role(nome = "Usuário")).toInt()

                    // Insere usuário admin padrão
                    val senhaHash = "a1234".reversed() // hash simples, trocar para SHA-256/bcrypt
                    userDao.addUser(
                        User
*/