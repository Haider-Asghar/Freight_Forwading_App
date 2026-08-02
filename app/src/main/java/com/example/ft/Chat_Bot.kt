package com.example.ft

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Chat_Bot : AppCompatActivity() {

    private lateinit var recyclerChat: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: ImageView

    private lateinit var adapter: BotAdapter

    private val messageList = mutableListOf<BotModel>()

    // 🔥 Gemini API
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = "AIzaSyB_K0Jpu48j8Cfvq54lmlqk8S4L0ZfJUlo"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_bot)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerChat = findViewById(R.id.recyclerChat)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)

        adapter = BotAdapter(messageList)

        recyclerChat.layoutManager = LinearLayoutManager(this)
        recyclerChat.adapter = adapter

        btnSend.setOnClickListener {

            val userMessage = etMessage.text.toString().trim()

            if (userMessage.isNotEmpty()) {

                addMessage(userMessage, "user")

                etMessage.text.clear()

                askGemini(userMessage)
            }
        }
    }
    private fun addMessage(message: String, role: String) {

        messageList.add(BotModel(message, role))

        adapter.notifyItemInserted(messageList.size - 1)

        recyclerChat.scrollToPosition(messageList.size - 1)
    }
    private fun askGemini(question: String) {

        CoroutineScope(Dispatchers.IO).launch {

            try {

                // 🔥 Freight-only restriction
                val prompt = """
                    
You are a Freight Forwarding AI Assistant.

RULES:

1. Only answer freight forwarding and logistics related questions.
2. Topics allowed:
   - Air Freight
   - Sea Freight
   - Customs Clearance
   - Import Export
   - MAWB
   - HAWB
   - FCL
   - LCL
   - Cargo
   - Shipping
   - Logistics
   - Transit Time
   - Incoterms
   - Container
   - Freight Rates
   
3. If user asks unrelated questions like movies, cricket, politics, coding, religion etc then reply ONLY:

"I only answer freight forwarding related questions."

4. Keep answers short and professional.

User Question:
$question

                """.trimIndent()

                val response = generativeModel.generateContent(prompt)

                val botReply = response.text ?: "No response"
                
                withContext(Dispatchers.Main) {

                    addMessage(botReply, "bot")
                }

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {

                    addMessage(
                        "Error: ${e.message}",
                        "bot"
                    )
                }
            }
        }
    }
}