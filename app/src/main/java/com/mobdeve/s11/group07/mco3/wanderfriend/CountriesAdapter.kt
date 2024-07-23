package com.mobdeve.s11.group07.mco3.wanderfriend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CountriesAdapter(
    private val countries: List<Country>
) : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    private val selectedCountries = mutableSetOf<Country>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.bind(country)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val countryFlag: ImageView = itemView.findViewById(R.id.countryFlag)
        private val countryName: TextView = itemView.findViewById(R.id.countryName)
        private val countryCheckBox: CheckBox = itemView.findViewById(R.id.countryCheckBox)

        fun bind(country: Country) {
            countryName.text = country.name.common
            if (country.flags.png.isNotEmpty()) {
                Picasso.get().load(country.flags.png).into(countryFlag)
            } else {
                countryFlag.setImageResource(R.drawable.placeholder) // Placeholder image if flag URL is empty
            }
            countryCheckBox.isChecked = selectedCountries.contains(country)

            itemView.setOnClickListener {
                if (countryCheckBox.isChecked) {
                    selectedCountries.remove(country)
                } else {
                    selectedCountries.add(country)
                }
                countryCheckBox.isChecked = !countryCheckBox.isChecked
            }
        }
    }
}