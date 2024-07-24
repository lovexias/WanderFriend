package com.mobdeve.s11.group07.mco3.wanderfriend

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NewLogOptionsActivity : AppCompatActivity() {

    private lateinit var backBtn: Button
    private lateinit var uploadPhoto: Button
    private lateinit var takePhoto: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_log_options)

        backBtn = findViewById(R.id.backBtn)
        uploadPhoto = findViewById(R.id.uploadPhoto)
        takePhoto = findViewById(R.id.takePhoto)
    }
}