package com.pieprzak.kevin.dunno.Fragments


import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.pieprzak.kevin.dunno.Adapters.AnswerRecyclerAdapter
import com.pieprzak.kevin.dunno.Model.Answer
import com.pieprzak.kevin.dunno.Model.Question

import com.pieprzak.kevin.dunno.R
import com.pieprzak.kevin.dunno.Utilities.DialogFactory
import com.pieprzak.kevin.dunno.Utilities.ServerConnection
import kotlinx.android.synthetic.main.fragment_question_details.*
import kotlinx.android.synthetic.main.fragment_question_details.view.*
import kotlinx.android.synthetic.main.question_row.view.*
import org.jetbrains.anko.act

private const val ARG_PARAM_QUESTION = "question_param"

class QuestionDetailsFragment : Fragment() {

    private var question: Question? = null
    private var noInternetDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        question = arguments.getSerializable(ARG_PARAM_QUESTION) as Question

        noInternetDialog = DialogFactory.buildNoInternetDialog(activity, {
            getQuestionsFromServer(view)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_question_details, container, false)

        view.titleTextView.text = question!!.title
        view.contentTextView.text = question!!.body
        view.userTextView.text = question!!.author

        view.swipecontainerQuestionAnswers.setOnRefreshListener {
            getQuestionsFromServer(view)
        }

        if(question!!.answers.isEmpty()){
            getQuestionsFromServer(view)
        }else{
            view.recyclerViewQuestionAnswers.layoutManager = LinearLayoutManager(this.activity)
            view.recyclerViewQuestionAnswers.adapter =
                    AnswerRecyclerAdapter(question!!.answers, activity)
        }

        // Inflate the layout for this fragment
        return view
    }


    private fun getQuestionsFromServer(view : View) {
        if (view.swipecontainerQuestionAnswers == null){
        }else if(!view.swipecontainerQuestionAnswers.isRefreshing)
            view.swipecontainerQuestionAnswers.isRefreshing = true

        ServerConnection.checkInternetConnection(activity, {
            if (activity != null) {
                ServerConnection.getAnswersToQuestion(question!!.id!!, { listOfAnswers ->
                    question!!.answers = listOfAnswers
                    question!!.answers.sortWith(compareBy{it.updatedAt})
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
                getQuestionsFromServer(view)
            })
        })
    }

    fun dismissLoading(){
        if (swipecontainerQuestionAnswers.isRefreshing)
            swipecontainerQuestionAnswers.isRefreshing = false
    }


}
