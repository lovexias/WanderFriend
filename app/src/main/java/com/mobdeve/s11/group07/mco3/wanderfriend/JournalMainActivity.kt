package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class JournalMainActivity : AppCompatActivity() {

    private lateinit var journalRecyclerView: RecyclerView
    private lateinit var journalAdapter: JournalAdapter
    private lateinit var dbHelper: UserDatabaseHelper

    private lateinit var cameraButton: ImageButton
    private lateinit var journalButton: ImageButton
    private lateinit var mapButton: ImageButton
    private lateinit var newJournalBtn: Button

    private val requestCodeNewJournal = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            handleIntent(result.data ?: Intent())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_journal_main)

        journalRecyclerView = findViewById(R.id.journalRecyclerView)
        journalRecyclerView.layoutManager = LinearLayoutManager(this)

        dbHelper = UserDatabaseHelper(this)

        val user = dbHelper.getUser()
        if (user != null) {
            // Add countries to the Country table and ensure they have IDs
            user.traveledCountries.forEach { country ->
                val storedCountry = dbHelper.getCountryByName(country.name.common)
                val countryId = storedCountry?.countryId ?: dbHelper.addOrUpdateCountry(country)
                Log.d("JournalMainActivity", "Ensuring country in database: ${country.name.common}, Country ID: $countryId")
            }

            // Set up the adapter with traveled countries and handle country selection
            journalAdapter = JournalAdapter(user.traveledCountries) { country ->
                val storedCountry = dbHelper.getCountryByName(country.name.common)
                val countryId = storedCountry?.countryId ?: dbHelper.addOrUpdateCountry(country)

                val intent = Intent(this, JournalActivity::class.java)
                intent.putExtra("selectedCountry", country.copy(countryId = countryId))
                Log.d("JournalMainActivity", "Navigating to JournalActivity with Country: ${country.name.common}, Country ID: $countryId")  // Debug log
                startActivity(intent)
            }
            journalRecyclerView.adapter = journalAdapter
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
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
            finish()
        }
        // END OF FOOTER BUTTONS

        newJournalBtn = findViewById(R.id.newJournalBtn)
        newJournalBtn.setOnClickListener {
            val intent = Intent(this, JournalCountriesActivity::class.java)
            requestCodeNewJournal.launch(intent)
        }

        // Handle incoming intents
        handleIntent(intent)
    }

    // Handle intent for selecting a country and caption
    private fun handleIntent(intent: Intent) {
        val selectedCountry: Country? = intent.getParcelableExtra("selectedCountry")
        val caption: String? = intent.getStringExtra("caption")

        if (selectedCountry != null && caption != null) {
            val user = dbHelper.getUser()
            user?.let {
                val updatedTraveledCountries = it.traveledCountries.toMutableList().apply {
                    add(selectedCountry.copy(name = selectedCountry.name.copy(common = "${selectedCountry.name.common}\n$caption")))
                }
                val updatedUser = it.copy(traveledCountries = updatedTraveledCountries)
                dbHelper.addUser(updatedUser)
                journalAdapter = JournalAdapter(updatedUser.traveledCountries) { country ->
                    val intent = Intent(this, JournalActivity::class.java)
                    intent.putExtra("selectedCountry", country)
                    startActivity(intent)
                }
                journalRecyclerView.adapter = journalAdapter
            }
        }
    }
}
