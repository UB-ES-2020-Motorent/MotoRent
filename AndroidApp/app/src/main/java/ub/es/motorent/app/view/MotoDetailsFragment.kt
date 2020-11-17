package ub.es.motorent.app.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ub.es.motorent.R


/**
 * A simple [Fragment] subclass.
 * Use the [MotoDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MotoDetailsFragment : Fragment() {
    private var license: String? = null
    private var battery: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            license = it.getString(ARG_LICENSE)
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
        licenseText.text = license
        batteryText.text = battery.toString()
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_LICENSE = "license"
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
        fun newInstance(license: String, battery: Int) =
            MotoDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LICENSE, license)
                    putInt(ARG_BATTERY, battery)
                }
            }
    }

    interface FromFragmentToActivity {
        fun onOptionChosenFromFragment(option: Int)
    }

}