package com.mobdeve.s11.group07.mco3.wanderfriend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class SingleCountryAdapter(
    private var countries: List<Country>,
    private val onCountrySelected: (Country) -> Unit
) : RecyclerView.Adapter<SingleCountryAdapter.CountryViewHolder>() {

    private var selectedCountry: Country? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country_radio, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.bind(country, country == selectedCountry)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun updateList(newCountries: List<Country>) {
        countries = newCountries
        notifyDataSetChanged()
    }

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val countryFlag: ImageView = itemView.findViewById(R.id.countryFlag)
        private val countryName: TextView = itemView.findViewById(R.id.countryName)
        private val countryRadioButton: RadioButton = itemView.findViewById(R.id.countryRadioButton)

        fun bind(country: Country, isSelected: Boolean) {
            countryName.text = country.name.common
            if (country.flags.png.isNotEmpty()) {
                Picasso.get().load(country.flags.png).into(countryFlag)
            } else {
                countryFlag.setImageResource(R.drawable.placeholder) // Placeholder image if flag URL is empty
            }
            countryRadioButton.isChecked = isSelected

            itemView.setOnClickListener {
                selectedCountry = country
                notifyDataSetChanged()
                onCountrySelected(country)
            }

            countryRadioButton.setOnClickListener {
                selectedCountry = if (countryRadioButton.isChecked) country else null
                notifyDataSetChanged()
                onCountrySelected(selectedCountry ?: country)
            }
        }
    }
}
