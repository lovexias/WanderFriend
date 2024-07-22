package com.mobdeve.s11.group07.mco3.wanderfriend

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.create_profile)

        // loading_screen
//        val textView: TextView = findViewById(R.id.name)
//        val typeface: Typeface? = ResourcesCompat.getFont(this, R.font.righteous)
//        textView.typeface = typeface

        // create_profile
        val welcomeText: TextView = findViewById(R.id.welcomeText)
        val welcomeTypeface: Typeface? = ResourcesCompat.getFont(this, R.font.righteous)
        welcomeText.typeface = welcomeTypeface

        val startedText: TextView = findViewById(R.id.startedText)
        val startedTypeface: Typeface? = ResourcesCompat.getFont(this, R.font.outfit_medium)
        startedText.typeface = startedTypeface

        val editText: EditText = findViewById(R.id.nameInput)
        val inputTypeface: Typeface? = ResourcesCompat.getFont(this, R.font.inter_semibold)
        editText.typeface = inputTypeface

        val ageText: EditText = findViewById(R.id.ageInput)
        val ageTypeface: Typeface? = ResourcesCompat.getFont(this, R.font.inter_semibold)
        ageText.typeface = ageTypeface

        // Find the Spinner by its ID
        val spinner: Spinner = findViewById(R.id.spinner)
        val spinnerTypeface: Typeface? = ResourcesCompat.getFont(this, R.font.inter_semibold)

        // Create an ArrayAdapter using the custom spinner item layout
        val adapter = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item,
            resources.getStringArray(R.array.countries)
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.typeface = spinnerTypeface
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.typeface = spinnerTypeface
                return view
            }
        }

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item)

        // Apply the adapter to the spinner
        spinner.adapter = adapter

        // Set the default selection to "Country"
        spinner.setSelection(0)

        // Handle Spinner selections
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Handle the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }



    }
}