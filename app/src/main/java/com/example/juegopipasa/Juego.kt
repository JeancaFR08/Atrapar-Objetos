package com.example.juegopipasa

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Juego : AppCompatActivity() {
    private var jugador: Float = 0.toFloat()
    private var marcador: Int = -2
    private var velocodadCaida: Float = 19.toFloat()
    private var velocodadCaidaDos: Float = 13.toFloat()
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    lateinit var canasta:ImageView
    lateinit var huevo:ImageView
    lateinit var huevoDos:ImageView
    lateinit var iv_roto:ImageView
    lateinit var iv_rotoDos:ImageView
    lateinit var puntos:TextView

    private var tv_tiempo: TextView?=null
    var tiempo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)

        iniciar()
        cuentaAtras()

        canasta.setOnTouchListener { _, event ->
            jugador = event.rawX - canasta.width / 2
            canasta.x = jugador
            true
        }
    }

    private fun cuentaAtras() {
        object : CountDownTimer(21000,1000){
            override fun onTick(millisUntilFinished: Long) {
                tiempo = (millisUntilFinished/1000).toInt()
                tv_tiempo?.text = "$tiempo s"
            }

            override fun onFinish() {
                if (marcador < 35 && tiempo == 0){
                    derrota()
               }
            }
        }.start()
    }

    private fun derrota() {
        val intent = Intent(this, Derrota::class.java)
        startActivity(intent)
        finish()
    }

    private fun iniciar() {
        canasta = findViewById(R.id.iv_canasta)
        huevo = findViewById(R.id.iv_huevo)
        huevoDos = findViewById(R.id.iv_huevoDos)
        puntos = findViewById(R.id.tv_puntos)
        tv_tiempo = findViewById(R.id.tv_tiempo)
        iv_roto = findViewById(R.id.iv_roto)
        iv_rotoDos = findViewById(R.id.iv_roto)

        handler = Handler()
        runnable = Runnable { moverObjeto()}
        handler.postDelayed(runnable, 0)
    }

    private fun moverObjeto() {
        val screenHeight = resources.displayMetrics.heightPixels.toFloat()

        if (huevo.y >= screenHeight-100) {
            // Aqui agregar la logica del huevo caido
            iv_roto.x = huevo.x
            iv_roto.visibility = View.VISIBLE
            //iv_roto.postDelayed({iv_roto.visibility = View.INVISIBLE}, 1000)
            huevoNuevo()
        } else {
            huevo.y += velocodadCaida

            if (agarrado()) {
                // El objeto ha sido atrapado
                marcador++
                puntos.text = "$marcador/35"
                validarGane()
                huevoNuevo()
            }
        }

        if (huevoDos.y >= screenHeight-100) {
            // Aqui agregar la logica del huevo caido
            iv_rotoDos.x = huevoDos.x
            iv_rotoDos.visibility = View.VISIBLE
            //iv_roto.postDelayed({iv_roto.visibility = View.INVISIBLE}, 1000)
            huevoNuevoDos()
        } else {
            huevoDos.y += velocodadCaidaDos

            if (agarradoDos()) {
                // El objeto ha sido atrapado
                marcador++
                puntos.text = "$marcador/35"
                validarGane()
                huevoNuevoDos()
            }
        }

        handler.postDelayed(runnable, 10)
    }

    private fun validarGane(){
        if (marcador >= 35 && tiempo > 0){
            val intent = Intent(this, Victoria::class.java)
            startActivity(intent)
            finish()
        } else if (marcador >= 35 && tiempo == 0) {
            derrota()
        }
    }

    //saca huevos nuevamente
    private fun huevoNuevo() {
        huevo.y = -huevo.height.toFloat()
        huevo.x = (Math.random() * (huevosAleatorios() - huevo.width)).toFloat()
    }

    private fun huevoNuevoDos() {
        huevoDos.y = -huevoDos.height.toFloat()
        huevoDos.x = (Math.random() * (huevosAleatorios() - huevoDos.width)).toFloat()
    }


    //valida si un huevo fue agarrado
    private fun agarrado(): Boolean {
        return huevo.y + huevo.height >= canasta.y &&
                huevo.x + huevo.width >= canasta.x &&
                huevo.x <= canasta.x + canasta.width
    }

    private fun agarradoDos(): Boolean {
        return huevoDos.y + huevoDos.height >= canasta.y &&
                huevoDos.x + huevo.width >= canasta.x &&
                huevoDos.x <= canasta.x + canasta.width
    }

    //Este metodo hace que los huevos salgan por diferente lado, segun el tamano de la pantalla
    private fun huevosAleatorios(): Int {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.widthPixels
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}