package com.example.ft

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot

class ShipmentAdapter( private val context: Context, private val list: ArrayList<DocumentSnapshot>)
    : RecyclerView.Adapter<ShipmentAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val refNo: TextView = itemView.findViewById(R.id.tvRefNo)
        val route: TextView = itemView.findViewById(R.id.tvRoute)
        val status: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_shipment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val doc = list[position]

        val refNo = doc.getString("refNo") ?: ""
        val origin = doc.getString("origin") ?: ""
        val destination = doc.getString("destination") ?: ""
        val status = doc.getString("currentStatus") ?: ""

        holder.refNo.text = refNo
        holder.route.text = "$origin → $destination"
        holder.status.text = status

        holder.itemView.setOnClickListener {
            val intent = Intent(context, View_Status::class.java)
            intent.putExtra("docId", doc.id)
            intent.putExtra("type", doc.reference.parent.parent?.id) // Import/Export
            context.startActivity(intent)
        }
    }
}