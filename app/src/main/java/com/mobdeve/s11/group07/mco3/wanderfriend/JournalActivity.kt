package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class JournalActivity : AppCompatActivity() {

    private lateinit var backBtn: Button
    private lateinit var newLogBtn: Button
    private lateinit var recyclerViewLogs: RecyclerView

    private lateinit var cameraButton: ImageButton
    private lateinit var journalButton: ImageButton
    private lateinit var mapButton: ImageButton

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
            TODO("Implement start of activity once MapActivity is created")
        }

        // END OF FOOTER BUTTONS

    }
}