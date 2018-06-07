package com.pieprzak.kevin.dunno.Model

import com.beust.klaxon.Json
import org.json.JSONObject
import java.io.Serializable

data class Question (
    var id : Int?,
    var title : String,
    var body : String,
    @Json(name = "created_at")
    var createdAt : String?,
    @Json(name = "updated_at")
    var updatedAt : String?,
    var author : String,
    @Json(ignored = true)
    var answers : ArrayList<Answer> = ArrayList()): Serializable{
    companion object {
        fun fromJson(json: JSONObject) : Question{
            val id = json.getInt("id")
            val title = json.getString("title")
            val body = json.getString("body")
            val createdAt = json.getString("created_at")
            val updatedAt = json.getString("updated_at")
            val author = json.getString("author")
            return Question(id, title, body, createdAt, updatedAt, author, ArrayList())
        }
    }
}