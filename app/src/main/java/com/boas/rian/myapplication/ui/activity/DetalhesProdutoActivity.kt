package com.boas.rian.myapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.boas.rian.myapplication.R
import com.boas.rian.myapplication.database.AppDatabase
import com.boas.rian.myapplication.databinding.ActivityDetalhesProdutoBinding
import com.boas.rian.myapplication.extensions.tentaCarregar
import com.boas.rian.myapplication.model.Produto
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class DetalhesProdutoActivity : AppCompatActivity() {
    private var produto: Produto? = null
    private var produtoId = 0L
    private val binding: ActivityDetalhesProdutoBinding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }
    private val produtoDao by lazy {
        AppDatabase.instancia(this).produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        tentaCarregarProduto()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            produto = produtoDao.buscaPorId(produtoId)
            produto?.let {
                preencheProduto(it)
            } ?: finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_detalhes_produto_editar -> {
                Intent(this, FormularioProdutoActivity::class.java).apply {
                    putExtra(CHAVE_PRODUTO_ID, produtoId)
                    startActivity(this)
                }
            }
            R.id.menu_detalhes_produto_remover -> {
                lifecycleScope.launch {
                    produto?.let { produtoDao.remove(it) }
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tentaCarregarProduto() {
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
    }

    private fun preencheProduto(produto: Produto){
        with(binding) {
            activityDetalhesProdutoImageView.tentaCarregar(produto.imagem)
            activityDetalhesProdutoNome.text = produto.nome
            activityDetalhesProdutoDesricao.text = produto.descricao
            val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            val valorEmReal = formatador.format(produto.valor)
            activityDetalhesProdutoPreco.text = valorEmReal

        }
    }
}