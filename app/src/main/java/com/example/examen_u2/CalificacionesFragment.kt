import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.examen_u2.R

class CalificacionesFragment : Fragment() {

    companion object {
        fun newInstance(calificaciones: String): CalificacionesFragment {
            val fragment = CalificacionesFragment()
            val bundle = Bundle()
            bundle.putString("calificaciones", calificaciones) // Aquí pasamos el string con las calificaciones
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragmento
        val rootView = inflater.inflate(R.layout.fragment_calificaciones, container, false)

        // Obtener las calificaciones del Bundle
        val calificaciones = arguments?.getString("calificaciones")
        val textView = rootView.findViewById<TextView>(R.id.textViewCalificaciones)

        if (calificaciones != null) {
            textView.text = calificaciones
        } else {
            textView.text = "No se han encontrado calificaciones."
        }

        return rootView
    }
}
