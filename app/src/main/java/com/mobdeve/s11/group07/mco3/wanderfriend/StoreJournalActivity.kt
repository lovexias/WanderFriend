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

        // Set up the RecyclerView with the CountryAdapter
        countryAdapter = CountryAdapter(countries) { selectedCountry ->
            // Handle storing the photo in the selected country's journal
            Log.d("StoreJournalActivity", "Storing photo in journal for Country: ${selectedCountry.name.common}, Country ID: ${selectedCountry.countryId}")

            val log = CountryLog(
                logId = 0,  // Assuming '0' for auto-increment if needed
                countryId = selectedCountry.countryId,
                date = "2024-08-06",  // Replace with actual date input if available
                caption = "Sample Caption",  // Replace with actual caption input if available
                photoUri = photoUri ?: ""
            )

            val success = dbHelper.addLog(log)
            if (success) {
                Log.d("StoreJournalActivity", "Log successfully added to Country ID: ${selectedCountry.countryId}")
                val intent = Intent(this, JournalActivity::class.java).apply {
                    putExtra("selectedCountry", selectedCountry)
                }
                startActivity(intent)
                finish()
            } else {
                Log.e("StoreJournalActivity", "Failed to add log to Country ID: ${selectedCountry.countryId}")
            }
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
        cameraButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        journalButton = findViewById(R.id.journalButton)
        journalButton.setOnClickListener {
            val intent = Intent(this, JournalMainActivity::class.java)
            startActivity(intent)
            finish()
        }

        mapButton = findViewById(R.id.mapButton)
        mapButton.setOnClickListener {
            // Implement start of activity once MapActivity is created
        }

        // END OF FOOTER BUTTONS
    }
}
