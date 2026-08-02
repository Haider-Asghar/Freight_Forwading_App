package com.example.ft

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class Admin_Chats : AppCompatActivity() {

    private lateinit var adapter: ChatAdapter
    private var list = mutableListOf<ChatModel>()
    private val tempMap = mutableMapOf<String, ChatModel>()
    private val listeners = mutableListOf<ListenerRegistration>()
    private lateinit var searchContainer: LinearLayout
    private lateinit var searchView: SearchView
    private lateinit var tvChats: TextView
    private var fullList = mutableListOf<ChatModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_chats)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        searchContainer = findViewById(R.id.searchContainerAdminChats)
        searchView = findViewById(R.id.searchViewAdmin)
        tvChats = findViewById(R.id.tvAdminChats)
// CLICK → OPEN SEARCH
        searchContainer.setOnClickListener {
            searchContainer.visibility = View.GONE
            tvChats.visibility = View.GONE
            searchView.visibility = View.VISIBLE
            searchView.isIconified = false
            searchView.requestFocus()
        }

// CLOSE (X BUTTON)
        searchView.setOnCloseListener {
            closeSearch()
            true
        }
        // SEARCH FILTER
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText ?: "")
                return true
            }
        })
        val rvAdmin = findViewById<RecyclerView>(R.id.rvChatsAdmin)
        rvAdmin.layoutManager = LinearLayoutManager(this)
        adapter = ChatAdapter(list) { chat ->
            val intent = Intent(this, Admin_Chat_Activity::class.java)
            intent.putExtra("refNo", chat.refNo)
            intent.putExtra("chatName", chat.userName)
            startActivity(intent)
        }
        rvAdmin.adapter = adapter

        loadChats()
    }
    private fun loadChats() {
        val db = FirebaseFirestore.getInstance()

        val types = listOf("Import", "Export")
        for (type in types) {
            val listener = db.collection("Jobs").document(type).collection("Reference Number").addSnapshotListener { value, error ->
                    if (error != null) return@addSnapshotListener
                    if (value == null) return@addSnapshotListener
                    for (docChange in value.documentChanges) {
                        val doc = docChange.document
                        val refNo = doc.id

                        when (docChange.type) {

                            com.google.firebase.firestore.DocumentChange.Type.ADDED,
                            com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> {
                                val model = ChatModel(
                                    refNo,
                                    doc.getString("chatName") ?: "",
                                    doc.getString("lastMessage") ?: "",
                                    doc.getTimestamp("lastTime")?.toDate()?.time ?: 0,
                                    doc.getLong("unreadCountAdmin")?.toInt() ?: 0)

                                tempMap[model.refNo] = model

                            }

                            com.google.firebase.firestore.DocumentChange.Type.REMOVED -> {
                                tempMap.remove(refNo)
                            }
                        }
                    }
                    if (value.documentChanges.isNotEmpty()) {
                        updateUI()
                    }
                }
            listeners.add(listener)
        }
    }
    private fun updateUI() {
        list.clear()
        list.addAll(tempMap.values)

        // 🔥 LATEST ON TOP
        list.sortByDescending { it.time }

        fullList.clear()
        fullList.addAll(list)
        adapter.updateList(list)
    }
    override fun onDestroy() {
        super.onDestroy()
        for (l in listeners) l.remove()
    }
    override fun onBackPressed() {
        if (searchView.visibility == View.VISIBLE) {
            closeSearch()
        } else {
            super.onBackPressed()
        }
    }
    private fun closeSearch() {
        searchView.setQuery("", false)
        searchView.clearFocus()
        searchView.visibility = View.GONE
        searchContainer.visibility = View.VISIBLE
        tvChats.visibility = View.VISIBLE
        adapter.updateList(fullList)
    }

    private fun filterList(query: String) {
        val filtered = fullList.filter {
            it.userName.contains(query, ignoreCase = true) ||
                    it.refNo.contains(query, ignoreCase = true)
        }
        adapter.updateList(filtered)
    }
}