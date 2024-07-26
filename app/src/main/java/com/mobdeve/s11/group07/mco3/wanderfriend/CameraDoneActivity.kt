package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CameraDoneActivity : AppCompatActivity() {

    private lateinit var photoView: ImageView
    private lateinit var storeButton: Button
    private lateinit var discardButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_done)

        photoView = findViewById(R.id.imageView)
        storeButton = findViewById(R.id.storeBtn)
        discardButton = findViewById(R.id.discardBtn)

        // Assuming the photo is passed as a URI string in the intent
        val photoUri = intent.getStringExtra("photoUri")
        photoUri?.let {
            photoView.setImageURI(Uri.parse(it))
        }

        storeButton.setOnClickListener {
            // Handle storing the photo in a journal
        }

        discardButton.setOnClickListener {
            // Handle discarding the photo
        }
    }
}
