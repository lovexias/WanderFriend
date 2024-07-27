package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JournalCountriesActivity : AppCompatActivity() {
    private lateinit var countriesAdapter: CountriesAdapter
    private lateinit var recyclerViewCountries: RecyclerView
    private lateinit var searchField: EditText
    private lateinit var backBtn: Button
    private lateinit var cancelBtn: Button
    private var countriesList: List<Country> = listOf()
    private val selectedCountries = mutableListOf<Country>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_countries)

        recyclerViewCountries = findViewById(R.id.recyclerViewCountries)
        searchField = findViewById(R.id.searchField)
        backBtn = findViewById(R.id.backBtn)
        cancelBtn = findViewById(R.id.cancelBtn)

        recyclerViewCountries.layoutManager = LinearLayoutManager(this)

        fetchCountries()

        backBtn.setOnClickListener {
            finish()
        }

        cancelBtn.setOnClickListener {
            val intent = Intent(this, JournalSubtitleActivity::class.java)
            startActivity(intent)
        }

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun fetchCountries() {
        RetrofitInstance.api.getAllCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    response.body()?.let { countries ->
                        countriesList = countries.sortedBy { it.name.common }
                        countriesAdapter = CountriesAdapter(countriesList) { country, isSelected ->
                            if (isSelected) {
                                selectedCountries.add(country)
                            } else {
                                selectedCountries.remove(country)
                            }
                        }
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
        countriesAdapter.updateList(filteredList)
    }
}
