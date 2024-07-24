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

class StoreJournalActivity : AppCompatActivity() {

    private lateinit var backBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var journalRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_store_journal)

        backBtn = findViewById(R.id.backBtn)
        cancelBtn = findViewById(R.id.cancelBtn)

        journalRecyclerView = findViewById(R.id.journalRecyclerView)
        journalRecyclerView.layoutManager = LinearLayoutManager(this)

        backBtn.setOnClickListener {
            finish()
        }

        cancelBtn.setOnClickListener {
            val intent = Intent(this, JournalMainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}