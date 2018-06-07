package com.pieprzak.kevin.dunno.Model

import com.beust.klaxon.Json
import org.json.JSONObject

data class Answer (
        var id : Int?,
        var body : String,
        var questionId : Int?,
        var createdAt : String?,
        var updatedAt : String?,
        var author : String
){
    companion object {
        fun fromJson(json: JSONObject) : Answer{
            val id = json.getInt("id")
            val questionId = json.getInt("question_id")
            val body = json.getString("body")
            val createdAt = json.getString("created_at")
            val updatedAt = json.getString("updated_at")
            val author = json.getString("author")
            return Answer(id, body, questionId, createdAt, updatedAt, author)
        }
    }
}