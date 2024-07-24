package com.mobdeve.s11.group07.mco3.wanderfriend

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CameraDoneActivity : AppCompatActivity() {

    private lateinit var storeBtn: Button
    private lateinit var discardBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_camera_done)

        storeBtn = findViewById(R.id.storeBtn)
        discardBtn = findViewById(R.id.discardBtn)

        discardBtn.setOnClickListener {
            finish()
        }

    }
}