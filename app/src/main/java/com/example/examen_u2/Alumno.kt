package com.example.examen_u2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class Alumno : AppCompatActivity() {

    lateinit var spinMatricula: Spinner
    lateinit var etNombre: EditText
    lateinit var etDireccion: EditText
    lateinit var etEmail: EditText
    lateinit var etTelefono: EditText
    lateinit var spinCarrera: Spinner
    lateinit var ivUserImage: ImageButton
    lateinit var btnActualizar: Button
    lateinit var btnEliminar: Button

    val url = "http://192.168.15.118/PHP_2025/"
    var imagenBase64: String = ""
    val CARRERAS = arrayOf("Informatica", "Administracion", "Mecatronica", "Alimentarias", "Industrial")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumno)

        spinMatricula = findViewById(R.id.spinMatricula)
        etNombre = findViewById(R.id.etNombre)
        etDireccion = findViewById(R.id.etDireccion)
        etEmail = findViewById(R.id.etEmail)
        etTelefono = findViewById(R.id.etTelefono)
        spinCarrera = findViewById(R.id.spinCarrera)
        ivUserImage = findViewById(R.id.ivUserImage)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnEliminar = findViewById(R.id.btnEliminar)

        // Inicializamos el Spinner de carreras
        val carreraAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, CARRERAS)
        carreraAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinCarrera.adapter = carreraAdapter
        spinCarrera.setSelection(-1) // Dejamos el spinner de carrera vacío inicialmente

        // Inicializamos el Spinner de matrículas con un valor vacío
        val listaMatriculas = arrayListOf<String>("") // Matricula vacía por defecto
        val matriculaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaMatriculas)
        matriculaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinMatricula.adapter = matriculaAdapter

        // Ahora solo cargamos las matrículas al seleccionar el Spinner de matrícula
        spinMatricula.onItemSelectedListener= object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Evitamos que la selección inicial dispare la consulta de matrículas
                if (position == 0) return

                // Si se selecciona una matrícula, cargamos los datos
                cargarDatosAlumno(spinMatricula.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Aquí también se puede limpiar los campos si es necesario
            }
        }

        ivUserImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        btnActualizar.setOnClickListener {
            actualizarAlumno()
        }

        btnEliminar.setOnClickListener {
            eliminarAlumno()
        }

        // Cargar matrículas al inicio
        cargarMatriculas()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            val inputStream = contentResolver.openInputStream(uri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            ivUserImage.setImageBitmap(bitmap)

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val bytes = stream.toByteArray()
            imagenBase64 = Base64.encodeToString(bytes, Base64.DEFAULT)
        }
    }

    private fun cargarMatriculas() {
        val requestQueue = Volley.newRequestQueue(this)
        val listaMatriculas = ArrayList<String>()

        val stringRequest = StringRequest(
            Request.Method.GET, url + "consultarMatriculas.php",
            { response ->
                val jsonArray = JSONArray(response)

                // Agregar un valor vacío solo si la lista no está vacía
                if (jsonArray.length() > 0) {
                    listaMatriculas.add("") // Deja el primer elemento vacío solo si hay elementos
                }

                // Filtramos registros nulos o vacíos
                for (i in 0 until jsonArray.length()) {
                    val matricula = jsonArray.getString(i)
                    if (matricula.isNotEmpty()) { // Aseguramos que la matrícula no esté vacía
                        listaMatriculas.add(matricula)
                    }
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaMatriculas)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinMatricula.adapter = adapter
            },
            { error ->
                Toast.makeText(this, "Error al cargar matrículas: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        requestQueue.add(stringRequest)
    }


    private fun cargarDatosAlumno(matricula: String) {
        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            Method.POST, url + "consultaAlumnoMatricula.php",
            { response ->
                val json = JSONObject(response)
                etNombre.setText(json.getString("nombre"))
                etDireccion.setText(json.getString("direccion"))
                etEmail.setText(json.getString("email"))
                etTelefono.setText(json.getString("telefono"))

                val carrera = json.getString("carrera")
                val indexCarrera = CARRERAS.indexOf(carrera)
                if (indexCarrera >= 0) {
                    spinCarrera.setSelection(indexCarrera)
                }

                val fotoBase64 = json.getString("foto")
                if (fotoBase64.isNotEmpty()) {
                    try {
                        val imagenBytes = Base64.decode(fotoBase64, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.size)
                        val roundedBitmap = getRoundedBitmap(bitmap) // Redondeamos la imagen
                        ivUserImage.setImageBitmap(roundedBitmap)
                        imagenBase64 = fotoBase64
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ivUserImage.setImageResource(android.R.drawable.ic_menu_camera) // Imagen predeterminada
                    }
                } else {
                    ivUserImage.setImageResource(android.R.drawable.ic_menu_camera) // Imagen predeterminada si no tiene foto
                }
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("matricula" to matricula)
            }
        }

        requestQueue.add(stringRequest)
    }

    private fun limpiarCampos() {
        etNombre.text.clear()
        etDireccion.text.clear()
        etEmail.text.clear()
        etTelefono.text.clear()
        spinCarrera.setSelection(-1)  // Restablecer el spinner de carrera a vacío
        ivUserImage.setImageResource(android.R.drawable.ic_menu_camera) // Restablecer la imagen por defecto
    }

    private fun actualizarAlumno() {
        val matricula = spinMatricula.selectedItem.toString()
        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            Method.POST, url + "actualizarAlumno.php",
            { response ->
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "matricula" to matricula,
                    "nombre" to etNombre.text.toString(),
                    "direccion" to etDireccion.text.toString(),
                    "email" to etEmail.text.toString(),
                    "telefono" to etTelefono.text.toString(),
                    "carrera" to spinCarrera.selectedItem.toString(),
                    "foto" to imagenBase64
                )
            }
        }

        requestQueue.add(stringRequest)
    }

    private fun eliminarAlumno() {
        val matricula = spinMatricula.selectedItem.toString()
        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            Method.POST, url + "eliminarAlumno.php",
            { response ->
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                cargarMatriculas() // Recargar la lista de matrículas
                limpiarCampos() // Limpiar campos después de eliminar
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("matricula" to matricula)
            }
        }

        requestQueue.add(stringRequest)
    }

    // Función para redondear la imagen
    private fun getRoundedBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val radius = Math.min(width, height) / 2

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(output)
        val paint = android.graphics.Paint()
        paint.isAntiAlias = true
        paint.color = android.graphics.Color.BLACK

        val rect = android.graphics.Rect(0, 0, width, height)
        val rectF = android.graphics.RectF(rect)
        canvas.drawRoundRect(rectF, radius.toFloat(), radius.toFloat(), paint)

        paint.xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }
}
