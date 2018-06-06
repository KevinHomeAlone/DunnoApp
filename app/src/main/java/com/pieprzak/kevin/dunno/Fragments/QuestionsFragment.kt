package com.pieprzak.kevin.dunno.Fragments


import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.pieprzak.kevin.dunno.Adapters.QuestionsRecyclerAdapter
import com.pieprzak.kevin.dunno.Model.Question

import com.pieprzak.kevin.dunno.R
import com.pieprzak.kevin.dunno.Utilities.DialogFactory
import com.pieprzak.kevin.dunno.Utilities.ServerConnection
import com.pieprzak.kevin.dunno.Utilities.SharedPreferencesConnection
import com.pieprzak.kevin.dunno.Utilities.Validators
import kotlinx.android.synthetic.main.fragment_questions.*
import kotlinx.android.synthetic.main.fragment_questions.view.*
import kotlinx.android.synthetic.main.custom_dialog_add_question.view.*


class QuestionsFragment : Fragment() {

    private var listOfQuestions: ArrayList<Question> = ArrayList()
    private var progressDialogLoadingQuestions: MaterialDialog? = null
    private var progressDialogAddingQuestion: MaterialDialog? = null
    private var addQuestionDialog : MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialogLoadingQuestions = DialogFactory.buildProgressDialog(activity,
                getString(R.string.loading_dunnos), getString(R.string.please_wait))
        progressDialogAddingQuestion = DialogFactory.buildProgressDialog(activity,
                getString(R.string.adding_dunno), getString(R.string.please_wait))
        addQuestionDialog = DialogFactory.buildAddQuestionDialog(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_questions, container, false)

        view.swipeContainerQuestions.setOnRefreshListener {
            getQuestionsFromServer()
        }

        addQuestionDialog!!.customView!!.acceptTextViewAddQuestionDialog.setOnClickListener{
            val titleTextView = addQuestionDialog!!.customView!!.titleAddQuestionDialog
            val contentTextView = addQuestionDialog!!.customView!!.contentAddQuestionDialog

            if(Validators.validateAnyTextField(addQuestionDialog!!.context, titleTextView)
                    && Validators.validateAnyTextField(addQuestionDialog!!.context, contentTextView)){
                val userName = SharedPreferencesConnection.getLogin(activity)
                val title = titleTextView.text.toString()
                val content = contentTextView.text.toString()
                val question = Question(null, title, content, null, null,
                        userName)
                Log.d("Created question: ", "$title $content $userName")
                //titleTextView.text.clear()
                //contentTextView.text.clear()
                addQuestionDialog!!.hide()
                addQuestionToServer(question)
            }
        }

        view.fabQuestions.setOnClickListener{
            addQuestionDialog!!.show()
        }

        if (listOfQuestions.isEmpty()) {
            getQuestionsFromServer()
        } else {
            view.recyclerViewQuestions.layoutManager = LinearLayoutManager(activity)
            view.recyclerViewQuestions.adapter =
                    QuestionsRecyclerAdapter(listOfQuestions, this)
        }

        // Inflate the layout for this fragment
        return view
    }

    private fun getQuestionsFromServer() {
        if (swipeContainerQuestions == null){
            progressDialogLoadingQuestions!!.show()
        }else if(!swipeContainerQuestions.isRefreshing)
            swipeContainerQuestions.isRefreshing = true

        ServerConnection.checkInternetConnection(activity, {
            if (activity != null) {
                ServerConnection.getAllQuestions({ listOfQuestions ->
                    this.listOfQuestions = listOfQuestions
                    this.listOfQuestions.sortWith(compareBy{it.title})
                    if (recyclerViewQuestions != null) {
                        recyclerViewQuestions.layoutManager = LinearLayoutManager(activity)
                        recyclerViewQuestions.adapter = QuestionsRecyclerAdapter(listOfQuestions, this)
                    }
                    dismissLoading()
                }, { exception ->
                    dismissLoading()
                    DialogFactory.showErrorDialog(activity, exception)
                })
            }
        }, {
            dismissLoading()
            DialogFactory.showNoInternetDialog(activity,{
                getQuestionsFromServer()
            })
        })
    }

    fun addQuestionToServer(question: Question){
        progressDialogAddingQuestion!!.show()
        ServerConnection.checkInternetConnection(activity, {
            if (activity != null) {
                ServerConnection.addQuestion(question, {
                    progressDialogAddingQuestion!!.hide()
                    getQuestionsFromServer()
                }, { exception ->
                    progressDialogAddingQuestion!!.hide()
                    DialogFactory.showErrorDialog(activity, exception)
                })
            }
        }, {
            progressDialogAddingQuestion!!.hide()
            DialogFactory.showNoInternetDialog(activity, {
                addQuestionToServer(question)
            })
        })
    }

    fun dismissLoading(){
        if (swipeContainerQuestions.isRefreshing)
            swipeContainerQuestions.isRefreshing = false
        if (progressDialogLoadingQuestions != null)
            progressDialogLoadingQuestions!!.dismiss()
    }


}
