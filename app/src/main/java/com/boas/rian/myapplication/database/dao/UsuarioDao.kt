package com.boas.rian.myapplication.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.boas.rian.myapplication.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert
    suspend fun salva(usuario: Usuario)

    @Query("SELECT * from Usuario where id = :id AND senha = :senha")
    suspend fun autentica(id: String, senha: String): Usuario?

    @Query("SELECT * from Usuario where id = :id")
    fun buscaPorId(id: String): Flow<Usuario?>
}