/*package com.example.carteogest.datadb.dbuser

import android.database.sqlite.SQLiteDatabase
import com.example.carteogest.datadb.dbrom.User


class UserRepository(private val db: SQLiteDatabase) {

    fun addUser(user: User) {
        val stmt = db.compileStatement(
            "INSERT INTO usuarios (nome, login, senha_hash, email, role_id, ativo,roleName) VALUES (?, ?, ?, ?, ?, ?,?)"
        )
        stmt.bindString(1, user.nome)
        stmt.bindString(2, user.login)
        stmt.bindString(3, user.senhaHash)
        stmt.bindString(4, user.email ?: "")
        stmt.bindLong(5, user.roleId.toLong())
        stmt.bindLong(6, if (user.ativo) 1 else 0)
        stmt.bindString(7,user.roleName)
        stmt.executeInsert()
    }
    fun updateUser(user: User) {
        val stmt = db.compileStatement(
            "UPDATE usuarios SET nome=?, email=?, role_id=? WHERE id=?"
        )
        stmt.bindString(1, user.nome)
        stmt.bindString(2, user.email ?: "")
        stmt.bindLong(3, user.roleId.toLong())
        stmt.bindLong(4, user.id!!.toLong())
        stmt.executeUpdateDelete()
    }
    fun getUsers(): List<User> {
        val cursor = db.rawQuery("SELECT * FROM usuarios", null)
        val users = mutableListOf<User>()
        while (cursor.moveToNext()) {
            users.add(
                User(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                    login = cursor.getString(cursor.getColumnIndexOrThrow("login")),
                    senhaHash = cursor.getString(cursor.getColumnIndexOrThrow("senha_hash")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    roleId = cursor.getInt(cursor.getColumnIndexOrThrow("role_id")),
                    ativo = cursor.getInt(cursor.getColumnIndexOrThrow("ativo")) == 1,
                    roleName = cursor.getString(cursor.getColumnIndexOrThrow("role_nome"))
                )
            )
        }
        cursor.close()
        return users
    }
    fun getRoleName(roleId: Int): String {
        val cursor = db.rawQuery("SELECT nome FROM roles WHERE id=?", arrayOf(roleId.toString()))
        return if (cursor.moveToFirst()) cursor.getString(0) else "Desconhecido"
    }
    fun deleteUser(userId: Int) {
        val stmt = db.compileStatement("DELETE FROM usuarios WHERE id=?")
        stmt.bindLong(1, userId.toLong())
        stmt.executeUpdateDelete()
    }
    fun autenticarUsuario(login: String, senhaHash: String): User? {
        val query = """
            SELECT u.id, u.nome, u.login, u.senha_hash, u.email, u.role_id, u.ativo, r.nome as role_nome
            FROM usuarios u
            JOIN roles r ON u.role_id = r.id
            WHERE u.login = ? AND u.senha_hash = ? AND u.ativo = 1
        """
        val cursor = db.rawQuery(query, arrayOf(login, senhaHash))

        val user = if (cursor.moveToFirst()) {
            User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                login = cursor.getString(cursor.getColumnIndexOrThrow("login")),
                senhaHash = cursor.getString(cursor.getColumnIndexOrThrow("senha_hash")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                roleId = cursor.getInt(cursor.getColumnIndexOrThrow("role_id")),
                ativo = cursor.getInt(cursor.getColumnIndexOrThrow("ativo")) == 1,
                roleName = cursor.getString(cursor.getColumnIndexOrThrow("role_nome"))
            )
        } else {
            null
        }
        cursor.close()
        return user
    }
}
*/