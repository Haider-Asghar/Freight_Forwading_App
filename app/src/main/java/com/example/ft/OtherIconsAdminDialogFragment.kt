package com.example.ft

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class OtherIconsAdminDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.other_icons_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setCanceledOnTouchOutside(true)

        val root = view.findViewById<View>(R.id.flOtherIconsAdmin)
        root.setOnClickListener {
            dismiss()
        }

        val cvOtherIconsAdmin = view.findViewById<View>(R.id.cvOtherIconsAdmin)
        cvOtherIconsAdmin.setOnClickListener { }
        val cvChatsAdmin = view.findViewById<View>(R.id.cvChatsAdmin)
        cvChatsAdmin.setOnClickListener {
            val intent = Intent(requireContext(),Admin_Chats::class.java)
            startActivity(intent)
            dismiss()
        }
        val cvDocScannerAdmin = view.findViewById<View>(R.id.cvDocScannerAdmin)
        cvDocScannerAdmin.setOnClickListener {
            val intent = Intent(requireContext(), Scanner_Activity::class.java)
            startActivity(intent)
            dismiss()
        }
        val cvDoStatusAdmin = view.findViewById<View>(R.id.cvDoStatusAdmin)
        cvDoStatusAdmin.setOnClickListener{
            val intent = Intent(requireContext(), Admin_Do::class.java)
            startActivity(intent)
            dismiss()
        }
        val cvInVoicesAdmin = view.findViewById<View>(R.id.cvInVoicesAdmin)
        cvInVoicesAdmin.setOnClickListener{
            val intent = Intent(requireContext(), Admin_Invoices::class.java)
            startActivity(intent)
            dismiss()
        }
        val cvProfileAdmin = view.findViewById<View>(R.id.cvProfileAdmin)
        cvProfileAdmin.setOnClickListener{
            val intent = Intent(requireContext(), AdminProfile::class.java)
            startActivity(intent)
            dismiss()
        }
        val cvRatesManagementAdmin = view.findViewById<View>(R.id.cvRatesManagementAdmin)
        cvRatesManagementAdmin.setOnClickListener{
            val intent = Intent(requireContext(), Rates_Manager::class.java)
            startActivity(intent)
            dismiss()
        }
        val cvStatusesAdmin = view.findViewById<View>(R.id.cvStatusesAdmin)
        cvStatusesAdmin.setOnClickListener{
            val intent = Intent(requireContext(), Status_Updates::class.java)
            startActivity(intent)
            dismiss()
        }
        val cvTrackerAdmin = view.findViewById<View>(R.id.cvTrackerAdmin)
        cvTrackerAdmin.setOnClickListener{
            val intent = Intent(requireContext(), Tracking_Activity::class.java)
            startActivity(intent)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }
}