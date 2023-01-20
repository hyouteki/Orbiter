package com.hyouteki.orbiter.interfaces

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.hyouteki.orbiter.R

interface Helper {
    fun makeToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun makeSnackBar(view: View, message: String, resources: Resources) {
        Snackbar
            .make(view, message, Snackbar.LENGTH_SHORT)
            .setAnchorView(view)
            .setBackgroundTint(resources.getColor(R.color.colorQuaternary))
            .setTextColor(resources.getColor(R.color.colorTertiary))
            .show()
    }
}