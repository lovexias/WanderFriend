package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class JournalSubtitleActivity : AppCompatActivity() {

    private lateinit var backBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var countryFlag: ImageView
    private lateinit var countryName: TextView
    private lateinit var editTextCaption: EditText
    private lateinit var submitJournalBtn: Button

    private lateinit var cameraButton: ImageButton
    private lateinit var journalButton: ImageButton
    private lateinit var mapButton: ImageButton

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

        val selectedCountry: Country? = intent.getParcelableExtra("selectedCountry")
        selectedCountry?.let { country ->
            countryName.text = country.name.common
            if (country.flags.png.isNotEmpty()) {
                Picasso.get().load(country.flags.png).into(countryFlag)
            } else {
                countryFlag.setImageResource(R.drawable.placeholder) // Placeholder image if flag URL is empty
            }
        }

        backBtn.setOnClickListener {
            finish()  // Return to the previous activity (JournalCountriesActivity)
        }

        cancelBtn.setOnClickListener {
            val intent = Intent(this, JournalMainActivity::class.java)
            startActivity(intent)
            finish()  // Return to JournalMainActivity
        }

        editTextCaption.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                submitJournalBtn.isEnabled = s.toString().isNotEmpty()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        submitJournalBtn.setOnClickListener {
            val caption = editTextCaption.text.toString()
            val resultIntent = Intent(this, JournalMainActivity::class.java).apply {
                putExtra("selectedCountry", selectedCountry)
                putExtra("caption", caption)
            }
            startActivity(resultIntent)
            finish()  // Ensure the activity finishes after setting the result
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
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        // END OF FOOTER BUTTONS
    }
}
