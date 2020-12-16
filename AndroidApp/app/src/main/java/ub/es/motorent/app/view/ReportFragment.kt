package ub.es.motorent.app.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import ub.es.motorent.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var id : Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(ARG_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val check1 = view.findViewById<CheckBox>(R.id.noFuncionaCheck)
        val check2 = view.findViewById<CheckBox>(R.id.noEstaCheck)
        val check3 = view.findViewById<CheckBox>(R.id.compExtranyCheck)
        val check4 = view.findViewById<CheckBox>(R.id.malEstaCheck)
        val reportText = view.findViewById<EditText>(R.id.textResum)
        val sendReport = view.findViewById<Button>(R.id.sendReportBtn)

        sendReport.setOnClickListener {
            var missatge = ""
            if (check1.isChecked == true){
                missatge += (check1.text.toString() + ", ")
            }
            if(check2.isChecked == true){
                missatge += (check2.text.toString() + ", ")
            }
            if(check3.isChecked == true){
                missatge += (check3.text.toString() + ", ")
            }
            if(check4.isChecked == true){
                missatge += (check4.text.toString() + ", ")
            }
            if(reportText.text.toString() != ""){
                missatge += (reportText.text.toString() + ".")
            }

            //A FALTA DE UNA CRIDA A BACK, CRIDA AMB ID DE LA MOTO I AUTH.USER

            Log.d("ReportFragment", missatge)
            Toast.makeText(context, "Incidència reportada correctament", Toast.LENGTH_LONG).show()
            activity?.supportFragmentManager?.popBackStack()
        }

    }


    companion object {
        private const val ARG_ID = "id"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance( id: Int) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putInt(ReportFragment.ARG_ID, id)
                }
            }

        interface FromFragmentToActivity {
            fun onOptionChosenFromFragment(option: Int)
            fun hideLoginFragment()
        }
    }
}