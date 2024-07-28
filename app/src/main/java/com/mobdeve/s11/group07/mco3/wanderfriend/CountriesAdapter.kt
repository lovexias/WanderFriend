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
    private val countries: List<Country>,
    private val selectedCountries: MutableList<Country>
) : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryFlag: ImageView = itemView.findViewById(R.id.countryFlag)
        val countryName: TextView = itemView.findViewById(R.id.countryName)
        val countryCheckBox: CheckBox = itemView.findViewById(R.id.countryCheckBox)

        fun bind(country: Country, isSelected: Boolean) {
            countryName.text = country.name.common
            country.flags?.png?.let {
                if (it.isNotEmpty()) {
                    Picasso.get().load(it).into(countryFlag)
                } else {
                    countryFlag.setImageResource(R.drawable.placeholder) // Placeholder image if flag URL is empty
                }
            } ?: run {
                countryFlag.setImageResource(R.drawable.placeholder) // Placeholder image if flag URL is null
            }
            countryCheckBox.isChecked = isSelected

            itemView.setOnClickListener {
                val newState = !countryCheckBox.isChecked
                countryCheckBox.isChecked = newState
                if (newState) {
                    selectedCountries.add(country)
                } else {
                    selectedCountries.remove(country)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.bind(country, selectedCountries.contains(country))
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun updateList(newCountries: List<Country>) {
        (countries as MutableList).clear()
        countries.addAll(newCountries)
        notifyDataSetChanged()
    }
}
