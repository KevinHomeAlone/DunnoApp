package com.pieprzak.kevin.dunno.Fragments


import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.pieprzak.kevin.dunno.Adapters.AnswerRecyclerAdapter
import com.pieprzak.kevin.dunno.Model.Answer
import com.pieprzak.kevin.dunno.Model.Question

import com.pieprzak.kevin.dunno.R
import com.pieprzak.kevin.dunno.Utilities.*
import kotlinx.android.synthetic.main.custom_dialog_add_answer.view.*
import kotlinx.android.synthetic.main.fragment_question_details.*
import kotlinx.android.synthetic.main.fragment_question_details.view.*
import kotlinx.android.synthetic.main.question_row.view.*

private const val ARG_PARAM_QUESTION = "question_param"

class QuestionDetailsFragment : Fragment() {

    private var question: Question? = null
    private var noInternetDialog: MaterialDialog? = null
    private var addAnswerDialog: MaterialDialog? = null
    private var progressDialogAddingAnswer: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        question = arguments.getSerializable(ARG_PARAM_QUESTION) as Question

        progressDialogAddingAnswer = DialogFactory.buildProgressDialog(activity,
                getString(R.string.adding_answer), getString(R.string.please_wait))
        noInternetDialog = DialogFactory.buildNoInternetDialog(activity, {
            getAnswersFromServer(view)
        })

        addAnswerDialog = DialogFactory.buildAddAnswerDialog(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_question_details, container, false)

        view.titleTextView.text = question!!.title
        view.contentTextView.text = question!!.body
        view.userTextView.text = question!!.author
        view.dateCreatedTextView.text = Tools.dateToString(question!!.createdAt!!)
        addAnswerDialog!!.customView!!.answerQuestionTextView.text = question!!.title

        view.swipecontainerQuestionAnswers.setOnRefreshListener {
            getAnswersFromServer(view)
        }

        addAnswerDialog!!.customView!!.acceptTextViewAddAnswerDialog.setOnClickListener{
            val contentTextView = addAnswerDialog!!.customView!!.answerBodyTextInput

            if(Validators.validateAnyTextField(addAnswerDialog!!.context, contentTextView)
                    && Validators.validateTextFieldLength(addAnswerDialog!!.context, contentTextView, 128)){
                val userName = SharedPreferencesConnection.getLogin(activity)
                val content = contentTextView.text.toString()
                val answer = Answer(null, content, null, null, null, userName)
                Log.d("Created answer: ", "$content $userName")
                contentTextView.text.clear()
                addAnswerDialog!!.hide()
                addAnswerToServer(question!!.id!!, answer, view)
            }
        }

        view.fabAnswers.setOnClickListener {
            addAnswerDialog!!.show()
        }

        if(question!!.answers.isEmpty()){
            getAnswersFromServer(view)
        }else{
            view.recyclerViewQuestionAnswers.layoutManager = LinearLayoutManager(this.activity)
            view.recyclerViewQuestionAnswers.adapter =
                    AnswerRecyclerAdapter(question!!.answers, activity)
        }

        // Inflate the layout for this fragment
        return view
    }


    private fun getAnswersFromServer(view : View) {
        if (view.swipecontainerQuestionAnswers == null){
        }else if(!view.swipecontainerQuestionAnswers.isRefreshing)
            view.swipecontainerQuestionAnswers.isRefreshing = true

        ServerConnection.checkInternetConnection(activity, {
            if (activity != null) {
                ServerConnection.getAnswersToQuestion(question!!.id!!, { listOfAnswers ->
                    question!!.answers = listOfAnswers
                    question!!.answers.sortWith(compareByDescending{it.createdAt})
                    if (recyclerViewQuestionAnswers != null) {
                        recyclerViewQuestionAnswers.layoutManager = LinearLayoutManager(activity)
                        recyclerViewQuestionAnswers.adapter = AnswerRecyclerAdapter(question!!.answers,
                                activity)
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
                getAnswersFromServer(view)
            })
        })
    }

    fun addAnswerToServer(questionId: Int, answer: Answer, view: View){
        progressDialogAddingAnswer!!.show()
        ServerConnection.checkInternetConnection(activity, {
            if (activity != null) {
                ServerConnection.addAnswer(questionId, answer, {
                    progressDialogAddingAnswer!!.hide()
                    getAnswersFromServer(view)
                }, { exception ->
                    progressDialogAddingAnswer!!.hide()
                    DialogFactory.showErrorDialog(activity, exception)
                })
            }
        }, {
            progressDialogAddingAnswer!!.hide()
            DialogFactory.showNoInternetDialog(activity, {
                addAnswerToServer(questionId, answer, view)
            })
        })
    }

    fun dismissLoading(){
        if (swipecontainerQuestionAnswers.isRefreshing)
            swipecontainerQuestionAnswers.isRefreshing = false
    }


}
