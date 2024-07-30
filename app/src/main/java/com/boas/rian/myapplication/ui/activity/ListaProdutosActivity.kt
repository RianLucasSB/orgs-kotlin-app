package com.boas.rian.myapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.boas.rian.myapplication.R
import com.boas.rian.myapplication.database.AppDatabase
import com.boas.rian.myapplication.databinding.ActivityListaProdutosBinding
import com.boas.rian.myapplication.extensions.vaiPara
import com.boas.rian.myapplication.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ListaProdutosActivity : UsuarioBaseActivity() {

    private val adapter = ListaProdutosAdapter(this)
    private lateinit var binding: ActivityListaProdutosBinding
    private var _ordenacaoSelecionada = MutableStateFlow(OrdernarProdutoEnum.NENHUM)
    val ordenacaoSelecionada: StateFlow<OrdernarProdutoEnum> = _ordenacaoSelecionada

    private val produtosDao by lazy {
        AppDatabase.instancia(this).produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()
        lifecycleScope.launch {
            launch {
                usuario.filterNotNull().collect {
                    buscaProdutosUsuario(it.id)
                }
            }
        }
    }


    private fun buscaProdutosUsuario(usuarioId: String) {
        lifecycleScope.launch {
            ordenacaoSelecionada.collect { ordernarProdutoEnum ->
                when (ordernarProdutoEnum) {
                    OrdernarProdutoEnum.NENHUM -> {
                        lifecycleScope.launch {
                            produtosDao.buscaTodosPorUsuarioId(usuarioId).collect {
                                adapter.atualiza(it)
                            }
                        }
                    }
                    else -> ordenaProdutos(usuarioId)
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_produtos, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_lista_produto_ordenar_preco_asc -> {
                _ordenacaoSelecionada.value = OrdernarProdutoEnum.VALOR_ASC
                item.isChecked = !item.isChecked
            }
            R.id.menu_lista_produto_ordenar_preco_desc -> {
                _ordenacaoSelecionada.value = OrdernarProdutoEnum.VALOR_DESC
                item.isChecked = !item.isChecked
            }
            R.id.menu_lista_produto_ordenar_nome_asc -> {
                _ordenacaoSelecionada.value = OrdernarProdutoEnum.NOME_ASC
                item.isChecked = !item.isChecked
            }
            R.id.menu_lista_produto_ordenar_nome_desc -> {
                _ordenacaoSelecionada.value = OrdernarProdutoEnum.NOME_DESC
                item.isChecked = !item.isChecked
            }
            R.id.menu_lista_produto_ordenar_nenhum -> {
                _ordenacaoSelecionada.value = OrdernarProdutoEnum.NENHUM
                item.isChecked = !item.isChecked
            }
            R.id.menu_lista_produto_deslogar -> {
                vaiPara(PerfilUsuarioActivity::class.java)
            }
            R.id.menu_lista_produto_todos_produtos -> {
                vaiPara(ListaTodosProdutosActivity::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun ordenaProdutos(usuarioId: String) {
        val column = ordenacaoSelecionada.value.getString().split("-")[0]
        val orderClause = ordenacaoSelecionada.value.getString().split("-")[1]

        lifecycleScope.launch {
            produtosDao.buscaProdutosOrderByAndOrderClause(column, orderClause, usuarioId).collect {
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