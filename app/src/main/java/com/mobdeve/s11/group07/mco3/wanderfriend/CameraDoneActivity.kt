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
    }
}
