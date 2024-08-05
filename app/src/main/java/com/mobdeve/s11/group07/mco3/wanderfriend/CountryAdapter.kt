package com.mobdeve.s11.group07.mco3.wanderfriend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CountryAdapter(
    private val countries: List<Country>,
    private val logCounts: Map<Long, Int>, // Map of countryId to log count
    private val onCountrySelected: (Country) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryFlag: ImageView = itemView.findViewById(R.id.countryImg)
        val countryName: TextView = itemView.findViewById(R.id.countryName)
        val journalCaption: TextView = itemView.findViewById(R.id.journalCaption)

        fun bind(country: Country) {
            countryName.text = country.name.common
            // Get the log count for the current country
            val logCount = logCounts[country.countryId] ?: 0
            journalCaption.text = if (logCount > 0) "$logCount logs available" else "No logs available"
            Picasso.get().load(country.flags.png).into(countryFlag)

            itemView.setOnClickListener {
                onCountrySelected(country)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.journal_item, parent, false)
        return CountryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int {
        return countries.size
    }
}
