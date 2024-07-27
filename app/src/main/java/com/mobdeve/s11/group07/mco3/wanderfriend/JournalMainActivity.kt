package com.mobdeve.s11.group07.mco3.wanderfriend

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class JournalMainActivity : AppCompatActivity() {

    private lateinit var journalRecyclerView: RecyclerView
    private lateinit var journalAdapter: JournalAdapter
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_main)

        journalRecyclerView = findViewById(R.id.journalRecyclerView)
        journalRecyclerView.layoutManager = LinearLayoutManager(this)

        dbHelper = UserDatabaseHelper(this)

        val user = dbHelper.getUser()
        if (user != null) {
            Log.d("JournalMainActivity", "User data retrieved: ${user.name}, ${user.age}, ${user.country}, ${user.traveledCountries.map { it.name.common }}")
            journalAdapter = JournalAdapter(user.traveledCountries)
            journalRecyclerView.adapter = journalAdapter
        } else {
            // Handle the case when user is null
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }
    }
}
