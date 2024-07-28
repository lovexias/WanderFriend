package com.mobdeve.s11.group07.mco3.wanderfriend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class JournalAdapter(
    private val countries: List<Country>,
    private val onCountryClick: (Country) -> Unit
) : RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.journal_item, parent, false)
        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    inner class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val countryNameTextView: TextView = itemView.findViewById(R.id.countryName)
        private val countryImageView: ImageView = itemView.findViewById(R.id.countryImg)
        private val journalCaptionTextView: TextView = itemView.findViewById(R.id.journalCaption)

        fun bind(country: Country) {
            val countryName = country.name.common.split("\n")
            countryNameTextView.text = countryName[0]
            if (country.flags.png.isNotEmpty()) {
                Picasso.get().load(country.flags.png).into(countryImageView)
            } else {
                countryImageView.setImageResource(R.drawable.placeholder)
            }
            journalCaptionTextView.text = if (countryName.size > 1) countryName[1] else ""

            itemView.setOnClickListener {
                onCountryClick(country)
            }
        }
    }
}
