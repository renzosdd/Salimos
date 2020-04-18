package com.app.salimos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_detail.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class EventDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //Habilita el boton para volver
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //Cambia el titulo de la barra de accion del activity por el nombre del evento
        supportActionBar!!.title = intent?.getStringExtra("name")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")
        val name = intent.getStringExtra("name")
        val dateStart = LocalDateTime.parse(intent.getStringExtra("date_start"))
        val dStart=dateStart.format(formatter)
        val dateEnd = LocalDateTime.parse(intent.getStringExtra("date_end"))
        val dEnd=dateEnd.format(formatter)
        val desc = intent?.getStringExtra("desc")
        val addr = intent?.getStringExtra("addr")
        val image = intent?.getStringExtra("image").toString()
        val webPage = intent?.getStringExtra("webPage")

        val eventDetailImage: ImageView = findViewById(R.id.event_img)
        val eventDetailWebPage:TextView = findViewById(R.id.event_detail_webpage)
        val eventDetailDateStart:TextView = findViewById(R.id.event_detail_date_start)
        val eventDetailDateEnd:TextView = findViewById(R.id.event_detail_date_end)
        val eventDetailDesc: TextView = findViewById(R.id.event_detail_desc)
        val eventDetailAddress: TextView = findViewById(R.id.event_detail_address)

        eventDetailImage.loadUrl(image)
        eventDetailWebPage.text= "Ir al sitio web del evento"
        eventDetailDateStart.text= "Inicio: "+ dStart.toString()
        eventDetailDateEnd.text= "Fin: "+ dEnd.toString()
        eventDetailDesc.text= desc
        eventDetailAddress.text=addr


        fab.setOnClickListener {
            val beginTime: Calendar = Calendar.getInstance()
            beginTime.set(dateStart.year, dateStart.monthValue,dateStart.dayOfMonth, dateStart.hour, dateStart.minute)

            val endTime: Calendar = Calendar.getInstance()
            endTime.set(dateEnd.year, dateEnd.monthValue,dateEnd.dayOfMonth, dateEnd.hour, dateEnd.minute)

            val intent: Intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,beginTime.timeInMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime.timeInMillis)
                .putExtra(CalendarContract.Events.TITLE, name)
                .putExtra(CalendarContract.Events.DESCRIPTION, desc)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, addr)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
            startActivity(intent)
        }

        rowDetailAddress.setOnClickListener{
            val intent = Intent(this@EventDetailActivity,EventMapActivity::class.java)
            intent.putExtra("lat", lat)
            intent.putExtra("long", long)
            intent.putExtra("name", name)
            startActivity(intent)
        }

        rowDetailWebPage.setOnClickListener{
            val url = webPage
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

    }

    //Funcion para cargar imagen desde URL utilizando la libreria Picasso
    private fun ImageView.loadUrl(url: String) {
        Picasso.with(context).load(url).into(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
