package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
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
        }

        // FOOTER BUTTONS, this code must be present in every activity with a footer
        cameraButton = findViewById(R.id.cameraButton)
        cameraButton.setOnClickListener{
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
        mapButton.setOnClickListener{
            // Implement start of activity once MapActivity is created
        }

        newJournalBtn = findViewById(R.id.newJournalBtn)
        newJournalBtn.setOnClickListener {
            val intent = Intent(this, JournalCountriesActivity::class.java)
            startActivity(intent)
        }
        // END OF FOOTER BUTTONS
    }
}
