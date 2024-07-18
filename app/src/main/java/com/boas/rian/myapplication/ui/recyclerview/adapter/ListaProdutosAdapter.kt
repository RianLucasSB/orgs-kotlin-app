package com.boas.rian.myapplication.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.boas.rian.myapplication.R
import com.boas.rian.myapplication.databinding.ActivityListaProdutosBinding
import com.boas.rian.myapplication.databinding.ProdutoItemBinding
import com.boas.rian.myapplication.model.Produto

class ListaProdutosAdapter(
    private val context: Context,
    produtos: List<Produto>
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private var produtos = produtos.toMutableList()

    class ViewHolder(binding: ProdutoItemBinding): RecyclerView.ViewHolder(binding.root) {
        val nome = binding.produtoItemNome
        val descricao = binding.produtoItemDescricao
        val valor = binding.produtoItemValor

        fun vincula(produto: Produto){
            nome.text = produto.nome
            descricao.text = produto.descricao
            valor.text = produto.valor.toPlainString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProdutoItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vincula(produtos[position])
    }

    override fun getItemCount(): Int = produtos.size

    fun atualiza(produtos: List<Produto>) {
        this.produtos.clear()
        this.produtos.addAll(produtos)
        notifyDataSetChanged()
    }
}
