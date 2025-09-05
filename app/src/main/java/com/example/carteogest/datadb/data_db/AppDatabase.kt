package com.example.carteogest.datadb.data_db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.carteogest.datadb.data_db.products.ProdutsDao
import com.example.carteogest.datadb.data_db.products.Products
import com.example.carteogest.datadb.data_db.supplier.Supplier
import com.example.carteogest.datadb.data_db.supplier.SupplierDao
import com.example.carteogest.datadb.data_db.login.User
import com.example.carteogest.datadb.data_db.login.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.carteogest.datadb.data_db.validity.ValidityAndFabrication
import com.example.carteogest.datadb.data_db.validity.ValidityDao


@Database(entities = [Products::class, Supplier::class, User::class, ValidityAndFabrication::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productsDao(): ProdutsDao  // Corrigido o erro de digitação
    abstract fun supplierDao(): SupplierDao
    abstract fun userDao(): UserDao
    abstract fun validityDao(): ValidityDao

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
                                        val validityDao = INSTANCE?.validityDao()
                                        userDao.addUser(User(nome = "admin", senha ="1234", permissao = "ADMIN"))

                                    }
                                } catch (e: Exception) {
                                    Log.e("AppDatabase", "Erro ao inserir dados iniciais: ${e.message}")
                                }
                            }
                        }
                    })
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
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

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS supplier (
                        uid INTEGER PRIMARY KEY NOT NULL,
                        name TEXT NOT NULL,
                        cnpj TEXT NOT NULL,
                        adress TEXT,
                        email TEXT,
                        phone TEXT,
                        createdAt TEXT NOT NULL
                    )
                """.trimIndent())
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Cria a tabela validity se não existir
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS validity (
                        uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        produtoId INTEGER NOT NULL,
                        fabrication TEXT NOT NULL,
                        validity TEXT NOT NULL,
                        FOREIGN KEY (produtoId) REFERENCES Products(uid) ON DELETE CASCADE
                    )
                """.trimIndent())

                // Cria índice para produtoId
                database.execSQL("CREATE INDEX IF NOT EXISTS index_validity_produtoId ON validity (produtoId)")

                // Migra dados das colunas antigas de Products para a nova tabela validity
                database.execSQL("""
                    INSERT INTO validity (produtoId, fabrication, validity)
                    SELECT uid, fabrication, validity FROM Products
                    WHERE fabrication IS NOT NULL OR validity IS NOT NULL
                """.trimIndent())

                // Atualiza colunas vazias em Products com defaults antes de remover (se necessário)
                database.execSQL("UPDATE Products SET fabrication = '' WHERE fabrication IS NULL")
                database.execSQL("UPDATE Products SET validity = '' WHERE validity IS NULL")

                // Recria a tabela Products sem as colunas fabrication e validity
                database.execSQL("""
                    CREATE TABLE products_new (
                        uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        Nome TEXT NOT NULL,
                        refencia TEXT NOT NULL,
                        Setor TEXT NOT NULL,
                        Preço REAL NOT NULL,
                        "Preço Promocional" REAL,
                        Quantidade INTEGER NOT NULL,
                        Marca TEXT NOT NULL,
                        Fornecedor INTEGER,
                        status INTEGER NOT NULL,
                        barcode TEXT,
                        cost REAL,
                        unit_of_measure TEXT,
                        createdAt TEXT NOT NULL
                    )
                """.trimIndent())

                // Copia os dados para a nova tabela (excluindo fabrication e validity)
                database.execSQL("""
                    INSERT INTO products_new (uid, Nome, refencia, Setor, Preço, "Preço Promocional", Quantidade, Marca, Fornecedor, status, barcode, cost, unit_of_measure, createdAt)
                    SELECT uid, Nome, refencia, Setor, Preço, "Preço Promocional", Quantidade, Marca, Fornecedor, status, barcode, cost, unit_of_measure, createdAt FROM products
                """.trimIndent())

                // Apaga a tabela antiga
                database.execSQL("DROP TABLE products")

                // Renomeia a nova tabela para products
                database.execSQL("ALTER TABLE products_new RENAME TO products")
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