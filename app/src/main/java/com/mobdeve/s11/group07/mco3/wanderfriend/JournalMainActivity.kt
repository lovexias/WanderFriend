package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class JournalMainActivity : AppCompatActivity() {

    private lateinit var journalRecyclerView: RecyclerView
    private lateinit var newJournalBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_journal_main)

        journalRecyclerView = findViewById(R.id.journalRecyclerView)
        journalRecyclerView.layoutManager = LinearLayoutManager(this)
        newJournalBtn = findViewById(R.id.newJournalBtn)

        newJournalBtn.setOnClickListener {
            val intent = Intent(this, JournalCountriesActivity::class.java)
            startActivity(intent)
        }
    }
}