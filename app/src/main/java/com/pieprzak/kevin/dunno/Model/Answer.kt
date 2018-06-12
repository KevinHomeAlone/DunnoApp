package com.pieprzak.kevin.dunno.Model

import com.beust.klaxon.Json
import com.pieprzak.kevin.dunno.Utilities.Tools
import org.json.JSONObject
import java.io.Serializable
import java.util.*

data class Answer (
        var id : Int?,
        var body : String,
        var questionId : Int?,
        var createdAt : Date?,
        var updatedAt : Date?,
        var author : String
) : Serializable{
    companion object {
        fun fromJson(json: JSONObject) : Answer{
            val id = json.getInt("id")
            val questionId = json.getInt("question_id")
            val body = json.getString("body")
            val createdAt = Tools.parseDate(json.getString("created_at"))
            val updatedAt = Tools.parseDate(json.getString("updated_at"))
            val author = json.getString("user_login")
            return Answer(id, body, questionId, createdAt, updatedAt, author)
        }
    }
}