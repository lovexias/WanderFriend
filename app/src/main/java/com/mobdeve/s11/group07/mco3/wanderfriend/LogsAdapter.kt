package com.mobdeve.s11.group07.mco3.wanderfriend

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class LogsAdapter(private val logs: List<CountryLog>) : RecyclerView.Adapter<LogsAdapter.LogViewHolder>() {

    inner class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logPhoto: ImageView = itemView.findViewById(R.id.logPhoto)
        val logDate: TextView = itemView.findViewById(R.id.logDate)
        val logCaption: TextView = itemView.findViewById(R.id.logCaption)

        fun bind(log: CountryLog) {
            logDate.text = log.date
            logCaption.text = log.caption
            Log.d("LogsAdapter", "Binding Log ID: ${log.logId}, Country ID: ${log.countryId}, Caption: ${log.caption}")  // Debug log
            try {
                Picasso.get().load(Uri.parse(log.photoUri)).into(logPhoto)
            } catch (e: SecurityException) {
                Log.e("LogsAdapter", "SecurityException: ${e.message}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.log_item, parent, false)
        return LogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.bind(logs[position])
    }

    override fun getItemCount(): Int {
        return logs.size
    }
}
