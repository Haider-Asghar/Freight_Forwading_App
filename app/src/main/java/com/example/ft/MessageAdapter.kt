package com.example.ft

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(private var list: List<MessageModel>,  private val currentUser: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class RightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val msg = view.findViewById<TextView>(R.id.tvMsg)
        val time = view.findViewById<TextView>(R.id.tvTime)
    }

    inner class LeftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val msg = view.findViewById<TextView>(R.id.tvMsg)
        val time = view.findViewById<TextView>(R.id.tvTime)
    }

    inner class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date = view.findViewById<TextView>(R.id.tvDate)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            list[position].isDate -> 2
            list[position].sender == currentUser -> 1
            else -> 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType) {

            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_right, parent, false)
                RightViewHolder(view)
            }

            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_left, parent, false)
                LeftViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_date, parent, false)
                DateViewHolder(view)
            }
        }
    }

    override fun getItemCount() = list.size

    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = list[position]

        when (holder) {

            is DateViewHolder -> {
                holder.date.text = item.message
            }

            is RightViewHolder -> {
                val imageView = holder.itemView.findViewById<ImageView>(R.id.imgMessage)
                val fileIcon = holder.itemView.findViewById<ImageView>(R.id.imgFileIcon)
                if (item.type == "image") {
                    holder.msg.visibility = View.GONE
                    imageView.visibility = View.VISIBLE

                    Glide.with(holder.itemView.context)
                        .load(item.fileUrl)
                        .into(imageView)

                } else if (item.type == "file") {
                    holder.msg.visibility = View.VISIBLE
                    imageView.visibility = View.GONE
                    fileIcon.visibility = View.VISIBLE
                    holder.msg.text = " Document"

                    holder.msg.setOnClickListener {
                        openFile(holder.itemView.context, item.fileUrl)
                    }

                } else {
                    holder.msg.visibility = View.VISIBLE
                    imageView.visibility = View.GONE
                    fileIcon.visibility = View.GONE
                    holder.msg.text = item.message
                }
                holder.time.text = if (item.time != 0L)
                    timeFormat.format(Date(item.time)) else ""

                if (item.type == "image") {
                    imageView.setOnClickListener {
                        openImage(holder.itemView.context, item.fileUrl)
                    }
                } else {
                    imageView.setOnClickListener(null)
                }
            }

            is LeftViewHolder -> {
                val imageView = holder.itemView.findViewById<ImageView>(R.id.imgMessage)
                val fileIcon = holder.itemView.findViewById<ImageView>(R.id.imgFileIcon)

                if (item.type == "image") {
                    holder.msg.visibility = View.GONE
                    imageView.visibility = View.VISIBLE

                    Glide.with(holder.itemView.context)
                        .load(item.fileUrl)
                        .into(imageView)

                } else if (item.type == "file") {
                    holder.msg.visibility = View.VISIBLE
                    imageView.visibility = View.GONE
                    fileIcon.visibility = View.VISIBLE
                    holder.msg.text = " Document"

                    holder.msg.setOnClickListener {
                        openFile(holder.itemView.context, item.fileUrl)
                    }

                } else {
                    holder.msg.visibility = View.VISIBLE
                    imageView.visibility = View.GONE
                    fileIcon.visibility = View.GONE
                    holder.msg.text = item.message
                }

                holder.time.text = if (item.time != 0L)
                    timeFormat.format(Date(item.time)) else ""

                if (item.type == "image") {
                    imageView.setOnClickListener {
                        openImage(holder.itemView.context, item.fileUrl)
                    }
                } else {
                    imageView.setOnClickListener(null)
                }
            }
        }
    }

    fun updateList(newList: List<MessageModel>) {
        list = newList
        notifyDataSetChanged()
    }

    private fun openFile(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(Intent.createChooser(intent, "Open file with"))
        } catch (e: Exception) {
            android.widget.Toast.makeText(context, "No app found to open this file", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun openImage(context: Context, url: String) {
        val intent = Intent(context, Image_Preview_Activity::class.java)
        intent.putExtra("url", url)
        context.startActivity(intent)
    }
}