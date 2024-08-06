package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CameraDoneActivity : AppCompatActivity() {

    private lateinit var photoView: ImageView
    private lateinit var storeButton: Button
    private lateinit var discardButton: Button

    private lateinit var cameraButton: ImageButton
    private lateinit var journalButton: ImageButton
    private lateinit var mapButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_done)

        photoView = findViewById(R.id.imageView)
        storeButton = findViewById(R.id.storeBtn)
        discardButton = findViewById(R.id.discardBtn)

        // Get the photo URI from the Intent
        val photoUri = intent.getStringExtra("photoUri")
        photoUri?.let {
            photoView.setImageURI(Uri.parse(it))
        }

        // Set click listener for the store button
        storeButton.setOnClickListener {
            // Redirect to StoreJournalActivity and pass the photo URI
            val intent = Intent(this, StoreJournalActivity::class.java).apply {
                putExtra("photoUri", photoUri) // Pass the photo URI to the StoreJournalActivity
            }
            startActivity(intent)
        }

        // Set click listener for the discard button
        discardButton.setOnClickListener {
            // Handle discarding the photo (e.g., return to previous screen or close activity)
            finish()
        }

        // FOOTER BUTTONS, this code must be present in every activity with a footer
        cameraButton = findViewById(R.id.cameraButton)
        cameraButton.setOnClickListener{
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
            finish()
        }

        journalButton = findViewById(R.id.journalButton)
        journalButton.setOnClickListener {
            val intent = Intent(this, JournalMainActivity::class.java)
            startActivity(intent)
        }

        mapButton = findViewById(R.id.mapButton)
        mapButton.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        // END OF FOOTER BUTTONS
    }
}
