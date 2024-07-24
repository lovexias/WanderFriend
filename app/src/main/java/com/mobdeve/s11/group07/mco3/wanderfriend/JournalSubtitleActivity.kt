package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class JournalSubtitleActivity : AppCompatActivity() {

    private lateinit var backBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var countryFlag: ImageView
    private lateinit var countryName: TextView
    private lateinit var editTextCaption: EditText
    private lateinit var submitJournalBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_journal_subtitle)

        backBtn = findViewById(R.id.backBtn)
        cancelBtn = findViewById(R.id.cancelBtn)
        countryFlag = findViewById(R.id.countryFlag)
        countryName = findViewById(R.id.countryName)
        editTextCaption = findViewById(R.id.editTextCaption)
        submitJournalBtn = findViewById(R.id.submitJournalBtn)

        backBtn.setOnClickListener{
            finish()
        }

        cancelBtn.setOnClickListener{
            val intent = Intent(this, JournalMainActivity::class.java)
            startActivity(intent)
            finish()
        }

        submitJournalBtn.setOnClickListener{
            val intent = Intent(this, JournalMainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}