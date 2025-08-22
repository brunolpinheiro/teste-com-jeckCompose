package com.example.carteogest.login

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.carteogest.login.UserDao
import com.example.carteogest.login.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest


fun hashSenha(senha: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val bytes = md.digest(senha.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}


class AppDatabaseCallback(private val userDao: UserDao) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Inicializa o usuário admin
        val admin = User(
            nome = "Admin",
            senha = hashSenha("1234") // senha padrão
        )

        // Precisamos rodar em corrotina porque DAO é suspend
        CoroutineScope(Dispatchers.IO).launch {
            userDao.addUser(admin)
        }
    }
}
