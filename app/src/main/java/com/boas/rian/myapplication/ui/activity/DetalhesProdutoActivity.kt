package com.boas.rian.myapplication.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.boas.rian.myapplication.R
import com.boas.rian.myapplication.databinding.ActivityDetalhesProdutoBinding
import com.boas.rian.myapplication.extensions.tentaCarregar
import com.boas.rian.myapplication.model.Produto
import java.text.NumberFormat
import java.util.*

class DetalhesProdutoActivity : AppCompatActivity() {
    private val binding: ActivityDetalhesProdutoBinding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val produto: Produto = intent.getSerializableExtra("produto") as Produto

        preencheProduto(produto)
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