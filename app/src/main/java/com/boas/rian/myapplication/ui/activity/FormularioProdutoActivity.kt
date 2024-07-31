package com.boas.rian.myapplication.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import android.R
import com.boas.rian.myapplication.database.AppDatabase
import com.boas.rian.myapplication.databinding.ActivityFormularioProdutoBinding
import com.boas.rian.myapplication.extensions.tentaCarregar
import com.boas.rian.myapplication.model.Produto
import com.boas.rian.myapplication.model.Usuario
import com.boas.rian.myapplication.ui.dialog.FormularioImagemDialog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FormularioProdutoActivity : UsuarioBaseActivity() {

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
                val campoUsuario = binding.formularioProdutoAutocompletetextview

                campoUsuario.visibility =
                    if(it.usuarioId == null){
                        configuraCampoUsuario()
                        VISIBLE
                    } else {
                        GONE
                    }
            }
        }
    }

    private fun configuraCampoUsuario() {
        lifecycleScope.launch {
            usuarios()
                .map { usuarios -> usuarios.map { it.id } }
                .collect { usuarios ->
                    configuraAutoCompleteTextView(usuarios)
                }
        }
    }

    private fun configuraAutoCompleteTextView(usuariosIds: List<String>) {
        val autoCompleteTextView = binding.formularioProdutoAutocompletetextview

        autoCompleteTextView.setAdapter(ArrayAdapter(
            this,
           R.layout.simple_dropdown_item_1line,
            usuariosIds
        ))

        autoCompleteTextView.setOnFocusChangeListener { _, focado ->
            if(!focado){
                validaUsuarioExistente(usuariosIds)
            }
        }
    }

    private fun validaUsuarioExistente(usuariosIds: List<String>): Boolean{
        val autoCompleteTextView = binding.formularioProdutoAutocompletetextview
        val usuarioSelecionado = autoCompleteTextView.text.toString()
        if(!usuariosIds.contains(usuarioSelecionado)){
            autoCompleteTextView.error = "usuário inexistente!"
            return false
        }
        return true
    }

    private suspend fun defineUsuarioId(usuario: Usuario): String = produtoDao
        .buscaPorId(produtoId)?.let { produtoEncontrado ->
            if (produtoEncontrado.usuarioId.isNullOrBlank()) {
                val usuarios = usuarios()
                    .map { usuariosEncontrados ->
                        usuariosEncontrados.map { it.id }
                    }.first()
                if (validaUsuarioExistente(usuarios)) {
                    val campoUsuarioId =
                        binding.formularioProdutoAutocompletetextview
                    return campoUsuarioId.text.toString()
                } else {
                    throw RuntimeException("Tentou salvar produto com usuário inexistente")
                }
            }
            null
        } ?: usuario.id

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
            lifecycleScope.launch {
                usuario.value?.let {
                    tentaSalvarProduto()
                }
            }
        }
    }

    private suspend fun tentaSalvarProduto() {
        usuario.value?.let { usuario ->
            try {
                val usuarioId = defineUsuarioId(usuario)
                val produto = criaProduto(usuarioId)
                produtoDao.salvaTodos(produto)
                finish()
            } catch (e: RuntimeException) {
                Log.e("FormularioProduto", "configuraBotaoSalvar: ", e)
            }
        }
    }

    private fun criaProduto(usuarioId: String): Produto {
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
            id = produtoId,
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url,
            usuarioId = usuarioId
        )
    }
}