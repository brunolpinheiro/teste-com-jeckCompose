/*package com.example.carteogest.datadb.dbrom

import com.example.carteogest.datadb.dbrom.RoleDao
import com.example.carteogest.datadb.dbrom.User
import com.example.carteogest.datadb.dbrom.UserDao

class UserRepository(private val userDao: UserDao, private val roleDao: RoleDao) {

    suspend fun addUser(user: User) = userDao.addUser(user)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    suspend fun getUsers() = userDao.getUsers()

    suspend fun getRoleName(roleId: Int): String = roleDao.getRoles().find { it.id == roleId }?.nome ?: "Desconhecido"

    suspend fun autenticarUsuario(login: String, senhaHash: String) =
        userDao.autenticarUsuario(login, senhaHash)
}*/