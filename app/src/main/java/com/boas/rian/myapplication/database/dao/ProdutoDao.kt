package com.boas.rian.myapplication.database.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.boas.rian.myapplication.model.Produto
import kotlinx.coroutines.flow.Flow


@Dao
interface ProdutoDao {
    @Query("SELECT * from Produto WHERE usuarioId = :usuarioId")
    fun buscaTodosPorUsuarioId(usuarioId: String): Flow<List<Produto>>

    @Query("SELECT * from Produto")
    fun buscaTodos(): Flow<List<Produto>>

    @Insert(onConflict = REPLACE)
    suspend fun salvaTodos(vararg produto: Produto)

    @Query("SELECT * from Produto WHERE id = :id")
    suspend fun buscaPorId(id: Long): Produto?

    @Delete
    suspend fun remove(produto: Produto)

    @RawQuery(observedEntities = [Produto::class])
    fun buscaTodosPorQuery(query: SupportSQLiteQuery): Flow<List<Produto>>

    fun buscaProdutosOrderByAndOrderClause(column: String, orderClause: String, usuarioId: String): Flow<List<Produto>> {
        val statement = "SELECT * FROM Produto WHERE usuarioId = ? ORDER BY $column ${orderClause.uppercase()}"
        val query: SupportSQLiteQuery = SimpleSQLiteQuery(statement, arrayOf(usuarioId))
        return buscaTodosPorQuery(query)
    }
}