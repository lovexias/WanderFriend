package com.mobdeve.s11.group07.mco3.wanderfriend

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.content.res.ResourcesCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.loading_screen)

        val textView: TextView = findViewById(R.id.name)
        val typeface: Typeface? = ResourcesCompat.getFont(this, R.font.righteous)
        textView.typeface = typeface

    }
}