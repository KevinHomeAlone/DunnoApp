package com.pieprzak.kevin.dunno.Adapters

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pieprzak.kevin.dunno.Fragments.QuestionDetailsFragment
import com.pieprzak.kevin.dunno.Fragments.QuestionsFragment
import com.pieprzak.kevin.dunno.Model.Question
import com.pieprzak.kevin.dunno.R
import com.pieprzak.kevin.dunno.Utilities.Tools
import kotlinx.android.synthetic.main.question_row.view.*

private const val ARG_PARAM_QUESTION = "question_param"

class QuestionsRecyclerAdapter(private val listOfQuestions: ArrayList<Question>, val fragment: QuestionsFragment,
                               val activity: Activity)
    :RecyclerView.Adapter<QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_row, parent, false)

        return QuestionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfQuestions.size
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(listOfQuestions[position], activity, fragment)
    }
}

class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: Question, activity: Activity, fragment: QuestionsFragment) {

        itemView.userTextView.text = item.author
        itemView.titleTextView.text = item.title
        itemView.contentTextView.text = item.body
        itemView.dateCreatedTextView.text = Tools.dateToString(item.createdAt!!)

        itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(ARG_PARAM_QUESTION, item)
            val questionDetailsFragment = QuestionDetailsFragment()
            questionDetailsFragment.arguments = bundle
            fragment.makeTransition(itemView,questionDetailsFragment, itemView.height)
        }
    }
}