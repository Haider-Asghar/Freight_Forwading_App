package com.example.ft

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class SecretKeyDialogFragment(private val onVerified: () -> Unit) : DialogFragment() {

    private var attemptsLeft = 3
    private var lockSeconds = 30
    private var countdownRunnable: Runnable? = null
    private var isLocked = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.secret_key_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etSecretKey = view.findViewById<TextInputEditText>(R.id.adminSecretKey)
        val tilSecretKey = view.findViewById<TextInputLayout>(R.id.adminSecretKeyLayout)
        val progressBar = view.findViewById<ProgressBar>(R.id.pbAdminSecretKey)
        val tvCountDown = view.findViewById<TextView>(R.id.tvCountDown)
        val btnVerify = view.findViewById<Button>(R.id.btnSecretKey)
        btnVerify.setOnClickListener{
            val enteredKey = etSecretKey.text.toString().trim()
            if (enteredKey.isEmpty()) {
                tilSecretKey.error = "Enter Secret Key"
                return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE
            btnVerify.isEnabled = false
            verifyFromFirestore(enteredKey, tilSecretKey, etSecretKey, progressBar, btnVerify, tvCountDown)
        }
        etSecretKey.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                tilSecretKey.error = null
            }
        }
        etSecretKey.setOnClickListener {
            tilSecretKey.error = null
        }
        etSecretKey.addTextChangedListener {
            tilSecretKey.clearAnimation()
            tilSecretKey.error = null
        }
        requireDialog().setOnKeyListener { _, keyCode, event ->
            if(keyCode == android.view.KeyEvent.KEYCODE_BACK && event.action == android.view.KeyEvent.ACTION_UP)
            {
                if(isLocked)
                {
                    true
                }
                else
                {
                    dismiss()
                    true
                }
            }
            else
            {
                false
            }
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(false)
    }
    private fun verifyFromFirestore(enteredKey: String, tilSecretKey: TextInputLayout, etSecretKey: TextInputEditText, progressBar: ProgressBar,
                                     btnVerify: Button, tvCountDown: TextView) {
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        db.collection("app_config").document("security").get().addOnSuccessListener { document ->
            val realKey = document.getString("admin_secret_key")
            if (enteredKey == realKey) {
                dismiss()
                onVerified()
            }
            else {
                progressBar.visibility = View.GONE
                btnVerify.isEnabled = true
                attemptsLeft--
                tilSecretKey.error = "Wrong key. Attempts left: $attemptsLeft"
                shakeView(etSecretKey)
                if (attemptsLeft == 0) {
                    lockDialog(tilSecretKey, etSecretKey, btnVerify, tvCountDown)
                }
            }
        }.addOnFailureListener {
            progressBar.visibility = View.GONE
            btnVerify.isEnabled = true
            tilSecretKey.error = "Network Error. Try again."
        }
    }
    private fun shakeView(view: View) {
        view.animate()
            .translationX(16f)
            .setDuration(40)
            .withEndAction {
                view.animate().translationX(-16f).setDuration(40).withEndAction {
                    view.animate().translationX(16f).setDuration(40).withEndAction {
                        view.animate().translationX(0f).setDuration(40).start()
                    }.start()
                }.start()
            }.start()
    }
    private fun lockDialog(tilSecretKey: TextInputLayout, etSecretKey: TextInputEditText, btnVerify: Button, tvCountDown: TextView){
        isLocked = true
        isCancelable = false
        dialog?.setCanceledOnTouchOutside(false)
        tilSecretKey.error = "Too many attempts. Locked!"
        etSecretKey.isEnabled = false
        btnVerify.isEnabled = false
        tvCountDown.visibility = View.VISIBLE
        var secondsLeft  = lockSeconds
        tvCountDown.text = "Try again in ${secondsLeft}s"

        val handler = Handler(Looper.getMainLooper())

        countdownRunnable = object : Runnable {
            override fun run(){
                secondsLeft--
                if(secondsLeft > 0){
                    tvCountDown.text = "Try again in ${secondsLeft}s"
                    handler.postDelayed(this, 1000)
                }
                else
                {
                    attemptsLeft = 3
                    isLocked = false
                    isCancelable = true
                    dialog?.setCanceledOnTouchOutside(false)
                    tilSecretKey.error = null
                    etSecretKey.isEnabled = true
                    btnVerify.isEnabled = true
                    tvCountDown.visibility = View.GONE
                }
            }
        }
        handler.postDelayed(countdownRunnable!!, 1000)
    }
}