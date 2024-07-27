package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.core.content.res.ResourcesCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateProfileActivity : ComponentActivity() {
    private lateinit var countrySpinner: Spinner
    private lateinit var spinnerTypeface: Typeface
    private lateinit var customButton: Button
    private lateinit var nameInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var errorMessage: TextView

    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbHelper = UserDatabaseHelper(this)

        // Check if user already exists
        if (dbHelper.getUser() != null) {
            // Redirect to JournalMainActivity if user exists
            val intent = Intent(this, JournalMainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // If no user exists, show the profile creation layout
        setContentView(R.layout.activity_create_profile)

        // Set typefaces for text views and edit texts
        val welcomeText: TextView = findViewById(R.id.welcomeText)
        val welcomeTypeface: Typeface? = ResourcesCompat.getFont(this, R.font.righteous)
        welcomeText.typeface = welcomeTypeface

        val startedText: TextView = findViewById(R.id.startedText)
        val startedTypeface: Typeface? = ResourcesCompat.getFont(this, R.font.outfit_medium)
        startedText.typeface = startedTypeface

        nameInput = findViewById(R.id.nameInput)
        val inputTypeface: Typeface? = ResourcesCompat.getFont(this, R.font.inter_semibold)
        nameInput.typeface = inputTypeface

        ageInput = findViewById(R.id.ageInput)
        val ageTypeface: Typeface? = ResourcesCompat.getFont(this, R.font.inter_semibold)
        ageInput.typeface = ageTypeface

        errorMessage = findViewById(R.id.errorMessage)

        // Set up the Spinner
        countrySpinner = findViewById(R.id.spinner)
        spinnerTypeface = ResourcesCompat.getFont(this, R.font.inter_semibold)!!

        fetchCountries()

        // Set up the custom button
        customButton = findViewById(R.id.customButton)
        customButton.setOnClickListener {
            val name = nameInput.text.toString()
            val age = ageInput.text.toString().toIntOrNull()
            val country = countrySpinner.selectedItem?.toString()

            if (name.isEmpty() || age == null || country == null) {
                errorMessage.visibility = View.VISIBLE
            } else {
                val user = User(name = name, age = age, country = country, traveledCountries = emptyList())
                val success = dbHelper.addUser(user)
                if (success) {
                    val intent = Intent(this, CountriesSignupActivity::class.java)
                    intent.putExtra("initialCountry", country)
                    startActivity(intent)
                    finish()
                } else {
                    errorMessage.text = "Error saving user. Please try again."
                    errorMessage.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun fetchCountries() {
        RetrofitInstance.api.getAllCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    response.body()?.let { countries ->
                        val sortedCountries = countries.sortedBy { it.name.common }
                        val countryNames = sortedCountries.map { it.name.common }
                        val adapter = object : ArrayAdapter<String>(
                            this@CreateProfileActivity,
                            R.layout.spinner_item,
                            countryNames
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
                        adapter.setDropDownViewResource(R.layout.spinner_item)
                        countrySpinner.adapter = adapter
                    }
                } else {
                    Log.e("CreateProfileActivity", "Response not successful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Log.e("CreateProfileActivity", "Error fetching countries", t)
            }
        })
    }
}
