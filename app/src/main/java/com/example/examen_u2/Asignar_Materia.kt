package com.example.examen_u2

import android.os.Bundle
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
import org.json.JSONObject

class Asignar_Materia : AppCompatActivity() {

    private lateinit var etCodigoMateria: EditText
    private lateinit var etNombreMateria: EditText
    private lateinit var spinnerMatriculas: Spinner
    private lateinit var btnAsignarMateria: Button
    val url = "http://192.168.15.118/PHP_2025/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_asignar_materia)

        // Ajustar el padding para las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar elementos de la interfaz
        etCodigoMateria = findViewById(R.id.etCodigoMateria)
        etNombreMateria = findViewById(R.id.etNombreMateria)
        spinnerMatriculas = findViewById(R.id.spinnerMatriculas)
        btnAsignarMateria = findViewById(R.id.btnAsignarMateria)

        // Cargar las matrículas de los alumnos
        cargarMatriculas()

        // Configurar el botón para asignar la materia
        btnAsignarMateria.setOnClickListener {
            val codigoMateria = etCodigoMateria.text.toString()
            val nombreMateria = etNombreMateria.text.toString()
            val matriculaSeleccionada = spinnerMatriculas.selectedItem.toString()

            // Validar que los campos no estén vacíos
            if (codigoMateria.isNotEmpty() && nombreMateria.isNotEmpty() && matriculaSeleccionada.isNotEmpty()) {
                asignarMateria(codigoMateria, nombreMateria, matriculaSeleccionada)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para cargar las matrículas de los alumnos en el Spinner
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
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error al cargar las matrículas.", Toast.LENGTH_SHORT).show()
            })
        queue.add(stringRequest)
    }

    // Función para asignar la materia al alumno seleccionado
    private fun asignarMateria(codigo: String, nombre: String, matricula: String) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Request.Method.POST, url + "asignarMateria.php",
            { response ->
                try {
                    // Intentar parsear la respuesta como JSON
                    val responseObject = JSONObject(response)
                    val message = responseObject.getString("message")
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    // Si no es JSON, mostrar un mensaje de error
                    Toast.makeText(this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error al asignar la materia.", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = mutableMapOf<String, String>()
                params["codigo"] = codigo
                params["nombre"] = nombre
                params["matricula"] = matricula
                return params
            }
        }
        queue.add(stringRequest)
    }


}
