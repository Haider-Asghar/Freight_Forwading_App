    package com.example.ft

    import android.content.Intent
    import android.graphics.Color
    import android.graphics.drawable.ColorDrawable
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.DialogFragment
    import eightbitlab.com.blurview.RenderScriptBlur


    class OtherIconsDialogFragment : DialogFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setStyle(STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar)
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.other_icons, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val companyName = arguments?.getString("COMPANY_NAME")

            val blurViewClient = view.findViewById<eightbitlab.com.blurview.BlurView>(R.id.blurViewClient)
            val decorView = requireActivity().window.decorView
            val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
            val windowBackground = decorView.background

            blurViewClient.setupWith(rootView, RenderScriptBlur(requireContext())).setFrameClearDrawable(windowBackground).setBlurRadius(18f)

            val root = view.findViewById<View>(R.id.flOtherIcons)
            root.setOnClickListener{
                dismiss()
            }

            val cvOtherIcons = view.findViewById<View>(R.id.cvOtherIcons)
            cvOtherIcons.setOnClickListener{ }
            val cvAiAssistant = view.findViewById<View>(R.id.cvAiAssistant)
            cvAiAssistant.setOnClickListener{ }
            val cvChats = view.findViewById<View>(R.id.cvChats)
            cvChats.setOnClickListener{
                val intent = Intent(requireContext(), Client_Chats::class.java)
                intent.putExtra("COMPANY_NAME", companyName)
                startActivity(intent)
                dismiss()
            }
            val cvDocScanner = view.findViewById<View>(R.id.cvDocScanner)
            cvDocScanner.setOnClickListener{
                val intent = Intent(requireContext(), Scanner_Activity::class.java)
                intent.putExtra("role", "client")
                intent.putExtra("companyName", companyName)
                startActivity(intent)
                dismiss()
            }
            val cvDoStatus = view.findViewById<View>(R.id.cvDoStatus)
            cvDoStatus.setOnClickListener{
                val intent = Intent(requireContext(), Client_Do::class.java)
                startActivity(intent)
                dismiss()
            }
            val cvFeedBack = view.findViewById<View>(R.id.cvFeedBack)
            cvFeedBack.setOnClickListener{
                val intent = Intent(requireContext(), FeedBack_Review::class.java)
                startActivity(intent)
                dismiss()
            }
            val cvInVoices = view.findViewById<View>(R.id.cvInVoices)
            cvInVoices.setOnClickListener{
                val intent = Intent(requireContext(), Client_Invoices::class.java)
                startActivity(intent)
                dismiss()
            }
            val cvProfile = view.findViewById<View>(R.id.cvProfile)
            cvProfile.setOnClickListener{
                val intent = Intent(requireContext(), Client_Profile::class.java)
                startActivity(intent)
                dismiss()
            }
            val cvRates = view.findViewById<View>(R.id.cvRates)
            cvRates.setOnClickListener{
                val intent = Intent(requireContext(), Client_Rates_Query::class.java)
                startActivity(intent)
                dismiss()
            }
            val  cvStatuses = view.findViewById<View>(R.id.cvStatuses)
            cvStatuses.setOnClickListener{
                val intent = Intent(requireContext(), View_Statuses::class.java)
                startActivity(intent)
                dismiss()
            }
            val cvTracker = view.findViewById<View>(R.id.cvTracker)
            cvTracker.setOnClickListener{
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
                   setWindowAnimations(R.style.BottomDialogAnimation)
            }
        }
        companion object {
            fun newInstance(companyName: String): OtherIconsDialogFragment {
                val fragment = OtherIconsDialogFragment()
                val bundle = Bundle()
                bundle.putString("COMPANY_NAME", companyName)
                fragment.arguments = bundle
                return fragment
            }
        }
    }