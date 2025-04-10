package com.example.examen_u2

import CalificacionesFragment
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class Consultar_Calif : AppCompatActivity() {
    val url = "http://192.168.15.118/PHP_2025/"
     lateinit var spinnerMatriculas: Spinner
     lateinit var spinnerMaterias: Spinner
    lateinit var tvNombreAlumno: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar_calif)

        spinnerMatriculas = findViewById(R.id.spinnerMatriculas)
        spinnerMaterias = findViewById(R.id.spinnerMaterias)
        tvNombreAlumno = findViewById(R.id.tvNombreAlumno)

        cargarMatriculas()

        spinnerMatriculas.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val matricula = spinnerMatriculas.selectedItem.toString()
                cargarNombreAlumno(matricula)
                cargarMaterias(matricula)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        spinnerMaterias.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val matricula = spinnerMatriculas.selectedItem.toString()
                val materia = spinnerMaterias.selectedItem.toString()
                mostrarCalificaciones(matricula, materia)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    private fun cargarMatriculas() {
        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(Request.Method.GET, url + "consultarMatriculas.php", {
            val jsonArray = JSONArray(it)
            val lista = ArrayList<String>()
            for (i in 0 until jsonArray.length()) {
                lista.add(jsonArray.getString(i))
            }
            spinnerMatriculas.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lista)
        }, {
            it.printStackTrace()
        })
        queue.add(request)
    }

    private fun cargarMaterias(matricula: String) {
        val queue = Volley.newRequestQueue(this)
        val request = object : StringRequest(Method.POST, url + "materiasAlumno.php", {
            val jsonArray = JSONArray(it)
            val materias = ArrayList<String>()
            for (i in 0 until jsonArray.length()) {
                materias.add(jsonArray.getString(i))
            }
            spinnerMaterias.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, materias)

            // Al cargar materias, mostrar automáticamente la primera si existe
            if (materias.isNotEmpty()) {
                mostrarCalificaciones(matricula, materias[0])
            }

        }, {
            it.printStackTrace()
        }) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("matricula" to matricula)
            }
        }
        queue.add(request)
    }

    private fun mostrarCalificaciones(matricula: String, materia: String) {
        val queue = Volley.newRequestQueue(this)
        val request = object : StringRequest(Method.POST, url + "obtenerCalificaciones.php", {
            val obj = JSONObject(it)
            Log.d("CalificacionesResponse", obj.toString())

            // Función para obtener cada calificación y convertir null o "null" en "0"
            fun safeGet(key: String): String {
                val value = obj.optString(key)
                return if (value == "null" || value.isEmpty()) "0" else value
            }

            // Crear la cadena concatenada con las calificaciones
            val calificaciones = """
            U1: ${safeGet("u1")}
            U2: ${safeGet("u2")}
            U3: ${safeGet("u3")}
            U4: ${safeGet("u4")}
            U5: ${safeGet("u5")}
        """.trimIndent()

            // Crear un Bundle para pasar la información al fragmento
            val bundle = Bundle().apply {
                putString("calificaciones", calificaciones)
            }

            // Crear una nueva instancia del fragmento y pasarle los argumentos
            val calificacionesFragment = CalificacionesFragment()
            calificacionesFragment.arguments = bundle

            // Reemplazar el fragmento con la nueva instancia
            supportFragmentManager.commit {
                replace(R.id.contenedorFragment, calificacionesFragment)
            }

        }, {
            it.printStackTrace()
        }) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("matricula" to matricula, "nombre" to materia)
            }
        }
        queue.add(request)
    }




    private fun cargarNombreAlumno(matricula: String) {
        val queue = Volley.newRequestQueue(this)
        val request = object : StringRequest(Method.POST, url + "obtenerNombreAlumno.php", {
            val obj = JSONObject(it)
            val nombre = obj.getString("nombre")
            tvNombreAlumno.text = "Alumno: $nombre"
        }, {
            it.printStackTrace()
        }) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("matricula" to matricula)
            }
        }
        queue.add(request)
    }
}



