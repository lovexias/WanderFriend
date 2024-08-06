package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.res.ResourcesCompat
import java.io.File

class LoadingScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)

        val textView: TextView = findViewById(R.id.name)
        val typeface: Typeface? = ResourcesCompat.getFont(this, R.font.righteous)
        textView.typeface = typeface

        // Delay for 5 seconds and then start CreateProfileActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, CreateProfileActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000) // 1 second delay
    }
}
