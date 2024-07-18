package com.boas.rian.myapplication.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.boas.rian.myapplication.R
import com.boas.rian.myapplication.dao.ProdutosDao
import com.boas.rian.myapplication.databinding.ActivityFormularioProdutoBinding
import com.boas.rian.myapplication.model.Produto
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {
    val dao = ProdutosDao()
    private lateinit var binding: ActivityFormularioProdutoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioProdutoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configuraBotaoSalvar()
    }

    private fun configuraBotaoSalvar() {
        val botao = binding.activityFormularioProdutoBotaoSalvar

        botao.setOnClickListener {
            val produto = criaProduto()

            dao.adiciona(produto)
            finish()
        }
    }

    private fun criaProduto(): Produto {
        val campoNome = binding.activityFormularioProdutoNome
        val campoDescricao = binding.activityFormularioProdutoDescricao
        val campoValor = binding.activityFormularioProdutoValor

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