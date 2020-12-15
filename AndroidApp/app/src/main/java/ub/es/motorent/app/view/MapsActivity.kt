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
import ub.es.motorent.app.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    MotoDetailsFragment.FromFragmentToActivity, LocationListener {

    private lateinit var mMap: GoogleMap
    lateinit var locationManager: LocationManager
    lateinit var coordenadas: LatLng
    lateinit var markerUser : Marker
    var markersInDisplay: ArrayList<Marker> = ArrayList()
    private var rentalStatus : Int = 0
    private val holePolyg : ArrayList<LatLng> = ArrayList()
    lateinit private var hollowPolygon : Polygon

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
                chooseMotosOnMap()
                generateCoordLimitation()
                mainHandler.postDelayed(this, 1000)
            }
        })

    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
    }

    override fun onLocationChanged(location: Location?) {
        coordenadas = LatLng(location?.latitude as Double, location.longitude)
        markerUser.position = coordenadas
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        // TODO("Not yet implemented")
    }

    override fun onProviderEnabled(p0: String?) {
        // TODO("Not yet implemented")
    }

    override fun onProviderDisabled(p0: String?) {
        // TODO("Not yet implemented")
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

        hollowPolygon = mMap.addPolygon(PolygonOptions().add(LatLng(0.0, 0.0)))

        chooseMotosOnMap()

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
        generateCoordLimitation()
    }

    private fun loadMotosOnMap(motoList: MotoList){

        val markersToUpdate: ArrayList<MarkerOptions?> = ArrayList()
        val motosToUpdate: ArrayList<MotoInfo> = ArrayList()

        for ( i in 0 until markersInDisplay.size){ // drop motos
            if(motoList.motos.contains(markersInDisplay[i].tag)){
                motoList.motos.drop(i)
            }
        }
        for (moto in motoList.motos) { // add new motos ( only available )
            if (moto.available?.toBoolean() == true){
                val location = LatLng(moto.latitude.toDouble(), moto.longitude.toDouble())
                val marker = MarkerOptions().position(location).icon(BitmapDescriptorFactory.
                fromResource(R.drawable.motoicon))
                markersToUpdate.add(marker)
                motosToUpdate.add(moto)
            }
        }
        for (i in 0 until markersToUpdate.size){  // show new motos
            val tmpMarker = mMap.addMarker(markersToUpdate[i])
            tmpMarker.tag = motosToUpdate[i]
            markersInDisplay.add(tmpMarker)
        }
        for (i in 0 until markersInDisplay.size){ // update markers in display
            if (!motosToUpdate.contains(markersInDisplay[i].tag)){
                markersInDisplay[i].remove()
            }
        }
    }

    private fun loadMotoRentalOnMap(motoId: Int, motoList: MotoList){
        markersInDisplay.forEach { it.remove() }
        motoList.motos.forEach { moto ->
            if (moto.id == motoId){
                val location = LatLng(moto.latitude.toDouble(), moto.longitude.toDouble())
                val markerOp = MarkerOptions().position(location).icon(BitmapDescriptorFactory.
                fromResource(R.drawable.motoicon))
                val marker = mMap.addMarker(markerOp)
                marker.tag = moto
                markersInDisplay.add(marker)
            }
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        if (p0 != markerUser && p0 != null){
            val moto: MotoInfo = p0.tag as MotoInfo
            startFragmentMotoDetail(moto.license_number, moto.id, moto.battery, LatLng(moto.latitude.toDouble(),moto.longitude.toDouble()))
        }
        return false
    }

    private fun startFragmentMotoDetail(licence: String, id:Int, battery: Int, coords: LatLng){
        supportFragmentManager.popBackStack()
        val newFragment = MotoDetailsFragment.newInstance(licence, id, battery, coords, rentalStatus)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_moto_detail, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun chooseMotosOnMap(){
        MotoDB.getMotos { motoList: MotoList? ->
            if (motoList == null) {
                Log.w(TAG, "db returned motoList null")
            } else {
                CommonFunctions.getUserIdOnDB(this)?.let {
                    RentalDB.getActiveRentalByUserId(it) { rentalDB ->
                        val rentalSP = CommonFunctions.loadCurrentRentalInfoFromSharedPref(this)
                        if (rentalDB != rentalSP) {
                            doSthWithWrongRentalOnSP(rentalDB)
                        }
                        setCurrentRentalInfo(rentalDB)

                        if (rentalDB == null || !rentalDB.active){
                            loadMotosOnMap(motoList)
                        } else {


                            loadMotoRentalOnMap(rentalDB.moto_id, motoList)
                        }
                    }
                }
            }
        }
    }

    private fun doSthWithWrongRentalOnSP(rentalDB: RentalInfo?) {
        CommonFunctions.saveCurrentRentalInfoToSharedPref(rentalDB, this)
    }

     override fun setCurrentRentalInfo(rentalDB: RentalInfo?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_moto_detail)
         if (fragment != null){
             val frag = fragment as MotoDetailsFragment
             when {
                 rentalDB == null -> {
                     frag.updateRentButtonText(0)
                     rentalStatus = 0
                 }
                 (rentalDB?.book_hour != null) and (rentalDB?.finish_book_hour == null) -> {
                     frag.updateRentButtonText(1)
                     rentalStatus = 1
                 }
                 (rentalDB?.finish_book_hour != null) and (rentalDB != null) -> {
                     frag.updateRentButtonText(2)
                     rentalStatus = 2

                 }
             }
         }

    }

    override fun setRentalStatus(status: Int) {
        rentalStatus = status
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
        return if(this::coordenadas.isInitialized) PolyUtil.containsLocation(coordenadas, holePolyg, true) else false
    }

    override fun getStatus(): Int {
        return rentalStatus
    }
    companion object {
        private const val TAG = "MapsActivity"
    }

    fun generateCoordLimitation(){

        MapCoordDB.getAllMapCoords(){
                holePolyg.removeAll(holePolyg)
                hollowPolygon.remove()
                holePolyg.add(
                    LatLng(
                        it?.map_coords?.get(0)?.from_latitude!!.toDouble(),
                        it.map_coords.get(0).from_longitude.toDouble()
                    )
                )
                var futureLat = it.map_coords.get(0).to_latitude.toDouble()
                var futureLong = it.map_coords.get(0).to_longitude.toDouble()
                for (i in 0 until it.map_coords.size-2) {
                    for (j in 0 until it.map_coords.size) {
                        if (it.map_coords.get(j).from_latitude.toDouble() == futureLat && it.map_coords.get(j).from_longitude.toDouble() == futureLong) {
                            futureLat = it.map_coords.get(j).to_latitude.toDouble()
                            futureLong = it.map_coords.get(j).to_longitude.toDouble()
                            holePolyg.add(LatLng(it.map_coords.get(j).from_latitude.toDouble(), it.map_coords.get(j).from_longitude.toDouble()))
                        }
                    }
                }
                /*for (i in 0 until holePolyg.size){
                    Log.i("LATITUD", holePolyg.get(i).latitude.toString())
                    Log.i("LONGITUD", holePolyg.get(i).longitude.toString())
                }*/

                hollowPolygon = mMap.addPolygon(
                    PolygonOptions().add(
                        LatLng(58.950017, -16.157126),
                        LatLng(58.950017, 26.123910),
                        LatLng(25.943059, 26.123910),
                        LatLng(25.943059, -16.157126)
                    ).geodesic(false).addHole(holePolyg)
                        .fillColor(Color.GRAY)
                        .strokeWidth(5.0f)
                        .fillColor(0x55686868)
                        .geodesic(false)
                )
        }

    }
}

