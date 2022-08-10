package com.dv.comfortly.ui.ext

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showDialog(
    @StringRes title: Int? = null,
    titleText: String? = null,
    @StringRes message: Int? = null,
    @StringRes positiveButtonText: Int? = null,
    positiveButtonListener: () -> Unit = {},
    @StringRes negativeButtonText: Int? = null,
    negativeButtonListener: () -> Unit = {},
) = AlertDialog.Builder(this).apply {
    title?.let { setTitle(it) } ?: titleText?.let { setTitle(it) }
    message?.let { setMessage(it) }
    positiveButtonText?.let {
        setPositiveButton(it) { _, _ ->
            positiveButtonListener()
        }
    }
    negativeButtonText?.let {
        setNegativeButton(it) { _, _ ->
            negativeButtonListener()
        }
    }
    show()
}

fun Context.showToast(
    @StringRes text: Int
) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)
