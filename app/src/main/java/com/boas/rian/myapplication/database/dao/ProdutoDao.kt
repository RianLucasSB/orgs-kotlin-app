package com.boas.rian.myapplication.database.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.boas.rian.myapplication.model.Produto


@Dao
interface ProdutoDao {
    @Query("SELECT * from Produto")
    fun buscaTodos(): List<Produto>

    @Insert(onConflict = REPLACE)
    fun salvaTodos(vararg produto: Produto)

    @Query("SELECT * from Produto WHERE id = :id")
    fun buscaPorId(id: Long): Produto?

    @Delete
    fun remove(produto: Produto)

    @RawQuery
    fun buscaTodosPorQuery(query: SupportSQLiteQuery?): List<Produto>

    fun buscaProdutosOrderByAndOrderClause(column: String, orderClause: String): List<Produto> {
        val statement = "SELECT * FROM Produto ORDER BY $column ${orderClause.uppercase()}"
        val query: SupportSQLiteQuery = SimpleSQLiteQuery(statement, arrayOf())
        return buscaTodosPorQuery(query)
    }
}