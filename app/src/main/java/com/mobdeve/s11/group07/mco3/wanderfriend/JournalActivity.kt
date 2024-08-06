package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class JournalActivity : AppCompatActivity() {

    private lateinit var backBtn: Button
    private lateinit var newLogBtn: Button
    private lateinit var recyclerViewLogs: RecyclerView

    private var selectedCountry: Country? = null
    private lateinit var userDatabaseHelper: UserDatabaseHelper

    private lateinit var cameraButton: ImageButton
    private lateinit var journalButton: ImageButton
    private lateinit var mapButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        backBtn = findViewById(R.id.backBtn)
        newLogBtn = findViewById(R.id.newLogBtn)
        recyclerViewLogs = findViewById(R.id.recyclerViewLogs)

        userDatabaseHelper = UserDatabaseHelper(this)

        // Retrieve the selected country from the intent
        selectedCountry = intent.getParcelableExtra("selectedCountry")

        backBtn.setOnClickListener {
            val intent = Intent(this, JournalMainActivity::class.java)
            startActivity(intent)
            finish()
        }

        newLogBtn.setOnClickListener {
            val intent = Intent(this, NewLogOptionsActivity::class.java).apply {
                putExtra("selectedCountry", selectedCountry)
            }
            startActivity(intent)
        }

        // Load logs for the selected country
        loadLogsForCountry()

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
    }

    private fun loadLogsForCountry() {
        // Ensure the selected country is not null
        selectedCountry?.let { country ->
            // Retrieve logs for the specific country using its ID
            val logs = userDatabaseHelper.getLogsForCountry(country.countryId) // Use country.countryId
            Log.d("JournalActivity", "Retrieved ${logs.size} logs for Country ID: ${country.countryId}")  // Debug log
            logs.forEach { log ->
                Log.d("JournalActivity", "Log ID: ${log.logId}, Date: ${log.date}, Caption: ${log.caption}, Photo URI: ${log.photoUri}")
            }
            // Set up the RecyclerView with GridLayoutManager to display logs in two columns
            recyclerViewLogs.layoutManager = GridLayoutManager(this, 2)
            recyclerViewLogs.adapter = LogsAdapter(logs)
            scrollToBottom()
        } ?: run {
            Log.e("JournalActivity", "Selected country is null")
        }
    }

    // Helper function to scroll the RecyclerView to the bottom
    private fun scrollToBottom() {
        recyclerViewLogs.post {
            recyclerViewLogs.scrollToPosition(recyclerViewLogs.adapter?.itemCount?.minus(1) ?: 0)
        }
    }
}
