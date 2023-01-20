package com.hyouteki.orbiter.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.hyouteki.orbiter.R
import com.hyouteki.orbiter.interfaces.Communicator
import com.hyouteki.orbiter.interfaces.Theme

class BookmarkOptionsBottomSheet : BottomSheetDialogFragment() {
    private lateinit var handle: View
    private lateinit var comm: Communicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_bookmark_options, container, false)
        handle = view.findViewById(R.id.v_bsbo_handle)
        val shareButton = view.findViewById<MaterialButton>(R.id.mb_bsbo_share)
        val updateButton = view.findViewById<MaterialButton>(R.id.mb_bsbo_update)
        val removeButton = view.findViewById<MaterialButton>(R.id.mb_bsbo_remove)
        comm = activity as Communicator
        handleTheme()
        shareButton.setOnClickListener {
            comm.shareWebPage()
            dismiss()
        }
        updateButton.setOnClickListener {
            comm.updateWebPage()
            dismiss()
        }
        removeButton.setOnClickListener {
            comm.removeWebPage()
            dismiss()
        }
        return view
    }

    private fun handleTheme() {
        val theme = Theme.getResource()
        handle.setBackgroundDrawable(resources.getDrawable(theme))
    }
}