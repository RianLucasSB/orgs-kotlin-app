package com.boas.rian.myapplication.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.boas.rian.myapplication.database.AppDatabase
import com.boas.rian.myapplication.databinding.ActivityFormularioProdutoBinding
import com.boas.rian.myapplication.extensions.tentaCarregar
import com.boas.rian.myapplication.model.Produto
import com.boas.rian.myapplication.ui.dialog.FormularioImagemDialog
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioProdutoBinding
    private val produtoDao by lazy {
        AppDatabase.instancia(this).produtoDao()
    }
    var url: String? = null
    var produtoId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioProdutoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configuraBotaoSalvar()
        binding.activityFormularioProdutoImagem.setOnClickListener {
            FormularioImagemDialog(this).mostra(url) {
                url = it
                binding.activityFormularioProdutoImagem.tentaCarregar(url)
            }
        }
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)

        lifecycleScope.launch {
            produtoDao.buscaPorId(produtoId)?.let {
                title = "Alterar produto"
                preencheCampos(it)
            }
        }
    }

    private fun preencheCampos(produto: Produto) {
        with(binding) {
            url = produto.imagem
            activityFormularioProdutoImagem.tentaCarregar(produto.imagem)
            activityFormularioProdutoNome.setText(produto.nome)
            activityFormularioProdutoDescricao.setText(produto.descricao)
            activityFormularioProdutoValor.setText(produto.valor.toPlainString())

        }
    }

    private fun configuraBotaoSalvar() {
        val botao = binding.activityFormularioProdutoBotaoSalvar

        botao.setOnClickListener {
            val produto = criaProduto()

            lifecycleScope.launch {
                produtoDao.salvaTodos(produto)
                finish()
            }
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

        return Produto(
            id = produtoId, nome = nome, descricao = descricao, valor = valor, imagem = url
        )
    }
}