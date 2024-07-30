package com.boas.rian.myapplication.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.boas.rian.myapplication.databinding.ActivityPerfilUsuarioBinding
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class PerfilUsuarioActivity : UsuarioBaseActivity() {
    private val binding by lazy {
        ActivityPerfilUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        carregaUsuario()
        configuraBotao()
    }

    private fun configuraBotao() {
        val botaoSair = binding.activityPerfilUsuarioBotaoSair

        botaoSair.setOnClickListener {
            lifecycleScope.launch {
                deslogaUsuario()
            }
        }
    }

    private fun carregaUsuario() {
        val idUsuario = binding.activityPerfilUsuarioTextUsuario
        val nomeUsuario = binding.activityPerfilUsuarioTextNome
        lifecycleScope.launch {
            usuario
                .filterNotNull()
                .collect {
                    idUsuario.text = it.id
                    nomeUsuario.text = it.nome
                }
        }

    }
}