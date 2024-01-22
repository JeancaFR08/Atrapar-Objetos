package com.example.juegopipasa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class Menu : AppCompatActivity() {

    lateinit var iv_inicio:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    private fun iniciar() {
        iv_inicio = findViewById(R.id.iv_inicio)
    }

    fun InicioJuego(imagen: View){
        var intent = Intent(this, Juego::class.java)
        startActivity(intent)
        finish()
    }
}