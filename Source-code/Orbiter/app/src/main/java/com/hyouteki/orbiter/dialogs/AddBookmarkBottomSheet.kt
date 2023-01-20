package com.hyouteki.orbiter.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hyouteki.orbiter.R
import com.hyouteki.orbiter.interfaces.Communicator
import com.hyouteki.orbiter.interfaces.Helper
import com.hyouteki.orbiter.interfaces.Theme
import com.hyouteki.orbiter.models.WebPage
import com.hyouteki.orbiter.viewmodels.MyViewModel

class AddBookmarkBottomSheet : BottomSheetDialogFragment(), Helper {
    private lateinit var handle: View
    private lateinit var nameField: TextInputLayout
    private lateinit var name: TextInputEditText
    private lateinit var saveButton: Button
    private val viewModel: MyViewModel by activityViewModels()
    private lateinit var comm: Communicator

    companion object {
        const val EDIT_MODE = 1
        const val NEW_MODE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_add_bookmark, container, false)
        handle = view.findViewById(R.id.v_bsab_handle)
        nameField = view.findViewById(R.id.til_bsab_name_field)
        name = view.findViewById(R.id.tiet_bsab_name)
        saveButton = view.findViewById(R.id.btn_bsab_save)
        val url = arguments?.getString("url")
        val nameText = arguments?.getString("name")
        val mode = arguments?.getInt("mode")
        comm = activity as Communicator
        handleTheme()
        name.setText(nameText)
        saveButton.setOnClickListener {
            if (name.text.toString() != "") {
                when (mode) {
                    NEW_MODE -> {
                        viewModel.insertWebPage(
                            WebPage(
                                id = 0,
                                name = name.text.toString(),
                                url = url!!,
                                type = "bookmark"
                            )
                        )
                    }
                    EDIT_MODE -> {
                        comm.updateWebPage(name.text.toString())
                    }
                }
                dismiss()
            } else {
                makeToast(requireContext(), "Insert bookmark name")
            }
        }

        return view
    }

    @SuppressLint("ResourceType")
    private fun handleTheme() {
        val color = Theme.getTheme()
        val theme = Theme.getResource()
        handle.setBackgroundDrawable(resources.getDrawable(theme))
        nameField.setStartIconTintList(resources.getColorStateList(color))
        name.setTextColor(resources.getColor(color))
        saveButton.backgroundTintList = resources.getColorStateList(color)
    }
}