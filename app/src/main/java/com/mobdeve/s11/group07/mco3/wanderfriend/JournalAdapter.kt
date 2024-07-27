package com.mobdeve.s11.group07.mco3.wanderfriend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class JournalAdapter(private val countries: List<Country>) :
    RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

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

    class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val countryNameTextView: TextView = itemView.findViewById(R.id.countryName)
        private val countryImageView: ImageView = itemView.findViewById(R.id.countryImg)
        private val journalCaptionTextView: TextView = itemView.findViewById(R.id.journalCaption)

        fun bind(country: Country) {
            countryNameTextView.text = country.name.common
            if (country.flags.png.isNotEmpty()) {
                Picasso.get().load(country.flags.png).into(countryImageView)
            } else {
                countryImageView.setImageResource(R.drawable.placeholder) // Placeholder image if flag URL is empty
            }
            journalCaptionTextView.text = country.name.common
        }
    }
}
