package com.boas.rian.myapplication.dao

import com.boas.rian.myapplication.model.Produto
import java.math.BigDecimal

class ProdutosDao {

    fun adiciona(produto: Produto) {
        produtos.add(produto)
    }

    fun buscaTodos(): List<Produto> {
        return produtos.toList()
    }

    companion object {
        private val produtos = mutableListOf<Produto>(
            Produto("Cuzcuz", "Flocão", BigDecimal("2.99"))
        )
    }
}