package com.mobdeve.s11.group07.mco3.wanderfriend

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import java.util.*

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

    private var photoUri: String? = null
    private var selectedCountry: Country? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            if (validateInputs()) {
                saveLogDetails()
            } else {
                Log.e("LogDetailsActivity", "Validation failed: All fields must be filled")
                errorMessage.text = "All fields must be filled"
                errorMessage.visibility = TextView.VISIBLE
            }
        }

        // FOOTER BUTTONS, this code must be present in every activity with a footer
        cameraButton = findViewById(R.id.cameraButton)
        cameraButton.setOnClickListener {
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
        mapButton.setOnClickListener {
            TODO("Implement start of activity once MapActivity is created")
        }

        // END OF FOOTER BUTTONS

        // Handle received data
        photoUri = intent.getStringExtra("photoUri")
        selectedCountry = intent.getParcelableExtra("selectedCountry")

        selectedCountry?.let {
            Log.d("LogDetailsActivity", "Received selectedCountry: ${it.name.common}")
        }

        photoUri?.let {
            Log.d("LogDetailsActivity", "Received photoUri: $it")
            Picasso.get().load(Uri.parse(it)).into(logPhoto)
        }

        dateInput.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            dateInput.setText(getString(R.string.date_format, selectedDay, selectedMonth + 1, selectedYear))
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun validateInputs(): Boolean {
        val isDateFilled = dateInput.text.toString().isNotEmpty()
        val isCaptionFilled = editTextCaption.text.toString().isNotEmpty()
        val isPhotoUriValid = photoUri != null

        Log.d("LogDetailsActivity", "Validation - Date: $isDateFilled, Caption: $isCaptionFilled, Photo URI: $isPhotoUriValid")

        return isDateFilled && isCaptionFilled && isPhotoUriValid
    }

    private fun saveLogDetails() {
        val date = dateInput.text.toString()
        val caption = editTextCaption.text.toString()

        Log.d("LogDetailsActivity", "Saving log details - Date: $date, Caption: $caption, Photo URI: $photoUri")

        selectedCountry?.let { country ->
            val log = CountryLog(0, country.id, date, caption, photoUri!!)
            val databaseHelper = UserDatabaseHelper(this)
            val success = databaseHelper.addLog(log, country.id)

            if (success) {
                Log.d("LogDetailsActivity", "Log saved successfully")
                val intent = Intent(this, JournalActivity::class.java).apply {
                    putExtra("selectedCountry", country)
                }
                startActivity(intent)
                finish()
            } else {
                Log.e("LogDetailsActivity", "Error saving log")
                errorMessage.text = "Error saving log"
                errorMessage.visibility = TextView.VISIBLE
            }
        } ?: run {
            Log.e("LogDetailsActivity", "Selected country is null")
            errorMessage.text = "Error: Selected country is null"
            errorMessage.visibility = TextView.VISIBLE
        }
    }
}
