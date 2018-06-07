package com.pieprzak.kevin.dunno.Adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pieprzak.kevin.dunno.Model.Answer
import com.pieprzak.kevin.dunno.R
import kotlinx.android.synthetic.main.answer_row.view.*

class AnswerRecyclerAdapter(private val listOfAnswers : ArrayList<Answer>, val activity: Activity)
    : RecyclerView.Adapter<AnswerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.answer_row, parent, false)

        return AnswerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfAnswers.size
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(listOfAnswers[position], activity)
    }
}

class AnswerViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    fun bind(item: Answer, activity: Activity) {

        Log.d("Answer ", "${item.author} ${item.body}")
        itemView.userTextViewAnswer.text = item.author
        itemView.contentTextViewAnswer.text = item.body
    }

}