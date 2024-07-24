package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AllSetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_set)

        val allSetMain = findViewById<View>(R.id.allSetMain)

        // Set an OnClickListener to navigate to JournalMainActivity
        allSetMain.setOnClickListener {
            val intent = Intent(this, JournalMainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set window insets listener
        ViewCompat.setOnApplyWindowInsetsListener(allSetMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}