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
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.google.maps.android.PolyUtil
import ub.es.motorent.R
import ub.es.motorent.app.model.CommonFunctions
import ub.es.motorent.app.model.MotoDB
import ub.es.motorent.app.model.MotoInfo
import ub.es.motorent.app.model.MotoList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    MotoDetailsFragment.FromFragmentToActivity, LocationListener {

    private lateinit var mMap: GoogleMap
    lateinit var locationManager: LocationManager
    lateinit var coordenadas: LatLng
    lateinit var markerUser : Marker
    var markers_in_display: ArrayList<Marker> = ArrayList()
    val hole : ArrayList<LatLng> = ArrayList()

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

        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                getMotosFromMap()
                mainHandler.postDelayed(this, 5000)
            }
        })

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

        getMotosFromMap()

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


        coordenadas = if (currentLocation == null) {
            LatLng(41.387185, 2.163077)
        } else{
            LatLng(currentLocation.latitude, currentLocation.longitude)
        }

        CommonFunctions.saveCurrentUserCoordsToSharedPref(coordenadas, this)

        markerUser = mMap.addMarker(MarkerOptions().position(coordenadas).icon(BitmapDescriptorFactory.fromResource(R.drawable.you_are_here_resized)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 17.0f))
        val holePolyg = listOf(
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
        hole.addAll(holePolyg)

        val hollowPolygon = mMap.addPolygon(
            PolygonOptions().add(
                LatLng(58.950017, -16.157126),
                LatLng(58.950017, 26.123910),
                LatLng(25.943059, 26.123910),
                LatLng(25.943059, -16.157126)
            ).addHole(holePolyg)
                .fillColor(Color.GRAY)
                .strokeWidth(5.0f)
                .fillColor(0x55686868)
                .geodesic(true)
        )
    }

    private fun loadMotosOnMap(motoList: MotoList?){

        val markers_to_update: ArrayList<MarkerOptions?> = ArrayList()
        val motos_to_update: ArrayList<MotoInfo> = ArrayList()

        for ( i in 0 until markers_in_display.size){
            if(motoList?.motos!!.contains(markers_in_display.get(i).tag)){
                motoList.motos.drop(i)
            }
        }

        if (motoList != null) {
            for (moto in motoList.motos) {
                if (moto.available?.toBoolean() == true){
                    val location = LatLng(moto.longitude.toDouble(), moto.latitude.toDouble())
                    val marker = MarkerOptions().position(location).icon(BitmapDescriptorFactory.
                                                fromResource(R.drawable.motoicon))
                    markers_to_update.add(marker)
                    motos_to_update.add(moto)
                }
            }
            for (i in 0 until markers_to_update.size){
                val tmp_marker = mMap.addMarker(markers_to_update.get(i))
                tmp_marker.tag = motos_to_update.get(i)
                markers_in_display.add(tmp_marker)
            }
            for (i in 0 until markers_in_display.size){
                if (!motos_to_update.contains(markers_in_display.get(i).tag)){
                    markers_in_display.get(i).remove()
                }
            }
        }
    }


    override fun onMarkerClick(p0: Marker?): Boolean {
        if (p0 != markerUser && p0 != null){
            val moto: MotoInfo = p0.tag as MotoInfo
            startFragmentMotoDetail(moto.license_number, moto.id!!.toInt(), moto.battery, LatLng(moto.latitude.toDouble(),moto.longitude.toDouble()))
        }
        return false
    }

    private fun startFragmentMotoDetail(licence: String, id:Int, battery: Int, coords: LatLng){
        supportFragmentManager.popBackStack()
        val newFragment = MotoDetailsFragment.newInstance(licence, id, battery, coords)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_moto_detail, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun getMotosFromMap(){
        MotoDB.getMotos {
            loadMotosOnMap(it)
        }
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

    override fun inZone(): Boolean {
        return PolyUtil.containsLocation(coordenadas, hole, true)
    }


}

