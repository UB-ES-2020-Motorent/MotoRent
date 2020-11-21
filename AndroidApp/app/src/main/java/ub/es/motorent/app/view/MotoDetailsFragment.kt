package ub.es.motorent.app.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.android.synthetic.*
import ub.es.motorent.R
import ub.es.motorent.app.model.*


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
    private var moto_lat: Double? = null
    private var moto_long: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            license = it.getString(ARG_LICENSE)
            id = it.getInt(ARG_ID)
            battery = it.getInt(ARG_BATTERY)
            moto_lat = it.getDouble(ARG_LAT)
            moto_long = it.getDouble(ARG_LONG)
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
            throw RuntimeException(requireContext().toString() + " debe implementar FromFragmentToActivity")
        }
    }

    override fun onDetach() {
        super.onDetach()
        fromFragmentToActivity = null
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_LICENSE = "license"
        private const val ARG_ID = "id"
        private const val ARG_BATTERY = "battery"
        private const val ARG_LAT = "moto_lat"
        private const val ARG_LONG = "moto_long"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param license Parameter 1.
         * @param battery Parameter 2.
         * @return A new instance of fragment MotoDetailsFragment.
         */
        @JvmStatic
        fun newInstance(license: String, id: Int, battery: Int, coords: LatLng) =
            MotoDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LICENSE, license)
                    putInt(ARG_ID, id)
                    putInt(ARG_BATTERY, battery)
                    putDouble(ARG_LAT, coords.latitude)
                    putDouble(ARG_LONG, coords.longitude)
                }
            }
    }

    fun updateRentButton(rentbtn:Button){
        val user_id = CommonFunctions.loadUserInfoFromSharedPrefFragment(this.activity)?.id
        if(rentbtn.text.equals("Reservar")){
            rentbtn.setText("Iniciar viatge")
            rentbtn.setBackgroundColor(getResources().getColor(R.color.rentedMoto))
            RentalDB.addRental(this.id,user_id){
                CommonFunctions.saveCurrentRentalInfoToSharedPref(it!!,this.activity)
            }
        } else if(rentbtn.text.equals("Iniciar viatge")){
            val rental_id = CommonFunctions.loadCurrentRentalInfoToSharedPref(this.activity)?.id
            Log.i("MOMENTOLOLAZO",rental_id.toString())
            rentbtn.setText("Finalitzar viatge")
            rentbtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary))
            RentalDB.updateRentalById(rental_id,"False", null, null)
        } else {
            val rental_id = CommonFunctions.loadCurrentRentalInfoToSharedPref(this.activity)?.id
            Log.i("XXXXXXXXXXXXXXDDDD",rental_id.toString())
            rentbtn.setText("Reservar")
            rentbtn.setBackgroundColor(getResources().getColor(R.color.rentMoto))
            RentalDB.updateRentalById(rental_id,"True", moto_lat?.toFloat(), moto_long?.toFloat())
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