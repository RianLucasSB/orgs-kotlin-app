package com.boas.rian.myapplication.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boas.rian.myapplication.databinding.ProdutoItemBinding
import com.boas.rian.myapplication.extensions.tentaCarregar
import com.boas.rian.myapplication.model.Produto
import java.text.NumberFormat
import java.util.*

class ListaProdutosAdapter(
    private val context: Context,
    produtos: List<Produto> = emptyList(),
    var onClickItemListener: (produto: Produto) -> Unit = {}
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private var produtos = produtos.toMutableList()

    inner class ViewHolder(binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val nome = binding.produtoItemNome
        val descricao = binding.produtoItemDescricao
        val valor = binding.produtoItemValor
        val imagem = binding.imageView

        private lateinit var produto: Produto

        init {
            itemView.setOnClickListener {
                if(::produto.isInitialized){
                    onClickItemListener(produto)
                }
            }
        }


        fun vincula(produto: Produto) {
            this.produto = produto

            nome.text = produto.nome
            descricao.text = produto.descricao
            val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            val valorEmReal = formatador.format(produto.valor)
            valor.text = valorEmReal

            val visibilidade = if (produto.imagem != null){
                View.VISIBLE
            } else{
                View.GONE
            }

            imagem.visibility = visibilidade

            imagem.tentaCarregar(produto.imagem)

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
