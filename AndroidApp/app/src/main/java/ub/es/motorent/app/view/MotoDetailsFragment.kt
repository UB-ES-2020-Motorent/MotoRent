package ub.es.motorent.app.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.*
import ub.es.motorent.R
import ub.es.motorent.app.model.MotoDB
import ub.es.motorent.app.model.RentalDB
import ub.es.motorent.app.model.UserDB


/**
 * A simple [Fragment] subclass.
 * Use the [MotoDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MotoDetailsFragment : Fragment() {
    private var license: String? = null
    private var id : Int? = null
    private var battery: Int? = null
    private var fromFragmentToActivity: FromFragmentToActivity ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            license = it.getString(ARG_LICENSE)
            id = it.getInt(ARG_ID)
            battery = it.getInt(ARG_BATTERY)

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_moto_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val licenseText = view.findViewById<TextView>(R.id.moto_txt_matricula_value)
        val batteryText = view.findViewById<TextView>(R.id.moto_txt_battery_value)
        val rentbtn = view.findViewById<Button>(R.id.reservarBtn)
        val reportBtn= view.findViewById<Button>(R.id.reportBtn)

        reportBtn.setOnClickListener{
            startFragmentReport(this.id!!)
        }

        rentbtn.setOnClickListener({updateRentButton(rentbtn)})
        licenseText.text = license
        batteryText.text = battery.toString()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FromFragmentToActivity){
            fromFragmentToActivity = context
        }else{
            throw RuntimeException(context!!.toString() + " debe implementar FromFragmentToActivity")
        }
    }


    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_LICENSE = "license"
        private const val ARG_ID = "id"
        private const val ARG_BATTERY = "battery"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param license Parameter 1.
         * @param battery Parameter 2.
         * @return A new instance of fragment MotoDetailsFragment.
         */
        @JvmStatic
        fun newInstance(license: String, id: Int, battery: Int) =
            MotoDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LICENSE, license)
                    putInt(ARG_ID, id)
                    putInt(ARG_BATTERY, battery)
                }
            }
    }

    fun updateRentButton(rentbtn:Button){
        if(rentbtn.text.equals("Reservar")){
            rentbtn.setText("Iniciar viatge")
            rentbtn.setBackgroundColor(getResources().getColor(R.color.rentedMoto))
            //TODO POST PER INICIAR RESERVA
        } else if(rentbtn.text.equals("Iniciar viatge")){
            rentbtn.setText("Finalitzar viatge")
            rentbtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary))
            //TODO PUT PER INICIAR VIATGE
        } else {
            rentbtn.setText("Reservar")
            rentbtn.setBackgroundColor(getResources().getColor(R.color.rentMoto))
            //TODO PUT PER ACABAR VIATGE
        }
    }

    interface FromFragmentToActivity {
        fun onOptionChosenFromFragment(option: Int)
        fun hideLoginFragment()
        fun launchReport(id: Int)

    }

    private fun startFragmentReport(id:Int){
        fromFragmentToActivity?.launchReport(id)
    }

}