package com.boas.rian.myapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.boas.rian.myapplication.R
import com.boas.rian.myapplication.database.AppDatabase
import com.boas.rian.myapplication.databinding.ActivityListaProdutosBinding
import com.boas.rian.myapplication.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.launch

class ListaProdutosActivity : AppCompatActivity() {

    private val adapter = ListaProdutosAdapter(this)
    private lateinit var binding: ActivityListaProdutosBinding
    private var ordenacaoSelecionada = OrdernarProdutoEnum.NENHUM

    private val produtosDao by lazy {
        AppDatabase.instancia(this).produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()
    }

    override fun onResume() {
        super.onResume()
        when (ordenacaoSelecionada) {
            OrdernarProdutoEnum.NENHUM -> {
                lifecycleScope.launch {
                    produtosDao.buscaTodos().collect {
                        adapter.atualiza(it)
                    }
                }
            }
            else -> ordenaProdutos(ordenacaoSelecionada)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_produtos, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_lista_produto_ordenar_preco_asc -> {
                ordenaProdutos(OrdernarProdutoEnum.VALOR_ASC)
                item.isChecked = !item.isChecked
            }
            R.id.menu_lista_produto_ordenar_preco_desc -> {
                ordenaProdutos(OrdernarProdutoEnum.VALOR_DESC)
                item.isChecked = !item.isChecked
            }
            R.id.menu_lista_produto_ordenar_nome_asc -> {
                ordenaProdutos(OrdernarProdutoEnum.NOME_ASC)
                item.isChecked = !item.isChecked
            }
            R.id.menu_lista_produto_ordenar_nome_desc -> {
                ordenaProdutos(OrdernarProdutoEnum.NOME_DESC)
                item.isChecked = !item.isChecked
            }
            R.id.menu_lista_produto_ordenar_nenhum -> {
                lifecycleScope.launch {
                    produtosDao.buscaTodos().collect {
                        adapter.atualiza(it)
                    }
                }
                ordenacaoSelecionada = OrdernarProdutoEnum.NENHUM
                item.isChecked = !item.isChecked
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun ordenaProdutos(value: OrdernarProdutoEnum) {
        ordenacaoSelecionada = value
        val column = value.getString().split("-")[0]
        val orderClause = value.getString().split("-")[1]

        lifecycleScope.launch {
            produtosDao.buscaProdutosOrderByAndOrderClause(column, orderClause).collect {
                adapter.atualiza(it)
            }
        }
    }

    private fun configuraFab() {
        val fab = binding.floatingActionButton

        fab.setOnClickListener {
            val intent = Intent(this, FormularioProdutoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        adapter.onClickItemListener = {
            val intent = Intent(
                this, DetalhesProdutoActivity::class.java
            ).apply {
                putExtra(CHAVE_PRODUTO_ID, it.id)
            }
            startActivity(intent)
        }
    }
}