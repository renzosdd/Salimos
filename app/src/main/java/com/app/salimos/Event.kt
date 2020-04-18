package com.app.salimos

import android.location.Location
import com.google.firebase.database.*
import java.util.HashMap




data class Event(val name:String, val desc:String, val image:String, val lat:String, val long:String,val address:String,val category:String, val date_start: String, val date_end:String,val webPage:String,val dist:Double)

fun newEvent(
    name: String,
    desc: String,
    image: String,
    lat: String,
    long: String,
    address: String,
    category: String,
    date_start: String,
    date_end: String,
    webPage: String
) {
    val myRef:DatabaseReference = FirebaseDatabase.getInstance().reference.child("Eventos")
    val event = Event(name, desc, image, lat, long, address, category, date_start, date_end,webPage,dist = 0.0)
    myRef.push().setValue(event)
}

/*

fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
    val results = FloatArray(1)
    Location.distanceBetween(lat1, lng1, lat2, lng2, results)
    // distance in meter
    return results[0]
}


*/