package com.example.ft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BotAdapter(private val list: List<BotModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val USER = 1
        const val BOT = 2
    }

    override fun getItemViewType(position: Int): Int {

        return if (list[position].role == "user") USER else BOT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == USER) {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)

            UserViewHolder(view)

        } else {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bot, parent, false)

            BotViewHolder(view)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = list[position]

        if (holder is UserViewHolder) {
            holder.txtUser.text = message.message
        } else if (holder is BotViewHolder) {
            holder.txtBot.text = message.message
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtUser: TextView = itemView.findViewById(R.id.txtUser)
    }

    class BotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtBot: TextView = itemView.findViewById(R.id.txtBot)
    }
}