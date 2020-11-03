package ub.es.motorent.app.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
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
    override fun onBackPressed() {
        //DO NOTHING
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
            LatLng(41.346546, 2.139268))

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
}