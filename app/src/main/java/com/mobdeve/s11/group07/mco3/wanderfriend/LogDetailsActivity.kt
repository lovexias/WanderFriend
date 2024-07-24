package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.log

class LogDetailsActivity : AppCompatActivity() {

    private lateinit var backBtn: Button
    private lateinit var logPhoto: ImageView
    private lateinit var dateInput: EditText
    private lateinit var editTextCaption: EditText
    private lateinit var errorMessage: TextView
    private lateinit var submitBtn: Button

    private lateinit var cameraButton: ImageButton
    private lateinit var journalButton: ImageButton
    private lateinit var mapButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_details)

        backBtn = findViewById(R.id.backBtn)
        logPhoto = findViewById(R.id.logPhoto)
        dateInput = findViewById(R.id.dateInput)
        editTextCaption = findViewById(R.id.editTextCaption)
        errorMessage = findViewById(R.id.errorMessage)
        submitBtn = findViewById(R.id.submitBtn)

        backBtn.setOnClickListener {
            finish()
        }

        submitBtn.setOnClickListener {
            // TODO: Must redirect to the journal it was added to 
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