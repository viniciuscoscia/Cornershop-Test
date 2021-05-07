package com.cornershop.counterstest.presentation.commons.errorevent

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.cornershop.counterstest.R

interface DialogErrorEvent : ErrorEvent {
    @get:StringRes
    val errorTitle: Int?
    @get:StringRes
    val errorMessage: Int
}

fun DialogErrorEvent.showErrorDialog(context: Context) {
    with(AlertDialog.Builder(context)) {
        errorTitle?.run {
            setTitle(this)
        }
        setMessage(errorMessage)
        setPositiveButton(R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
    }
}