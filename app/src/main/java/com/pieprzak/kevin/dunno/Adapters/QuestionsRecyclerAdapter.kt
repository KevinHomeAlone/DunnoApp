package com.pieprzak.kevin.dunno.Adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pieprzak.kevin.dunno.Fragments.QuestionsFragment
import com.pieprzak.kevin.dunno.Model.Question
import com.pieprzak.kevin.dunno.R
import kotlinx.android.synthetic.main.question_row.view.*

class QuestionsRecyclerAdapter(private val listOfQuestions: ArrayList<Question>, val fragment: QuestionsFragment)
    :RecyclerView.Adapter<QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_row, parent, false)

        return QuestionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfQuestions.size
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(listOfQuestions[position])
    }
}

class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: Question) {
        Log.d("Question ", "${item.title} ${item.createdAt}")
        itemView.userTextView.text = item.author
        itemView.titleTextView.text = item.title
        itemView.contentTextView.text = item.body
    }
}