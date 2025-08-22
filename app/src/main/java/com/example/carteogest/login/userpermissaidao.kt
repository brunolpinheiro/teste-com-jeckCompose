package com.example.carteogest.login

import androidx.room.*
import com.example.carteogest.login.permissoes.Permissao


@Dao
interface UsuarioPermissaoDao {
    @Insert
    suspend fun addUsuarioPermissao(usuarioPermissao: UsuarioPermissao)

    @Delete
    suspend fun deleteUsuarioPermissao(usuarioPermissao: UsuarioPermissao)

    @Query("""
        SELECT p.* FROM permissoes p
        INNER JOIN usuario_permissoes up ON p.id = up.permissaoId
        WHERE up.usuarioId = :usuarioId
    """)
    suspend fun getPermissoesDoUsuario(usuarioId: Int): List<Permissao>
}
