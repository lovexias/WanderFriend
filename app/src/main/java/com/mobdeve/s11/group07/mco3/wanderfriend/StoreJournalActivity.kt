package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StoreJournalActivity : AppCompatActivity() {

    private lateinit var backBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var journalRecyclerView: RecyclerView

    private lateinit var cameraButton: ImageButton
    private lateinit var journalButton: ImageButton
    private lateinit var mapButton: ImageButton

    private lateinit var countryAdapter: CountryAdapter
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_store_journal)

        backBtn = findViewById(R.id.backBtn)
        cancelBtn = findViewById(R.id.cancelBtn)
        journalRecyclerView = findViewById(R.id.journalRecyclerView)
        journalRecyclerView.layoutManager = LinearLayoutManager(this)

        dbHelper = UserDatabaseHelper(this)

        // Retrieve the photo URI passed from CameraDoneActivity
        val photoUri = intent.getStringExtra("photoUri")
        Log.d("StoreJournalActivity", "Received photoUri: $photoUri")  // Debug log

        // Fetch countries from the Country table
        val countries = dbHelper.getAllCountries()
        Log.d("StoreJournalActivity", "Retrieved ${countries.size} countries from database")  // Debug log

        // Fetch log counts for each country
        val logCounts = dbHelper.getLogCountsByCountry()
        Log.d("StoreJournalActivity", "Log counts: $logCounts")  // Debug log

        // Set up the RecyclerView with the CountryAdapter
        countryAdapter = CountryAdapter(countries, logCounts) { selectedCountry ->
            // Log and create an intent to redirect to LogDetailsActivity
            Log.d("StoreJournalActivity", "Selected Country: ${selectedCountry.name.common}, Country ID: ${selectedCountry.countryId}")

            // Create an intent to navigate to LogDetailsActivity
            val intent = Intent(this, LogDetailsActivity::class.java).apply {
                putExtra("selectedCountry", selectedCountry)  // Pass the selected country
                putExtra("photoUri", photoUri)  // Pass the photo URI
            }
            startActivity(intent)
        }

        journalRecyclerView.adapter = countryAdapter

        backBtn.setOnClickListener {
            finish()
        }

        cancelBtn.setOnClickListener {
            val intent = Intent(this, JournalMainActivity::class.java)
            startActivity(intent)
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
            finish()
        }

        mapButton = findViewById(R.id.mapButton)
        mapButton.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        // END OF FOOTER BUTTONS
    }
}
