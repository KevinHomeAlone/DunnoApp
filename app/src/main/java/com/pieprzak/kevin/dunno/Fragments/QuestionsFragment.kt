package com.pieprzak.kevin.dunno.Fragments


import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bg.devlabs.transitioner.Transitioner
import com.afollestad.materialdialogs.MaterialDialog
import com.pieprzak.kevin.dunno.Adapters.QuestionsRecyclerAdapter
import com.pieprzak.kevin.dunno.Model.Question

import com.pieprzak.kevin.dunno.R
import com.pieprzak.kevin.dunno.Utilities.DialogFactory
import com.pieprzak.kevin.dunno.Utilities.ServerConnection
import com.pieprzak.kevin.dunno.Utilities.SharedPreferencesConnection
import com.pieprzak.kevin.dunno.Utilities.Validators
import kotlinx.android.synthetic.main.custom_dialog_add_question.view.*
import kotlinx.android.synthetic.main.questions_view.*
import kotlinx.android.synthetic.main.questions_view.view.*

private const val ARG_IS_USER_DUNNOS_FRAGMENT = "user_dunnos"


class QuestionsFragment : Fragment() {

    private var listOfQuestions: ArrayList<Question> = ArrayList()
    private var listOfUserQuestions: ArrayList<Question> = ArrayList()
    private var progressDialogLoadingQuestions: MaterialDialog? = null
    private var progressDialogAddingQuestion: MaterialDialog? = null
    private var addQuestionDialog : MaterialDialog? = null
    private var isUserDunnosFragment : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(arguments.containsKey(ARG_IS_USER_DUNNOS_FRAGMENT)){
            isUserDunnosFragment = arguments.getBoolean(ARG_IS_USER_DUNNOS_FRAGMENT)
        }
        progressDialogLoadingQuestions = DialogFactory.buildProgressDialog(activity,
                getString(R.string.loading_dunnos), getString(R.string.please_wait))
        progressDialogAddingQuestion = DialogFactory.buildProgressDialog(activity,
                getString(R.string.adding_dunno), getString(R.string.please_wait))
        addQuestionDialog = DialogFactory.buildAddQuestionDialog(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val layout = when(isUserDunnosFragment){
            true -> R.layout.fragment_questions_user
            else -> R.layout.fragment_questions
        }

        val view = inflater.inflate(layout, container, false)

        view.swipeContainerQuestions.setOnRefreshListener {
            getQuestionsFromServer()
        }

        addQuestionDialog!!.customView!!.acceptTextViewAddQuestionDialog.setOnClickListener{
            val titleTextView = addQuestionDialog!!.customView!!.titleAddQuestionDialog
            val contentTextView = addQuestionDialog!!.customView!!.contentAddQuestionDialog

            if(Validators.validateAnyTextField(addQuestionDialog!!.context, titleTextView)
                    && Validators.validateTextFieldLength(addQuestionDialog!!.context, titleTextView, 32)
                    && Validators.validateAnyTextField(addQuestionDialog!!.context, contentTextView)
                    && Validators.validateTextFieldLength(addQuestionDialog!!.context, contentTextView, 128)){
                val userName = SharedPreferencesConnection.getLogin(activity)
                val title = titleTextView.text.toString()
                val content = contentTextView.text.toString()
                val question = Question(null, title, content, null, null,
                        userName)
                Log.d("Created question: ", "$title $content $userName")
                titleTextView.text.clear()
                contentTextView.text.clear()
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
            reloadRecyclerView(view)
        }

        // Inflate the layout for this fragment
        return view
    }

    private fun getQuestionsFromServer() {
        if (swipeContainerQuestions == null){
            progressDialogLoadingQuestions!!.show()
        }else if(!view.swipeContainerQuestions.isRefreshing)
            view.swipeContainerQuestions.isRefreshing = true

        ServerConnection.checkInternetConnection(activity, {
            if (activity != null) {
                ServerConnection.getAllQuestions(activity, { listOfQuestions ->
                    this.listOfQuestions = listOfQuestions
                    val login = SharedPreferencesConnection.getLogin(activity)
                    this.listOfQuestions.sortWith(compareByDescending{it.createdAt})
                    this.listOfUserQuestions = ArrayList(this.listOfQuestions.filter{ it.author == login })
                    if (view.recyclerViewQuestions != null) {
                        reloadRecyclerView(view)
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
                ServerConnection.addQuestion(activity, question, {
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
        if (view.swipeContainerQuestions.isRefreshing)
            view.swipeContainerQuestions.isRefreshing = false
        if (progressDialogLoadingQuestions != null)
            progressDialogLoadingQuestions!!.dismiss()
    }

    fun makeTransition(startView: View, questionDetailsFragment: QuestionDetailsFragment, height: Int){
        var params = view.transition_con.layoutParams
        params.height = height
        view.transition_con.layoutParams = params
        val transition = Transitioner(startView, view.transition_con)
        transition.duration = 300
        transition.animateTo(1f)
        transition.onProgressChanged {
            if(transition.currentProgress == 1f){
                activity.fragmentManager.beginTransaction().replace(R.id.main_fragment_placeholder,
                        questionDetailsFragment)
                        .addToBackStack(null)
                        .commit()
            }
        }
    }

    fun reloadRecyclerView(view: View){
        if(isUserDunnosFragment){
            view.recyclerViewQuestions.layoutManager = LinearLayoutManager(activity)
            view.recyclerViewQuestions.adapter = QuestionsRecyclerAdapter(listOfUserQuestions, this, activity)
        }else{
            view.recyclerViewQuestions.layoutManager = LinearLayoutManager(activity)
            view.recyclerViewQuestions.adapter = QuestionsRecyclerAdapter(listOfQuestions, this, activity)
        }

    }

}
