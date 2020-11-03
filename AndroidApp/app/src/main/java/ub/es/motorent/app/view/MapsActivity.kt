package ub.es.motorent.app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ub.es.motorent.R

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        val coordenadas = LatLng(41.3818, 2.1685)
        val coordMoto1 = LatLng(41.3818, 2.1685)
        val coordMoto2 = LatLng(41.382093, 2.131414)
        val coordMoto3 = LatLng(41.402959, 2.174802)
        val coordMoto4 = LatLng(41.413352, 2.202810)
        val coordMoto5 = LatLng(41.437218, 2.180026)
        val coordMoto6 = LatLng(41.411589, 2.152448)
        mMap.addMarker(MarkerOptions().position(coordMoto1).icon(BitmapDescriptorFactory.fromResource(R.drawable.motoicon)))
        mMap.addMarker(MarkerOptions().position(coordMoto2).icon(BitmapDescriptorFactory.fromResource(R.drawable.motoicon)))
        mMap.addMarker(MarkerOptions().position(coordMoto3).icon(BitmapDescriptorFactory.fromResource(R.drawable.motoicon)))
        mMap.addMarker(MarkerOptions().position(coordMoto4).icon(BitmapDescriptorFactory.fromResource(R.drawable.motoicon)))
        mMap.addMarker(MarkerOptions().position(coordMoto5).icon(BitmapDescriptorFactory.fromResource(R.drawable.motoicon)))
        mMap.addMarker(MarkerOptions().position(coordMoto6).icon(BitmapDescriptorFactory.fromResource(R.drawable.motoicon)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 17.0f))
    }
}