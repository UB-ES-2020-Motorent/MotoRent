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
import ub.es.motorent.app.model.*
import java.text.SimpleDateFormat

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

        val userId = CommonFunctions.loadUserInfoFromSharedPrefFragment(activity)?.id
        updateStatus(userId)

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


    fun updateStatus(userId: Int?){
        RentalDB.getActiveRentalByUserId(userId!!){rentalAct ->
            when {
                rentalAct == null -> {
                    rentalStatus = 0
                }
                (rentalAct?.book_hour != null) and (rentalAct?.finish_book_hour == null) -> {
                    rentalStatus = 1
                }
                (rentalAct?.finish_book_hour != null) and (rentalAct != null) -> {
                    rentalStatus = 2
                }

            }
        }
    }



    fun updateRentButton(){
        val userId = CommonFunctions.loadUserInfoFromSharedPrefFragment(activity)?.id
        UserDB.getUserByIdOrGoogleToken(userId){
            val userBankData = it?.id_bank_data
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

                        if (rentalId != null) {
                            if(userBankData != null){
                                getImportRental(rentalId){ listRet ->
                                    PaymentsDB.addPayment(rentalId!!,userBankData!!, listRet[0]!!.toFloat(), listRet[1] ){it3->
                                        CommonFunctions.saveCurrentPaymentInfoToSharedPref(it3, activity)
                                    }
                                }
                                Toast.makeText(activity, "Viatje finalitzat. S'ha carregat l'import a la teva targeta per defecte.", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(activity, "Viatje finalitzat. Pagament pendent a falta d'associar targeta.", Toast.LENGTH_LONG).show()
                            }

                        }

                    } else {
                        Toast.makeText(activity, "No pots deixar la moto fora de l'àrea delimitada.", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    Log.e(TAG, "wrong rental status = $rentalStatus")
                }
            }
        }


    }

    fun updateRentButtonText(status: Int){
        fromFragmentToActivity?.setRentalStatus(status)
        rentalStatus = status
        when (status) {
            0 -> {
                rentbtn.text = "Reservar"
                rentbtn.setBackgroundResource(R.drawable.rental_buttons_bg1)
            }
            1 -> {
                rentbtn.text = "Iniciar viatge"
                rentbtn.setBackgroundResource(R.drawable.rental_buttons_bg2)
            }
            2 -> {
                rentbtn.text = "Finalitzar viatge"
                rentbtn.setBackgroundResource(R.drawable.rental_buttons_bg3)
            }
        }
    }

    private fun getRentalIdFromDB() : Int? {
        val rentalId = CommonFunctions.loadCurrentRentalInfoFromSharedPref(activity)?.id
        Log.i(TAG, rentalId.toString())
        return rentalId
    }


    fun getImportRental(rentalId : Int, onResult: (List<String?>) -> Unit ) {
        var totalImport : Float? = 0.00F

        RentalDB.getRentalById(rentalId){rentalImport->
            var listReturn = listOf<String?>(null)
            val startTravel = rentalImport?.finish_book_hour
            val finishTravel = rentalImport?.finish_rental_hour
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")

            val startDate = formatter.parse(startTravel)
            val endDate = formatter.parse(finishTravel)

            if ((startDate != null) and (endDate != null)){
                val diff: Long = endDate.time - startDate.time
                val seconds = (diff / 1000).toFloat()
                var minutes = (seconds / 60)

                if (minutes < 1.00F){
                    minutes = 1.00F
                }

                totalImport = minutes * 0.22F

                listReturn = listOf<String?>(totalImport.toString(), finishTravel)

                // "finish_book_hour": "2020-12-02T15:10:02.558473"
                // "finish_rental_hour": "2020-12-02T15:10:50.782567"
                onResult(listReturn)
            }
            
        }

    }





    interface FromFragmentToActivity {
        fun onOptionChosenFromFragment(option: Int)
        fun hideLoginFragment()
        fun launchReport(id: Int)
        fun inZone(): Boolean
        fun setRentalStatus(status: Int)
        fun setCurrentRentalInfo(rentalDB: RentalInfo)
        fun getStatus(): Int
    }

    private fun startFragmentReport(id:Int){
        fromFragmentToActivity?.launchReport(id)
    }
    fun blockEndTrip(): Boolean {
        return fromFragmentToActivity?.inZone() ?: false
    }
}
