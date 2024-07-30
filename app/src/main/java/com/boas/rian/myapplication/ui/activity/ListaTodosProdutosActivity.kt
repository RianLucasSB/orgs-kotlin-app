package com.boas.rian.myapplication.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.boas.rian.myapplication.database.AppDatabase
import com.boas.rian.myapplication.databinding.ActivityListaTodosProdutosBinding
import com.boas.rian.myapplication.extensions.vaiPara
import com.boas.rian.myapplication.model.Produto
import com.boas.rian.myapplication.ui.recyclerview.adapter.CabecalhoAdapter
import com.boas.rian.myapplication.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ListaTodosProdutosActivity : UsuarioBaseActivity() {
    private val binding by lazy {
        ActivityListaTodosProdutosBinding.inflate(layoutInflater)
    }
    private val dao by lazy {
        AppDatabase.instancia(this).produtoDao()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
    }

    private fun configuraRecyclerView(){
        val recyclerView = binding.activityTodosProdutosRecyclerview
        lifecycleScope.launch {
            dao.buscaTodos()
                .map {
                    it.sortedBy { it.usuarioId }
                        .groupBy { it.usuarioId }
                        .map { criaAdapterDeProdutosComCabecalho(it) }
                        .flatten()
                }
                .collect { adapter ->
                    recyclerView.adapter = ConcatAdapter(adapter)
                }
        }

    }

    private fun criaAdapterDeProdutosComCabecalho(produtosUsuario: Map.Entry<String?, List<Produto>>) =
        listOf(
            CabecalhoAdapter(this, listOf(produtosUsuario.key)),
            ListaProdutosAdapter(
                this,
                produtosUsuario.value
            ) { produtoClicado ->
                vaiPara(DetalhesProdutoActivity::class.java) {
                    putExtra(CHAVE_PRODUTO_ID, produtoClicado.id)
                }
            }
        )

}