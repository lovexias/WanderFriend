package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountriesSignupActivity : AppCompatActivity() {
    private lateinit var countriesAdapter: CountriesAdapter
    private lateinit var recyclerViewCountries: RecyclerView
    private lateinit var searchField: EditText
    private lateinit var fabDone: FloatingActionButton
    private lateinit var noCountryBtn: Button
    private var countriesList: List<Country> = listOf()
    private val selectedCountries = mutableListOf<Country>()

    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries_signup)

        dbHelper = UserDatabaseHelper(this)

        recyclerViewCountries = findViewById(R.id.recyclerViewCountries)
        searchField = findViewById(R.id.searchField)
        fabDone = findViewById(R.id.fabDone)
        noCountryBtn = findViewById(R.id.noCountryBtn)

        recyclerViewCountries.layoutManager = LinearLayoutManager(this)

        fetchCountries()

        noCountryBtn.setOnClickListener {
            saveSelectedCountriesAndProceed()
        }

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        fabDone.setOnClickListener {
            saveSelectedCountriesAndProceed()
        }
    }

    private fun fetchCountries() {
        RetrofitInstance.api.getAllCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    response.body()?.let { countries ->
                        countriesList = countries.sortedBy { it.name.common }
                        countriesAdapter = CountriesAdapter(countriesList, selectedCountries)
                        recyclerViewCountries.adapter = countriesAdapter

                        // Add initial country if provided
                        val initialCountryName = intent.getStringExtra("initialCountry")
                        initialCountryName?.let { name ->
                            countries.find { it.name.common == name }?.let { country ->
                                selectedCountries.add(country)
                                countriesAdapter.notifyDataSetChanged()
                                Log.d("CountriesSignupActivity", "Initial country added: ${country.name.common}")
                                Log.d("CountriesSignupActivity", "Current selected countries: ${selectedCountries.map { it.name.common }}")
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                // Handle error
                Log.e("CountriesSignupActivity", "Error fetching countries", t)
            }
        })
    }

    private fun filter(text: String) {
        val filteredList = countriesList.filter {
            it.name.common.contains(text, ignoreCase = true)
        }
        countriesAdapter.updateList(filteredList)
    }

    private fun saveSelectedCountriesAndProceed() {
        val user = dbHelper.getUser() ?: return
        // Combine initial country and selected countries
        val allCountries = user.traveledCountries.toMutableList().apply {
            addAll(selectedCountries)
        }.distinct()
        val updatedUser = user.copy(traveledCountries = allCountries)
        val success = dbHelper.addUser(updatedUser)
        Log.d("CountriesSignupActivity", "Selected countries to be saved: ${allCountries.map { it.name.common }}, Success: $success")
        val intent = Intent(this, JournalMainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
