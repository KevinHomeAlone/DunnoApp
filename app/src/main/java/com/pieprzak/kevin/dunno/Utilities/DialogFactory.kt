package com.pieprzak.kevin.dunno.Utilities

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.pieprzak.kevin.dunno.R

object DialogFactory {

    fun buildProgressDialog(activity: Activity, title: String, content: String): MaterialDialog {
        return MaterialDialog.Builder(activity)
                .title(title)
                .content(content)
                .cancelable(false)
                .progress(true, 0)
                .build()
    }

    fun showErrorDialog(activity: Activity, exception: Exception) {
        MaterialDialog.Builder(activity)
                .title(activity.getString(R.string.error))
                .content(exception.localizedMessage)
                .neutralText(activity.getString(R.string.ok))
                .show()
    }

}