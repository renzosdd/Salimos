package com.app.salimos

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_events_create.*
import kotlinx.android.synthetic.main.activity_register.*


class EventCreateActivity : AppCompatActivity() {

    private lateinit var nameEvent: EditText
    private lateinit var dirEvent: EditText
    private lateinit var descEvent: EditText
    private lateinit var catEvent: EditText
    private lateinit var imageEvent: EditText
    private lateinit var startEvent: EditText
    private lateinit var endEvent: EditText
    private lateinit var latEvent: EditText
    private lateinit var longEvent: EditText
    private lateinit var webEvent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_create)

        nameEvent = findViewById(R.id.txtNameEvent)
        dirEvent= findViewById(R.id.txtDirEvent)
        descEvent = findViewById(R.id.txtDescEvent)
        catEvent = findViewById(R.id.txtCatEvent)
        imageEvent = findViewById(R.id.txtImageEvent)
        startEvent = findViewById(R.id.txtStartEvent)
        endEvent = findViewById(R.id.txtEndEvent)
        latEvent = findViewById(R.id.txtLatEvent)
        longEvent = findViewById(R.id.txtLongEvent)
        webEvent = findViewById(R.id.txtWebEvent)


        img_pick_btn.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
    }



    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            println(image_view.setImageURI(data?.data))
        }
    }

    fun createEvent(view: View) {
            newEvent(
                nameEvent.text.toString(),
                descEvent.text.toString(),
                imageEvent.text.toString(),
                latEvent.text.toString(),
                longEvent.text.toString(),
                dirEvent.text.toString(),
                catEvent.text.toString(),
                startEvent.text.toString(),
                endEvent.text.toString(),
                webEvent.text.toString()
            )
            onBackPressed()
    }
}
//Cambio para ver si refleja