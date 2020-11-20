package ub.es.motorent.app.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import ub.es.motorent.R

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    MotoDetailsFragment.FromFragmentToActivity, LocationListener {

    private lateinit var mMap: GoogleMap
    lateinit var locationManager: LocationManager
    lateinit var coordenadas: LatLng
    lateinit var markerUser : Marker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportActionBar?.hide()

        val settingBtn : ImageButton = findViewById(R.id.settingBtn)

        settingBtn.setOnClickListener {
            val intentI = Intent(this, SettingsActivity::class.java)
            startActivity(intentI)
        }


        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {}
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1.toFloat(), this)
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
    }

    override fun onLocationChanged(location: Location?) {
        coordenadas = LatLng(location?.latitude as Double, location?.longitude)
        markerUser.position = coordenadas
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onProviderEnabled(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onProviderDisabled(p0: String?) {
        TODO("Not yet implemented")
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        initMotosOnMap()
        mMap.setOnMarkerClickListener { onMarkerClick(it) }

        mMap.setOnMapClickListener {
            hideLoginFragment()
        }

        val currentLocation = if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            val providers: List<String> = locationManager.getProviders(true)
            var bestLocation : Location? = null
            for (provider in providers){
                val l = locationManager.getLastKnownLocation(provider)
                if (l == null) continue
                if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                    bestLocation = l
                }
            }
            bestLocation
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, permissions,0)
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }

        coordenadas = LatLng(currentLocation?.latitude as Double, currentLocation?.longitude)

        markerUser = mMap.addMarker(MarkerOptions().position(coordenadas).icon(BitmapDescriptorFactory.fromResource(R.drawable.you_are_here_resized)))

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 17.0f))
        val hole = listOf(
            LatLng(41.346835, 2.139348),
            LatLng(41.355486, 2.131968),
            LatLng(41.362218, 2.135069),
            LatLng(41.377781, 2.121317),
            LatLng(41.375642, 2.117680),
            LatLng(41.375865, 2.109544),
            LatLng(41.381575, 2.107604),
            LatLng(41.384287, 2.111785),
            LatLng(41.387389, 2.106061),
            LatLng(41.400614, 2.113133),
            LatLng(41.415180, 2.131959),
            LatLng(41.419323, 2.127931),
            LatLng(41.421286, 2.134218),
            LatLng(41.425994, 2.135808),
            LatLng(41.427717, 2.128503),
            LatLng(41.430291, 2.128299),
            LatLng(41.430284, 2.139204),
            LatLng(41.444066, 2.143382),
            LatLng(41.442012, 2.153787),
            LatLng(41.449161, 2.172412),
            LatLng(41.460230, 2.167192),
            LatLng(41.469531, 2.183957),
            LatLng(41.433781, 2.211112),
            LatLng(41.408529, 2.224949),
            LatLng(41.375045, 2.190526),
            LatLng(41.374874, 2.188226),
            LatLng(41.381653, 2.184492),
            LatLng(41.352369, 2.162530),
            LatLng(41.346546, 2.139268)
        )

        val hollowPolygon = mMap.addPolygon(
            PolygonOptions().add(
                LatLng(58.950017, -16.157126),
                LatLng(58.950017, 26.123910),
                LatLng(25.943059, 26.123910),
                LatLng(25.943059, -16.157126)
            ).addHole(hole)
                .fillColor(Color.GRAY)
                .strokeWidth(5.0f)
                .fillColor(0x55686868)
                .geodesic(true)
        )
    }

    private fun initMotosOnMap(){
        val motoList = listOf(
            LatLng(41.3818, 2.1685),
            LatLng(41.382093, 2.131414),
            LatLng(41.402959, 2.174802),
            LatLng(41.413352, 2.202810),
            LatLng(41.437218, 2.180026),
            LatLng(41.411589, 2.152448)
        )
        val licenseList = listOf(
            "9980 BKB",
            "7528 CDT",
            "5369 PLN",
            "2491 LKC",
            "6317 PPB",
            "7018 KKB"
        )

        for (i in motoList.indices) {
            mMap.addMarker(MarkerOptions().position(motoList[i]).icon(BitmapDescriptorFactory.fromResource(R.drawable.motoicon)).title(licenseList[i]))
        }
    }


    override fun onMarkerClick(p0: Marker?): Boolean {
        when (p0!!.title) {
            "9980 BKB" -> {
                startFragmentMotoDetail(p0!!.title, 1, 94)
            }
            "7528 CDT" -> {
                startFragmentMotoDetail(p0!!.title, 1, 81)
            }
            "5369 PLN" -> {
                startFragmentMotoDetail(p0!!.title, 1, 76)
            }
            "2491 LKC" -> {
                startFragmentMotoDetail(p0!!.title, 1, 89)
            }
            "6317 PPB" -> {
                startFragmentMotoDetail(p0!!.title, 1, 87)
            }
            else -> {
                startFragmentMotoDetail(p0!!.title, 1, 97)
            }
        }
        return false
    }

    private fun startFragmentMotoDetail(licence: String, id:Int, battery: Int){
        supportFragmentManager.popBackStack()
        val newFragment = MotoDetailsFragment.newInstance(licence, id, battery)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_moto_detail, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onOptionChosenFromFragment(option: Int) {
        TODO("Not yet implemented")
    }

    override fun hideLoginFragment() {
        this.fragment_moto_detail.removeAllViews()
    }

    override fun launchReport(id: Int) {
        supportFragmentManager.popBackStack()
        val newFragment = ReportFragment.newInstance(id)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_moto_detail, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

