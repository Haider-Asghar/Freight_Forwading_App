package com.example.ft

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DocAdapter(private var list: List<MessageModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_DATE = 0
        const val TYPE_DOC = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].isDate) TYPE_DATE else TYPE_DOC
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_DATE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
            DateViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doc, parent, false)
            DocViewHolder(view)
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = list[position]

        if (holder is DateViewHolder) {
            holder.date.text = item.message
        }

        if (holder is DocViewHolder) {

            holder.name.text = item.message

            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            holder.time.text = sdf.format(Date(item.time))

            // 🔥 PDF Thumbnail
            loadPdfThumbnail(holder.thumbnail, item.fileUrl)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, Pdf_Preview::class.java)
                intent.putExtra("url", item.fileUrl)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    inner class DocViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvFileName)
        val time: TextView = view.findViewById(R.id.tvTime)
        val thumbnail: ImageView = view.findViewById(R.id.imgThumb)
    }

    inner class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.tvDate)
    }

    fun updateList(newList: List<MessageModel>) {
        list = newList
        notifyDataSetChanged()
    }

    // 🔥 REAL PDF THUMBNAIL
    private fun loadPdfThumbnail(imageView: ImageView, url: String) {

        CoroutineScope(Dispatchers.IO).launch {

            try {
                val file = File.createTempFile("temp", ".pdf")

                val input = URL(url).openStream()
                val output = FileOutputStream(file)

                input.copyTo(output)
                input.close()
                output.close()

                val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                val renderer = PdfRenderer(pfd)

                val page = renderer.openPage(0)

                val bitmap = Bitmap.createBitmap(
                    page.width,
                    page.height,
                    Bitmap.Config.ARGB_8888
                )

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                page.close()
                renderer.close()

                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(bitmap)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    imageView.setImageResource(R.drawable.pdf_icon)
                }
            }
        }
    }
}