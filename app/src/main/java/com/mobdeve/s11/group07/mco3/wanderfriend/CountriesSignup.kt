package com.mobdeve.s11.group07.mco3.wanderfriend

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mobdeve.s11.group07.mco3.wanderfriend.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountriesSignup : AppCompatActivity() {
    private lateinit var countriesAdapter: CountriesAdapter
    private lateinit var recyclerViewCountries: RecyclerView
    private lateinit var searchField: EditText
    private lateinit var fabDone: FloatingActionButton
    private var countriesList: List<Country> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries_signup)

        recyclerViewCountries = findViewById(R.id.recyclerViewCountries)
        searchField = findViewById(R.id.searchField)
        fabDone = findViewById(R.id.fabDone)

        recyclerViewCountries.layoutManager = LinearLayoutManager(this)

        fetchCountries()

        // Implement search filter
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        fabDone.setOnClickListener {
            // Handle FAB click
        }
    }

    private fun fetchCountries() {
        RetrofitInstance.api.getAllCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    response.body()?.let { countries ->
                        countriesList = countries.sortedBy { it.name.common }
                        countriesAdapter = CountriesAdapter(countriesList)
                        recyclerViewCountries.adapter = countriesAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun filter(text: String) {
        val filteredList = countriesList.filter {
            it.name.common.contains(text, ignoreCase = true)
        }
        countriesAdapter = CountriesAdapter(filteredList)
        recyclerViewCountries.adapter = countriesAdapter
    }
}