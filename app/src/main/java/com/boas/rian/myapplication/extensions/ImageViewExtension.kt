package com.boas.rian.myapplication.extensions

import android.widget.ImageView
import coil.load
import com.boas.rian.myapplication.R

fun ImageView.tentaCarregar(url: String? = null){
    load(url){
        fallback(R.drawable.erro)
        error(R.drawable.erro)
        placeholder(R.drawable.placeholder)
    }
}