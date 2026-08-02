package com.example.ft

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    private lateinit var adapter: MessageAdapter
    private val list = mutableListOf<MessageModel>()
    private lateinit var storage: FirebaseStorage
    private lateinit var fileUri: Uri
    private lateinit var jobType: String
    private lateinit var refNoGlobal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        val refNo = intent.getStringExtra("refNo")
        val name = intent.getStringExtra("chatName") ?: "User"

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvProfile = findViewById<TextView>(R.id.tvProfile)

        tvName.text = name
        tvProfile.text = name.trim().takeIf { it.isNotEmpty() }
            ?.first()?.uppercase()
            ?: "?"
        if (refNo.isNullOrEmpty()) {
            finish()
            return
        }

        val type = if(refNo.startsWith("IMP")) "Import" else "Export"
        jobType = type
        refNoGlobal = refNo
        val rv = findViewById<RecyclerView>(R.id.rvMessages)
        val et = findViewById<EditText>(R.id.etMessage)
        val btn = findViewById<ImageView>(R.id.btnSend)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        rv.layoutManager = layoutManager
        adapter = MessageAdapter(list, "client")
        rv.adapter = adapter
        rv.setHasFixedSize(true)
        rv.itemAnimator = null

        loadMessages(type, refNo)
        resetUnread(type, refNo)
        btn.setOnClickListener {
            val msg = et.text.toString().trim()
            if (msg.isEmpty())
                return@setOnClickListener
            sendMessage(type, refNo, msg)
            et.setText("")
            rv.post {
                rv.scrollToPosition(list.size - 1)
            }
        }
        et.addTextChangedListener {
            btn.alpha = if (it.toString().trim().isEmpty()) 0.5f else 1f
        }
        storage = FirebaseStorage.getInstance()

        val btnAttach = findViewById<ImageView>(R.id.btnAttach)
        val btnCamera = findViewById<ImageView>(R.id.btnCamera)

        btnAttach.setOnClickListener {
            pickFile()
        }

        btnCamera.setOnClickListener {
            showImagePickerDialog()
        }
    }
    private fun sendMessage(type: String, refNo: String, text: String) {
        val db = FirebaseFirestore.getInstance()

        val message = hashMapOf(
            "sender" to "client",
            "message" to text,
            "time" to FieldValue.serverTimestamp()
        )
        val ref = db.collection("Jobs").document(type).collection("Reference Number").document(refNo)

        ref.collection("Messages").add(message)

        ref.update(mapOf(
            "lastMessage" to text,
            "lastTime" to FieldValue.serverTimestamp(),
            "unreadCountAdmin" to FieldValue.increment(1))
        )

    }

    private fun loadMessages(type: String, refNo: String) {
        val db = FirebaseFirestore.getInstance()
        val rv = findViewById<RecyclerView>(R.id.rvMessages)

        db.collection("Jobs").document(type).collection("Reference Number").document(refNo)
            .collection("Messages").orderBy("time", com.google.firebase.firestore.Query.Direction.ASCENDING).addSnapshotListener { value, _ ->
                if (value == null) return@addSnapshotListener
                list.clear()

                var lastDate = ""
                for (doc in value) {
                    val time = doc.getTimestamp("time")?.toDate()?.time ?: 0
                    val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(time))
                    if (dateStr != lastDate) {
                        val label = if (android.text.format.DateUtils.isToday(time)) "Today" else dateStr

                        list.add(MessageModel("", label, 0, true))
                        lastDate = dateStr
                    }

                    val sender = doc.getString("sender") ?: continue
                    val message = doc.getString("message") ?: ""
                    val typeMsg = doc.getString("type") ?: "text"
                    val fileUrl = doc.getString("fileUrl") ?: ""

                    list.add(MessageModel(sender, message, time, false, typeMsg, fileUrl))
                }

                adapter.updateList(list)
                rv.post {
                    rv.scrollToPosition(list.size - 1)
                }
            }
    }
    private fun resetUnread(type: String, refNo: String) {

        FirebaseFirestore.getInstance()
            .collection("Jobs")
            .document(type)
            .collection("Reference Number")
            .document(refNo)
            .update("unreadCountClient", 0)
    }
    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                fileUri = it
                uploadFile("file")
            }
        }

    private fun pickFile() {
        filePickerLauncher.launch("*/*") // all types
    }
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                val uri = ImageUtils.getImageUri(this, it)
                fileUri = uri
                uploadFile("image")
            }
        }

    private fun openCamera() {
        cameraLauncher.launch(null)
    }
    private fun uploadFile(type: String) {

        val ref = storage.reference.child("chat_files/${System.currentTimeMillis()}")

        ref.putFile(fileUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { url ->
                    sendFileMessage(type, url.toString())
                }
            }
    }
    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                openCamera()
            }
        }
    private fun sendFileMessage(fileType: String, url: String) {

        val db = FirebaseFirestore.getInstance()

        val message = hashMapOf(
            "sender" to "client",
            "message" to "",
            "fileUrl" to url,
            "type" to fileType,
            "time" to FieldValue.serverTimestamp()
        )

        val ref = db.collection("Jobs").document(jobType).collection("Reference Number").document(refNoGlobal)

        ref.collection("Messages").add(message)

        ref.update(mapOf(
                "lastMessage" to if (fileType == "image") "📷 Image" else "📄 Document",
                "lastTime" to FieldValue.serverTimestamp(),
                "unreadCountAdmin" to FieldValue.increment(1))
        )
    }
    private fun showImagePickerDialog() {
        val options = arrayOf("Camera", "Gallery")

        android.app.AlertDialog.Builder(this)
            .setTitle("Select Image")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    1 -> pickImageFromGallery()
                }
            }
            .show()
    }
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                fileUri = it
                uploadFile("image")
            }
        }

    private fun pickImageFromGallery() {
        imagePickerLauncher.launch("image/*")
    }
    object ImageUtils {

        fun getImageUri(context: Context, bitmap: Bitmap): Uri {
            val path = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                bitmap,
                "IMG_${System.currentTimeMillis()}",
                null
            )
            return Uri.parse(path)
        }
    }
}