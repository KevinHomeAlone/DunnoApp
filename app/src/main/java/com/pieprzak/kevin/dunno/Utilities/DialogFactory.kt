package com.pieprzak.kevin.dunno.Utilities

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.pieprzak.kevin.dunno.R
import kotlinx.android.synthetic.main.custom_dialog_add_question.view.*

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

    fun buildNoInternetDialog(activity: Activity, retryAction: () -> Unit): MaterialDialog {
        return MaterialDialog.Builder(activity)
                .title(R.string.no_internet_connection)
                .cancelable(false)
                .neutralText(R.string.try_again)
                .onNeutral { dialog, _ ->
                    dialog.dismiss()
                    retryAction()
                }
                .negativeText(R.string.cancel)
                .onNegative { dialog, _ ->
                    dialog.dismiss()
                }
                .build()
    }

    fun showNoInternetDialog(activity: Activity, retryAction: () -> Unit): MaterialDialog {
        return MaterialDialog.Builder(activity)
                .title(R.string.no_internet_connection)
                .cancelable(false)
                .neutralText(R.string.try_again)
                .onNeutral { dialog, _ ->
                    dialog.dismiss()
                    retryAction()
                }
                .negativeText(R.string.cancel)
                .onNegative { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    fun buildAddQuestionDialog(activity: Activity): MaterialDialog{
        return MaterialDialog.Builder(activity)
                .title(R.string.add_dunno)
                .customView(R.layout.custom_dialog_add_question, true)
                /*.positiveText(R.string.accept)
                .onPositive { dialog, _ ->

                    if(Validators.validateAnyTextField(dialog.context, titleTextView)
                    && Validators.validateAnyTextField(dialog.context, contentTextView))
                        acceptAction(titleTextView.text.toString(), contentTextView.text.toString())
                }*/
                .build()
    }

}