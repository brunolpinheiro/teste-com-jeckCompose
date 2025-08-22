/*package com.example.carteogest.datadb.dbuser

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "controle_estoque.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Criação das tabelas
        db.execSQL("""
            CREATE TABLE roles (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL
            )
        """)
        db.execSQL("""
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                login TEXT UNIQUE NOT NULL,
                senha_hash TEXT NOT NULL,
                email TEXT,
                role_id INTEGER NOT NULL,
                ativo INTEGER NOT NULL DEFAULT 1,
                FOREIGN KEY(role_id) REFERENCES roles(id)
            )
        """)

        // Insere roles iniciais
        db.execSQL("INSERT INTO roles (nome) VALUES ('Administrador')")
        db.execSQL("INSERT INTO roles (nome) VALUES ('Usuário')")

        // Cria usuário admin padrão
        val senhaHash = hashSenha("a1234") // <<< você pode trocar por um hash real (SHA-256, etc.)
        db.execSQL("""
            INSERT INTO usuarios (nome, login, senha_hash, email, role_id, ativo)
            VALUES ('Administrador', 'admin', '$senhaHash', 'admin@local', 1, 1)
        """)
        Log.d("DatabaseHelper", "Usuário admin criado com login 'admin' e senha 'admin123'")
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Aqui você implementa atualização do DB caso mude versão
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS roles")
        onCreate(db)
    }
    private fun hashSenha(senha: String): String {
        // ⚡ Implementação simples (não use em produção sem melhorar!)
        return senha.reversed() // apenas exemplo (coloque SHA-256 ou bcrypt de verdade)
    }

}
*/