package com.boas.rian.myapplication.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.boas.rian.myapplication.R
import com.boas.rian.myapplication.dao.ProdutosDao
import com.boas.rian.myapplication.model.Produto
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity(R.layout.activity_formulario_produto) {
    val dao = ProdutosDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configuraBotaoSalvar()
    }

    private fun configuraBotaoSalvar() {
        val botao = findViewById<Button>(R.id.activity_formulario_produto_botao_salvar)

        botao.setOnClickListener {
            val produto = criaProduto()

            dao.adiciona(produto)
            finish()
        }
    }

    private fun criaProduto(): Produto {
        val campoNome = findViewById<EditText>(R.id.activity_formulario_produto_nome)
        val campoDescricao = findViewById<EditText>(R.id.activity_formulario_produto_descricao)
        val campoValor = findViewById<EditText>(R.id.activity_formulario_produto_valor)

        val nome = campoNome.text.toString()
        val descricao = campoDescricao.text.toString()
        val valorEmTexto = campoValor.text.toString()
        val valor = if (valorEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorEmTexto)
        }

        return Produto(nome, descricao, valor)
    }
}