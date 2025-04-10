package com.example.examen_u2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class Calificar : AppCompatActivity() {

    private lateinit var spinnerMatriculas: Spinner
    private lateinit var spinnerMaterias: Spinner
    private lateinit var etU1: EditText
    private lateinit var etU2: EditText
    private lateinit var etU3: EditText
    private lateinit var etU4: EditText
    private lateinit var etU5: EditText
    private lateinit var btnGuardarCalificacion: Button
    val url = "http://192.168.15.118/PHP_2025/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calificar)

        // Ajustar el padding para las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar los elementos de la interfaz
        spinnerMatriculas = findViewById(R.id.spinnerMatriculas)
        spinnerMaterias = findViewById(R.id.spinnerMaterias)
        etU1 = findViewById(R.id.etU1)
        etU2 = findViewById(R.id.etU2)
        etU3 = findViewById(R.id.etU3)
        etU4 = findViewById(R.id.etU4)
        etU5 = findViewById(R.id.etU5)
        btnGuardarCalificacion = findViewById(R.id.btnGuardarCalificacion)

        // Cargar las matrículas de los alumnos
        cargarMatriculas()

        // Configurar el botón para guardar las calificaciones
        btnGuardarCalificacion.setOnClickListener {
            val matriculaSeleccionada = spinnerMatriculas.selectedItem.toString()
            val matSelec = spinnerMaterias.selectedItem.toString()
            val u1 = etU1.text.toString()
            val u2 = etU2.text.toString()
            val u3 = etU3.text.toString()
            val u4 = etU4.text.toString()
            val u5 = etU5.text.toString()

            // Validar que los campos no estén vacíos
            if (u1.isNotEmpty() && u2.isNotEmpty() && u3.isNotEmpty() && u4.isNotEmpty() && u5.isNotEmpty()) {
                guardarCalificacion(matSelec, u1, u2, u3, u4, u5)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            }
            etU1.setText("")
            etU2.setText("")
            etU3.setText("")
            etU4.setText("")
            etU5.setText("")
            spinnerMaterias.setSelection(-1)
        }
    }

    // Función para cargar las matrículas de los alumnos
    private fun cargarMatriculas() {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, url + "consultarMatriculas.php",
            { response ->
                val listaMatriculas = arrayListOf<String>("") // Elemento vacío para inicializar
                val jsonArray = JSONArray(response)

                // Filtramos y agregamos las matrículas no vacías
                for (i in 0 until jsonArray.length()) {
                    val matricula = jsonArray.getString(i)
                    if (matricula.isNotEmpty()) {
                        listaMatriculas.add(matricula)
                    }
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaMatriculas)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerMatriculas.adapter = adapter

                // Listener para selección de matrícula
                spinnerMatriculas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position == 0) return // Evitar la selección inicial
                        cargarMaterias(spinnerMatriculas.selectedItem.toString())
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Aquí puedes manejar lo que sucede cuando no se selecciona nada
                    }
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error al cargar las matrículas.", Toast.LENGTH_SHORT).show()
            })
        queue.add(stringRequest)
    }

    // Función para cargar las materias asignadas a la matrícula seleccionada
    private fun cargarMaterias(matricula: String) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, url + "consultaMateriaPorAlumno.php?matricula=$matricula",
            { response ->
                try {
                    // Depuración para ver la respuesta
                    Log.d("JSONResponse", response)

                    val listaMaterias = arrayListOf<String>("") // Elemento vacío para inicializar
                    val jsonArray = JSONArray(response)

                    // Filtramos y agregamos las materias
                    for (i in 0 until jsonArray.length()) {
                        val materia = jsonArray.getString(i)
                        if (materia.isNotEmpty()) {
                            listaMaterias.add(materia)
                        }
                    }

                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaMaterias)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerMaterias.adapter = adapter
                } catch (e: JSONException) {
                    Log.e("JSONError", "Error al analizar la respuesta JSON: ${e.message}")
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error al cargar las materias.", Toast.LENGTH_SHORT).show()
            })
        queue.add(stringRequest)
    }


    // Función para guardar las calificaciones en la base de datos
    private fun guardarCalificacion(
        nombreMateria: String,
        u1: String,
        u2: String,
        u3: String,
        u4: String,
        u5: String,
    ) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Request.Method.POST, url + "guardarCalificaciones.php",
            { response ->
                Toast.makeText(this, "Calificación guardada correctamente", Toast.LENGTH_SHORT).show()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error al guardar la calificación.", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = mutableMapOf<String, String>()
                params["nombre"] = nombreMateria
                params["u1"] = u1
                params["u2"] = u2
                params["u3"] = u3
                params["u4"] = u4
                params["u5"] = u5
                return params
            }
        }
        queue.add(stringRequest)
    }
}
