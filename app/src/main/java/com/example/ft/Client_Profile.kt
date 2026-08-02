package com.example.ft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Client_Profile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()

        val imgIvBackClientProfile = findViewById<ImageView>(R.id.img_ivBackClientProfile)
        imgIvBackClientProfile.setOnClickListener{
            finish()
        }

        val rlClientProfile = findViewById<RelativeLayout>(R.id.rl_updateClientProfile)
        rlClientProfile.setOnClickListener{
            val intent = Intent(this, Update_Client_Profile::class.java)
            startActivity(intent)
        }

        val rlChangeClientPassword = findViewById<RelativeLayout>(R.id.rl_changeClientPassword)
        rlChangeClientPassword.setOnClickListener{
            showChangePasswordDialog()
        }

        val rlClientAboutUs = findViewById<RelativeLayout>(R.id.rl_clientAboutUs)
        rlClientAboutUs.setOnClickListener{
            val intent = Intent(this, About_Us::class.java)
            startActivity(intent)
        }

        val rlClientFeedback = findViewById<RelativeLayout>(R.id.rl_clientFeedBack)
        rlClientFeedback.setOnClickListener{
            val intent = Intent(this, FeedBack_Review::class.java)
            startActivity(intent)
        }

        val rlClientLogOut = findViewById<RelativeLayout>(R.id.rl_clientLogOut)
        rlClientLogOut.setOnClickListener{
            auth.signOut()
            Toast.makeText(this,"Logged out successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Client_Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
    private fun showChangePasswordDialog() {

        val dialogView = layoutInflater.inflate(R.layout.change_password, null)

        val currentPassword = dialogView.findViewById<TextInputEditText>(R.id.currentPassword)
        val newPassword = dialogView.findViewById<TextInputEditText>(R.id.newPassword)
        val confirmPassword = dialogView.findViewById<TextInputEditText>(R.id.confirmNewPassword)
        val btnChangePassword = dialogView.findViewById<Button>(R.id.btnChangePassword)

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
        btnChangePassword.setOnClickListener {

            val current = currentPassword.text.toString().trim()
            val newPass = newPassword.text.toString().trim()
            val confirm = confirmPassword.text.toString().trim()

            // 🔒 Disable button
            btnChangePassword.isEnabled = false
            btnChangePassword.alpha = 0.5f

            // ❌ Empty check
            if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                enableButton(btnChangePassword)
                return@setOnClickListener
            }
            if (newPass != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                enableButton(btnChangePassword)
                return@setOnClickListener
            }

            val user = auth.currentUser

            if (user != null && user.email != null) {

                val credential = com.google.firebase.auth.EmailAuthProvider
                    .getCredential(user.email!!, current)

                // 🔐 Re-authenticate
                user.reauthenticate(credential)
                    .addOnSuccessListener {

                        // ✅ Update password
                        user.updatePassword(newPass)
                            .addOnSuccessListener {

                                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()

                                // clear fields
                                currentPassword.text?.clear()
                                newPassword.text?.clear()
                                confirmPassword.text?.clear()

                                dialog.dismiss()
                            }.addOnFailureListener {
                                Toast.makeText(this, "Password update failed", Toast.LENGTH_SHORT).show()
                                enableButton(btnChangePassword)
                            }

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show()
                        enableButton(btnChangePassword)
                    }

            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                enableButton(btnChangePassword)
            }
        }
    }
    private fun enableButton(button: Button) {
        button.isEnabled = true
        button.alpha = 1f
    }
}