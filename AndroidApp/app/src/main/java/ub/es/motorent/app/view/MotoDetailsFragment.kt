package ub.es.motorent.app.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import ub.es.motorent.R
import ub.es.motorent.app.model.CommonFunctions
import ub.es.motorent.app.model.RentalDB

/**
 * A simple [Fragment] subclass.
 * Use the [MotoDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION")
class MotoDetailsFragment : Fragment() {
    private var license: String? = null
    private var id : Int? = null
    private var battery: Int? = null
    private var fromFragmentToActivity: FromFragmentToActivity ?= null
    private var motoLat: Double? = null
    private var motoLong: Double? = null
    private var rentalStatus: Int = 0
    lateinit var rentbtn: Button
    var inZone: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            license = it.getString(ARG_LICENSE)
            id = it.getInt(ARG_ID)
            battery = it.getInt(ARG_BATTERY)
            motoLat = it.getDouble(ARG_LAT)
            motoLong = it.getDouble(ARG_LONG)
            rentalStatus = it.getInt(ARG_RENTAL_STATUS)
        }

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                inZone = blockEndTrip()
                mainHandler.postDelayed(this, 50)
            }
        })
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
        rentbtn = view.findViewById<Button>(R.id.reservarBtn)
        val reportBtn= view.findViewById<Button>(R.id.reportBtn)
        reportBtn.setOnClickListener{
            startFragmentReport(this.id!!)
        }

        rentbtn.setOnClickListener {updateRentButton()}
        licenseText.text = license
        batteryText.text = battery.toString()

        updateRentButtonText(rentalStatus)
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
        private const val ARG_RENTAL_STATUS = "rental_status"
        private const val TAG = "MotoDetailFrag"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param license Parameter 1.
         * @param battery Parameter 2.
         * @return A new instance of fragment MotoDetailsFragment.
         */
        @JvmStatic
        fun newInstance(license: String, id: Int, battery: Int, coords: LatLng, rentalStatus: Int) =
            MotoDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LICENSE, license)
                    putInt(ARG_ID, id)
                    putInt(ARG_BATTERY, battery)
                    putDouble(ARG_LAT, coords.latitude)
                    putDouble(ARG_LONG, coords.longitude)
                    putInt(ARG_RENTAL_STATUS, rentalStatus)
                }
            }
    }

    fun updateRentButton(){
        val userId = CommonFunctions.loadUserInfoFromSharedPrefFragment(activity)?.id
        when (rentalStatus) {
            0 -> {
                updateRentButtonText(1)
                RentalDB.addRental(this.id, userId){rental ->
                    CommonFunctions.saveCurrentRentalInfoToSharedPref(rental, activity)
                }
            }
            1 -> {
                val rentalId = getRentalIdFromDB()
                updateRentButtonText(2)
                RentalDB.updateRentalById(rentalId, "False", null, null)
            }
            2 -> {
                if (inZone){
                    val rentalId = getRentalIdFromDB()
                    updateRentButtonText(0)
                    RentalDB.updateRentalById(rentalId, "True", motoLat?.toFloat(), motoLong?.toFloat())
                    CommonFunctions.saveCurrentRentalInfoToSharedPref(null, activity)
                } else {
                    Toast.makeText(activity, "No pots deixar la moto fora de l'àrea delimitada.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Log.e(TAG, "wrong rental status = $rentalStatus")
            }
        }
    }

    fun updateRentButtonText(status: Int){
        fromFragmentToActivity?.setRentalStatus(status)
        rentalStatus = status
        when (status) {
            0 -> {
                rentbtn.text = "Reservar"
                rentbtn.setBackgroundColor(resources.getColor(R.color.rentMoto))
            }
            1 -> {
                rentbtn.text = "Iniciar viatge"
                rentbtn.setBackgroundColor(resources.getColor(R.color.rentedMoto))
            }
            2 -> {
                rentbtn.text = "Finalitzar viatge"
                rentbtn.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }
        }
    }

    private fun getRentalIdFromDB() : Int? {
        val rentalId = CommonFunctions.loadCurrentRentalInfoFromSharedPref(activity)?.id
        Log.i(TAG, rentalId.toString())
        return rentalId
    }

    interface FromFragmentToActivity {
        fun onOptionChosenFromFragment(option: Int)
        fun hideLoginFragment()
        fun launchReport(id: Int)
        fun inZone(): Boolean
        fun setRentalStatus(status: Int)
    }

    private fun startFragmentReport(id:Int){
        fromFragmentToActivity?.launchReport(id)
    }
    fun blockEndTrip(): Boolean {
        return fromFragmentToActivity?.inZone() ?: false
    }
}
