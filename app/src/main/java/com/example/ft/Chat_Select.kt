package com.example.ft

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Chat_Select : AppCompatActivity() {

    private lateinit var adapter: ChatAdapter
    private val list = mutableListOf<ChatModel>()
    private val tempMap = mutableMapOf<String, ChatModel>()

    private lateinit var fileUri: String
    private lateinit var role: String
    private var companyName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_select)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fileUri = intent.getStringExtra("fileUri")!!
        role = intent.getStringExtra("role")!!   // "admin" ya "client"
        companyName = intent.getStringExtra("companyName")

        val rv = findViewById<RecyclerView>(R.id.rvChats)
        rv.layoutManager = LinearLayoutManager(this)

        adapter = ChatAdapter(list) { chat ->
            showConfirmDialog(chat)
        }

        rv.adapter = adapter

        loadChats()
    }
    private fun loadChats() {
        val db = FirebaseFirestore.getInstance()

        val types = listOf("Import", "Export")

        for (type in types) {

            db.collection("Jobs").document(type)
                .collection("Reference Number").addSnapshotListener { value, _ ->

                    if (value == null) return@addSnapshotListener

                    for (doc in value.documents) {

                        val refNo = doc.id

                        // ✅ CLIENT FILTER
                        if (role == "client") {

                            if (type == "Import") {
                                val consignee = doc.getString("consignee") ?: continue
                                if (!consignee.equals(companyName, true)) continue
                            } else {
                                val shipper = doc.getString("shipper") ?: continue
                                if (!shipper.equals(companyName, true)) continue
                            }
                        }

                        val model = ChatModel(
                            refNo,
                            doc.getString("chatName") ?: "",
                            doc.getString("lastMessage") ?: "",
                            doc.getTimestamp("lastTime")?.toDate()?.time ?: 0,
                            0
                        )

                        tempMap[refNo] = model
                    }

                    updateUI()
                }
        }
    }
    private fun updateUI() {
        list.clear()
        list.addAll(tempMap.values)
        list.sortByDescending { it.time }
        adapter.updateList(list)
    }
    private fun showConfirmDialog(chat: ChatModel) {
        AlertDialog.Builder(this)
            .setTitle("Send Document")
            .setMessage("Send scanned document to ${chat.userName}?")
            .setPositiveButton("Send") { _, _ ->
                uploadAndSend(chat)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun uploadAndSend(chat: ChatModel) {

        val storage = FirebaseStorage.getInstance()
        val db = FirebaseFirestore.getInstance()

        val uri = Uri.parse(fileUri)

        val ref = storage.reference.child("chat_files/${System.currentTimeMillis()}.pdf")

        ref.putFile(uri).addOnSuccessListener {

            ref.downloadUrl.addOnSuccessListener { url ->

                val type = if (chat.refNo.startsWith("IMP")) "Import" else "Export"

                val sender = if (role == "admin") "admin" else "client"

                val message = hashMapOf(
                    "sender" to sender,
                    "message" to "",
                    "fileUrl" to url.toString(),
                    "type" to "file",
                    "time" to FieldValue.serverTimestamp()
                )

                val docRef = db.collection("Jobs").document(type)
                    .collection("Reference Number").document(chat.refNo)

                docRef.collection("Messages").add(message)

                val unreadField =
                    if (sender == "admin") "unreadCountClient"
                    else "unreadCountAdmin"

                docRef.update(
                    mapOf(
                        "lastMessage" to "📄 Document",
                        "lastTime" to FieldValue.serverTimestamp(),
                        unreadField to FieldValue.increment(1)
                    )
                )

                finish()
            }
        }
    }
}