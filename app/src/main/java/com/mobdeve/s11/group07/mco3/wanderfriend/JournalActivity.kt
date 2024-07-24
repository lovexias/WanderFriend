package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class JournalActivity : AppCompatActivity() {

    private lateinit var backBtn: Button
    private lateinit var newLogBtn: Button
    private lateinit var recyclerViewLogs: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_journal)

        recyclerViewLogs = findViewById(R.id.recyclerViewLogs)
        backBtn = findViewById(R.id.backBtn)
        newLogBtn = findViewById(R.id.newLogBtn)

        val gridLayoutManager = GridLayoutManager(this, 2)
        recyclerViewLogs.setLayoutManager(gridLayoutManager)

        backBtn.setOnClickListener {
            val intent = Intent(this, JournalMainActivity::class.java)
            startActivity(intent)
            finish()
        }

        newLogBtn.setOnClickListener{
            val intent = Intent(this, NewLogOptionsActivity::class.java)
            startActivity(intent)
        }

    }
}