package com.app.salimos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class EventMapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {

        val name = intent.getStringExtra("name")
        //Habilita el boton para volver
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //Cambia el titulo de la barra de accion del activity por el nombre del evento
        supportActionBar!!.title = name

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")
        val marker = intent?.getStringExtra("name").toString()
        val myPlace = LatLng(lat.toDouble(), long.toDouble())

        map.addMarker(MarkerOptions().position(myPlace).title(marker))
        map.moveCamera(CameraUpdateFactory.newLatLng(myPlace))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 16.0f))
    }

    override fun onMarkerClick(p0: Marker?) = false

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
