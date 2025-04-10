package com.example.examen_u2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream

class Registrar_Alumno : AppCompatActivity() {

    lateinit var etMatricula: EditText
    lateinit var etNombre: EditText
    lateinit var etDireccion: EditText
    lateinit var etEmail: EditText
    lateinit var etTelefono: EditText
    lateinit var spinnerCarrera: Spinner
    lateinit var ivUserImage: ImageButton
    lateinit var btnGuardar: Button

    val url = "http://192.168.15.118/PHP_2025/"
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_alumno)

        etMatricula = findViewById(R.id.etMatricula)
        etNombre = findViewById(R.id.etNombre)
        etDireccion = findViewById(R.id.etDireccion)
        etEmail = findViewById(R.id.etEmail)
        etTelefono = findViewById(R.id.etTelefono)
        spinnerCarrera = findViewById(R.id.spinnerCarrera)
        ivUserImage = findViewById(R.id.ivUserImage)
        btnGuardar = findViewById(R.id.btnGuardarContacto)

        val carreras = arrayOf("Informatica", "Alimentarias", "Mecatronica", "Administracion", "Industrial")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, carreras)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCarrera.adapter = adapter

        ivUserImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnGuardar.setOnClickListener {
            guardarAlumno()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val roundedBitmap = getRoundedBitmap(bitmap) // Redondeamos la imagen
            ivUserImage.setImageBitmap(roundedBitmap)
        }
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

    // Método para convertir la imagen en base64
    fun convertirImagenBase64(imageView: ImageButton): String {
        val drawable = imageView.drawable
        val bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun limpiarCampos() {
        etMatricula.text.clear()
        etNombre.text.clear()
        etDireccion.text.clear()
        etEmail.text.clear()
        etTelefono.text.clear()
        spinnerCarrera.setSelection(0)  // Restablecer el spinner de carrera a su valor inicial
        ivUserImage.setImageResource(android.R.drawable.ic_menu_camera) // Restablecer la imagen por defecto
    }

    fun guardarAlumno() {
        val matriculaText = etMatricula.text.toString()

        if (matriculaText.isNotEmpty() && matriculaText.toIntOrNull() != null) {
            val requestQueue = Volley.newRequestQueue(this)

            // Verificar si la imagen fue seleccionada
            val imagenBase64 = if (ivUserImage.drawable != null) {
                convertirImagenBase64(ivUserImage)  // Codificar imagen en base64
            } else {
                ""  // Si no se selecciona una imagen, se guarda como vacío o puedes asignar una imagen predeterminada
            }

            val stringRequest = object : StringRequest(Request.Method.POST, url+"insertarAlumno.php",
                { response ->
                    try {
                        val json = JSONObject(response)
                        Toast.makeText(this, json.getString("message"), Toast.LENGTH_LONG).show()
                        limpiarCampos() // Limpiar los campos después de guardar
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error en la respuesta: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }) {

                override fun getParams(): MutableMap<String, String>? {
                    val parametros = HashMap<String, String>()
                    parametros["matricula"] = matriculaText
                    parametros["nombre"] = etNombre.text.toString()
                    parametros["direccion"] = etDireccion.text.toString()
                    parametros["email"] = etEmail.text.toString()
                    parametros["telefono"] = etTelefono.text.toString()
                    parametros["carrera"] = spinnerCarrera.selectedItem.toString()
                    parametros["foto"] = imagenBase64  // Enviar la imagen codificada en base64
                    return parametros
                }
            }

            requestQueue.add(stringRequest)
        } else {
            Toast.makeText(this, "Matrícula inválida", Toast.LENGTH_SHORT).show()
        }
    }
}

