package com.example.examen_u2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    lateinit var btn1: Button
    lateinit var btn2: Button
    lateinit var btn3: Button
    lateinit var btn4: Button
    lateinit var btn5: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btn1=findViewById(R.id.button1)
        btn2=findViewById(R.id.button2)
        btn3=findViewById(R.id.button3)
        btn4=findViewById(R.id.button4)
        btn5=findViewById(R.id.button5)

        btn1.setOnClickListener {
            val intento1 = Intent(this, Alumno::class.java)
            startActivity(intento1)
        }
        btn3.setOnClickListener {
            val intento3= Intent(this, Calificar::class.java)
            startActivity(intento3)
        }
        btn4.setOnClickListener {
            val intento4= Intent(this, Registrar_Alumno::class.java)
            startActivity(intento4)
        }
        btn5.setOnClickListener {
            val intento5= Intent(this, Asignar_Materia::class.java)
            startActivity(intento5)
        }
        btn2.setOnClickListener {
            val intento2= Intent(this, Consultar_Calif::class.java)
            startActivity(intento2)
        }

    }
}