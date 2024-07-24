package com.boas.rian.myapplication.ui.activity

enum class OrdernarProdutoEnum {
    VALOR_ASC {
        override fun getString(): String {
            return "valor-asc"
        }
    },
    VALOR_DESC {
        override fun getString(): String {
            return "valor-desc"
        }
    },
    NOME_ASC {
        override fun getString(): String {
            return "nome-asc"
        }
    },
    NOME_DESC {
        override fun getString(): String {
            return "nome-desc"
        }
    },
    NENHUM {
        override fun getString(): String {
            return "nenhum"
        }
    };

    abstract fun getString(): String
}