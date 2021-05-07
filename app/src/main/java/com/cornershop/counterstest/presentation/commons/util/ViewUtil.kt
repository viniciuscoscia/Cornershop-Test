package com.cornershop.counterstest.presentation.commons.util

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.cornershop.counterstest.R

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Context?.showGenericErrorDialog() {
    if (this == null) return
    with(AlertDialog.Builder(this)) {
        setMessage(R.string.generic_error_description)
        setPositiveButton(R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
    }
}