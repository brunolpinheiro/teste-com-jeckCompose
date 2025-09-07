// UserRepository.kt
package com.example.gest.datadb.data_db.login

import android.util.Log
import java.security.MessageDigest



class UserRepository(private val userDao: UserDao,
                     private val userPrefs: UserPrefs) {

     private fun hashSenha(senha: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(senha.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
    suspend fun addUser(user: User){
        val userHashed = user.copy(senha = user.senha)
        userDao.addUser(userHashed)
    }
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    suspend fun getUsers() = userDao.getUsers()
    suspend fun autenticarUsuario(nome: String, senha: String) = userDao.autenticarUsuario(nome, senha)

    suspend  fun findByName(nome: String): Boolean {
        return try {
            val exists = userDao.findByName(nome)
            Log.d("SupplierModelView", "Verificação do nome do fornecedor  '$nome': $exists")
            exists

        } catch (e: Exception) {
            Log.e("SupplierModelView", "Não foi possível verificar o nome do produto: ${e.message}")
            false
        }
    }
    suspend fun getById(id: Int): User? {
        return userDao.getById(id)
    }
    suspend fun findUserByName(nome: String): User? {
        return try {
            userDao.findUserByName(nome)
        } catch (e: Exception) {
            Log.e("UserRepository", "Erro ao buscar usuário pelo nome: ${e.message}")
            null
        }
    }
    suspend fun getPermissao(nome: String): String? {
        return userDao.getPermissaoByName(nome)
    }

    suspend fun logout() {
        userPrefs.clearLoggedUser()
    }
    // 👇 expõe o prefs também
    fun loggedUserFlow() = userPrefs.loggedUserName
    suspend fun saveLoggedUser(nome: String) = userPrefs.saveLoggedUser(nome)
    suspend fun clearLoggedUser() = userPrefs.clearLoggedUser()
}

