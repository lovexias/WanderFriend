package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
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

    private val REQUEST_CODE_NEW_JOURNAL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_journal_main)

        journalRecyclerView = findViewById(R.id.journalRecyclerView)
        journalRecyclerView.layoutManager = LinearLayoutManager(this)

        dbHelper = UserDatabaseHelper(this)

        val user = dbHelper.getUser()
        if (user != null) {
            journalAdapter = JournalAdapter(user.traveledCountries)
            journalRecyclerView.adapter = journalAdapter
        } else {
            // Handle the case when user is null
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
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

        newJournalBtn = findViewById(R.id.newJournalBtn)
        newJournalBtn.setOnClickListener {
            val intent = Intent(this, JournalCountriesActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_NEW_JOURNAL)
        }

        // Check if an intent has been passed to handle new journal entry
        handleIntent(intent)
    }

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
                journalAdapter = JournalAdapter(updatedUser.traveledCountries)
                journalRecyclerView.adapter = journalAdapter
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_NEW_JOURNAL && resultCode == RESULT_OK) {
            handleIntent(data ?: Intent())
        }
    }
}
