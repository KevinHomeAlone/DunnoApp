package com.pieprzak.kevin.dunno.Fragments


import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.pieprzak.kevin.dunno.Adapters.QuestionsRecyclerAdapter
import com.pieprzak.kevin.dunno.Model.Question

import com.pieprzak.kevin.dunno.R
import com.pieprzak.kevin.dunno.Utilities.DialogFactory
import com.pieprzak.kevin.dunno.Utilities.ServerConnection
import kotlinx.android.synthetic.main.fragment_questions.*
import kotlinx.android.synthetic.main.fragment_questions.view.*


class QuestionsFragment : Fragment() {

    private var listOfQuestions: ArrayList<Question> = ArrayList()
    private var progressDialog: MaterialDialog? = null
    private var noInternetDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog = DialogFactory.buildProgressDialog(activity, getString(R.string.loading_dunnos), getString(R.string.please_wait))

        noInternetDialog = DialogFactory.buildNoInternetDialog(activity, {
            getQuestionsFromServer()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_questions, container, false)

        view.swipeContainerQuestions.setOnRefreshListener {
            getQuestionsFromServer()
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
        if (swipeContainerQuestions == null || !swipeContainerQuestions.isRefreshing){
            //progressDialog = DialogFactory.buildProgressDialog(activity, getString(R.string.loading_dunnos), getString(R.string.please_wait))
            progressDialog!!.show()
        }

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
            noInternetDialog!!.show()
        })
    }

    fun dismissLoading(){
        if (swipeContainerQuestions.isRefreshing)
            swipeContainerQuestions.isRefreshing = false
        if (progressDialog != null)
            progressDialog!!.dismiss()
    }


}
