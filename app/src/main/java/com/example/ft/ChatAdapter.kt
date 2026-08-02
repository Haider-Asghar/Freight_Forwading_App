package com.example.ft

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter(private var list: List<ChatModel>, private val onClick: (ChatModel) -> Unit) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.tvName)
        val message = view.findViewById<TextView>(R.id.tvMessage)
        val time = view.findViewById<TextView>(R.id.tvTime)
        val count = view.findViewById<TextView>(R.id.tvCount)
        val profile: TextView = view.findViewById(R.id.tvProfile)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(view)
    }

    fun updateList(newList: List<ChatModel>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    private val sdfTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
    private val sdfDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = list[position]

        holder.name.text = item.userName
        holder.message.text = if (item.lastMessage.isNotEmpty())
            item.lastMessage
        else
            "Start chat..."

        if (item.time == 0L) {
            holder.time.text = ""
        } else {


            val today = android.text.format.DateUtils.isToday(item.time)

            holder.time.text = if (today) {
                sdfTime.format(Date(item.time))
            } else {
                sdfDate.format(Date(item.time))
            }
        }
        holder.profile.text =
            item.userName.trim().takeIf { it.isNotEmpty() }
                ?.first()?.uppercase()
                ?: "?"

        holder.itemView.setOnClickListener {
            it.isEnabled = false
            onClick(item)
            it.postDelayed({ it.isEnabled = true }, 500)
        }
        if (item.unreadCount > 0) {

            holder.count.visibility = View.VISIBLE
            holder.count.text = item.unreadCount.toString()

            holder.message.setTypeface(null, Typeface.BOLD)
            holder.time.setTypeface(null, Typeface.BOLD)

        } else {
            holder.count.visibility = View.GONE

            holder.message.setTypeface(null, Typeface.NORMAL)
            holder.time.setTypeface(null, Typeface.NORMAL)
        }

    }
}