package com.example.ft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AirRatesAdapter(val list: List<AirRatesModel>, val serviceTerm: String,  val onItemSelected: (AirRatesModel) -> Unit) : RecyclerView.Adapter<AirRatesAdapter.ViewHolder>() {
    private var selectedPosition = -1
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mainLayout: View = view.findViewById(R.id.mainLayout)
        val airline = view.findViewById<TextView>(R.id.tvAirline)
        val freight = view.findViewById<TextView>(R.id.tvFreight)
        val exWork = view.findViewById<TextView>(R.id.tvExWork)
        val doCharges = view.findViewById<TextView>(R.id.tvDo)
        val transit = view.findViewById<TextView>(R.id.tvTransit)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rates_air, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.airline.text = item.airline
        holder.freight.text = "Freight USD: ${item.freight}"
        holder.doCharges.text = "D/O Charges: ${item.doCharges}"
        holder.transit.text = "Transit Time: ${item.transitTime}"

        if (serviceTerm == "ExWork") {
            holder.exWork.visibility = View.VISIBLE
            holder.exWork.text = "ExWork Charges: ${item.exWork}"
        }
        else {
            holder.exWork.visibility = View.GONE
        }

        if (position == selectedPosition) {
            holder.mainLayout.setBackgroundResource(R.drawable.item_selected_bg)
        }
        else {
            holder.mainLayout.setBackgroundResource(R.drawable.item_normal_bg)
        }
        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onItemSelected(item)
        }
    }
}