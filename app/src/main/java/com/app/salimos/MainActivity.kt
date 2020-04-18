package com.app.salimos

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var myRef: DatabaseReference
    private lateinit var search: SearchView
    private lateinit var textViewError:TextView

    var myEvents = mutableListOf<Event>()
    var filteredEvents = myEvents

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //Cargo la referenica a la BD
        loadDatabase()
        chipControl()
    }
//Funcion que carga la base de datos
    private fun loadDatabase(){
        myRef = FirebaseDatabase.getInstance().reference.child("Eventos")
        var dist:Double=0.0
        //Cargo los datos de la BD en la variable de tipo Events.events en myEvents
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myEvents.clear()
                if (dataSnapshot.exists()) {
                    dist=0.0
                    val children = dataSnapshot.children
                    children.forEach {
                        val eventHash = it.value as HashMap<*, *>
                        val ev = Event(
                            eventHash["name"].toString(),
                            eventHash["desc"].toString(),
                            eventHash["image"].toString(),
                            eventHash["lat"].toString(),
                            eventHash["long"].toString(),
                            eventHash["address"].toString(),
                            eventHash["category"].toString(),
                            eventHash["date_start"].toString(),
                            eventHash["date_end"].toString(),
                            eventHash["webPage"].toString(),
                            dist
                        )
                        myEvents.add(ev)
                    }

                    filteredEvents = filterOutdatedEvents(myEvents) as MutableList<Event>
                    reloadRecycler(filteredEvents)
                }
                return
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error al cargar la base de datos", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun chipControl(){
        //Control del filtro con el componente Chip de Material
        chip0.isChecked=true
        for (index in 0 until chipGroup.childCount) {
            var queryEvents: MutableList<Event>
            val chip: Chip = chipGroup.getChildAt(index) as Chip

            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if(chip.text=="Todos"){
                        reloadRecycler(filterOutdatedEvents(myEvents))
                    }else{
                        queryEvents=filterByCategory(filteredEvents,chip.text as String) as MutableList<Event>
                        reloadRecycler(queryEvents)}
                }
            }
        }
    }

    // Funcion para crear el menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        search = MenuItemCompat.getActionView(searchItem) as SearchView
        search.setOnCloseListener { true }

        val searchPlate = search.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        searchPlate.hint = "Busqueda"
        val searchPlateView: View =
            search.findViewById(androidx.appcompat.R.id.search_plate)
        searchPlateView.setBackgroundColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )

        //Manejador de la busqueda
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                val queryEvents: MutableList<Event>
                if (query != null) {
                    queryEvents = filterByName(filteredEvents,query) as MutableList<Event>
                    textViewError.visibility=View.GONE
                    if (queryEvents.isEmpty()) {
                        textViewError.visibility=View.VISIBLE
                        filteredEvents = filterOutdatedEvents(myEvents) as MutableList<Event>
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var queryEvents=filteredEvents
                if (newText != null) {
                    queryEvents = filterByName(filteredEvents,newText) as MutableList<Event>
                    textViewError = findViewById(R.id.NoItemInSearch)
                    textViewError.visibility=View.GONE
                    if (filteredEvents.isEmpty()) {
                        textViewError.visibility=View.VISIBLE
                    }
                }
                reloadRecycler(queryEvents)
                return false
            }
        })

        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as SearchManager
        search.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                return true
            }
            R.id.action_signOut -> {
                signOut()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Funcion que maneja cuando se realiza click en algun evento y lanza el activity EventDetailActivity
    private fun eventItemClicked(event: Event) {
        val intent = Intent(this@MainActivity, EventDetailActivity::class.java)
        intent.putExtra("name", event.name)
        intent.putExtra("desc", event.desc)
        intent.putExtra("addr", event.address)
        intent.putExtra("date_start", event.date_start)
        intent.putExtra("date_end", event.date_end)
        intent.putExtra("lat", event.lat)
        intent.putExtra("long", event.long)
        intent.putExtra("image", event.image)
        intent.putExtra("category", event.category)
        intent.putExtra("webPage",event.webPage)
        startActivity(intent)

    }

    //Funcion que maneja el boton atras en el campo de busqueda
    override fun onBackPressed() {
        if (!search.isIconified) {
            search.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }

    //Recarga el RecyclerView de los eventos
    fun reloadRecycler(ev: List<Event>) {
        list_recycler_view.adapter?.notifyDataSetChanged()
        list_recycler_view.apply {

            layoutManager = this?.let { LinearLayoutManager(context) }
            list_recycler_view!!.itemAnimator = DefaultItemAnimator()
            adapter =
                ListEventsAdapter(ev) { events: Event ->
                    eventItemClicked(events)
                }
        }
    }

    //Filtra los eventos que ya se vencieron
    private fun filterOutdatedEvents(ev: List<Event>): List<Event> {
        val list = ev.filter { LocalDateTime.parse(it.date_start).isAfter(LocalDateTime.now()) }
        //return list.sortWith(compareBy({it.dist}, {it.date_start})) as MutableList<Event>
        //return list.sortedBy { it.date_start}
        return list.sortedWith(compareBy({ it.dist }, { it.date_start }))

    }

    //Filtro de eventos por nombre (lo usa el buscador)
    fun filterByName(list:List<Event>,filter: String): List<Event> {
        return filterOutdatedEvents(list).filter { it.name.contains(filter, true) }
    }

    //Filtro de eventos por nombre (lo usa el buscador)
    fun filterByCategory(list:List<Event>,filter: String): List<Event> {
        return filterOutdatedEvents(list).filter { it.category.contains(filter, true) }
    }


    //Funcion para cierre de sesion.
    private fun signOut() {
        startActivity(LoginActivity.getLaunchIntent(this))
        FirebaseAuth.getInstance().signOut();
    }


    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}

