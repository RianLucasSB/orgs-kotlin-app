package com.boas.rian.myapplication.ui.dialog

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import coil.load
import com.boas.rian.myapplication.databinding.FormularioImagemBinding
import com.boas.rian.myapplication.extensions.tentaCarregar

class FormularioImagemDialog(private val context: Context) {
    fun mostra(urlPadrao: String? = null, cb: (url: String) -> Unit){
        FormularioImagemBinding.inflate(LayoutInflater.from(context)).apply {
            urlPadrao?.let {
                formularioImagemUrl.setText(it)
                formularioImagemImageView.load(it)
            }

            formularioImagemButton.setOnClickListener {
                val url = formularioImagemUrl.text.toString()
                formularioImagemImageView.tentaCarregar(url)
            }

            AlertDialog.Builder(context)
                .setView(this.root)
                .setPositiveButton("Confirmar") { _, _ ->
                    val url = formularioImagemUrl.text.toString()
                    formularioImagemImageView.tentaCarregar(url)
                    cb(url)
                }
                .setNegativeButton("Cancelar") { _, _ -> }
                .show()
        }

    }
}