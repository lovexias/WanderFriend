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
import android.widget.Toast
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

        // Initialize views
        backBtn = findViewById(R.id.backBtn)
        logPhoto = findViewById(R.id.logPhoto)
        dateInput = findViewById(R.id.dateInput)
        editTextCaption = findViewById(R.id.editTextCaption)
        errorMessage = findViewById(R.id.errorMessage)
        submitBtn = findViewById(R.id.submitBtn)

        // Set up back button functionality
        backBtn.setOnClickListener {
            finish()
        }

        // Set up submit button functionality
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
            // Implement start of activity once MapActivity is created
        }

        // Initialize photo and selected country data
        initializeData()

        // Set up date picker
        dateInput.setOnClickListener {
            showDatePickerDialog()
        }
    }

    /**
     * Initialize the photo URI and selected country data.
     */
    private fun initializeData() {
        photoUri = intent.getStringExtra("photoUri")
        selectedCountry = intent.getParcelableExtra("selectedCountry")

        selectedCountry?.let {
            Log.d("LogDetailsActivity", "Received selectedCountry: ${it.name.common}")

            photoUri?.let { uriString ->
                Log.d("LogDetailsActivity", "Received photoUri: $uriString")
                val uri = Uri.parse(uriString)
                grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                Picasso.get().load(uri).into(logPhoto)
            } ?: run {
                Log.e("LogDetailsActivity", "Photo URI is null")
                errorMessage.text = "Error: Photo URI is null"
                errorMessage.visibility = TextView.VISIBLE
            }
        } ?: run {
            Log.e("LogDetailsActivity", "Selected country is null")
            errorMessage.text = "Error: Selected country is null"
            errorMessage.visibility = TextView.VISIBLE
        }
    }

    /**
     * Show a date picker dialog for selecting the log date.
     */
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            dateInput.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
        }, year, month, day)

        datePickerDialog.show()
    }

    /**
     * Validate input fields before saving log details.
     * @return true if all inputs are valid, false otherwise
     */
    private fun validateInputs(): Boolean {
        val isDateFilled = dateInput.text.toString().isNotEmpty()
        val isCaptionFilled = editTextCaption.text.toString().isNotEmpty()
        val isPhotoUriValid = photoUri != null

        Log.d("LogDetailsActivity", "Validation - Date: $isDateFilled, Caption: $isCaptionFilled, Photo URI: $isPhotoUriValid")

        return isDateFilled && isCaptionFilled && isPhotoUriValid
    }

    /**
     * Save the log details into the database.
     */
    private fun saveLogDetails() {
        val date = dateInput.text.toString()
        val caption = editTextCaption.text.toString()

        Log.d("LogDetailsActivity", "Saving log details - Date: $date, Caption: $caption, Photo URI: $photoUri")

        selectedCountry?.let { country ->
            val log = CountryLog(
                logId = 0,  // Use the correct parameter name for logId
                countryId = country.countryId,  // Ensure you have this property in the Country model
                date = date,
                caption = caption,
                photoUri = photoUri!!
            )
            val databaseHelper = UserDatabaseHelper(this)
            val success = databaseHelper.addLog(log)

            if (success) {
                Log.d("LogDetailsActivity", "Log saved successfully")
                Toast.makeText(this, "Log saved successfully!", Toast.LENGTH_SHORT).show()
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