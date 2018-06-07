package com.pieprzak.kevin.dunno.Model

import android.util.*
import com.beust.klaxon.Json
import com.pieprzak.kevin.dunno.Utilities.Tools
import org.json.JSONObject
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.Instant
import java.util.*

data class Question (
    var id : Int?,
    var title : String,
    var body : String,
    @Json(name = "created_at")
    var createdAt : Date?,
    @Json(name = "updated_at")
    var updatedAt : Date?,
    var author : String,
    @Json(ignored = true)
    var answers : ArrayList<Answer> = ArrayList()): Serializable{
    companion object {
        fun fromJson(json: JSONObject) : Question{
            val id = json.getInt("id")
            val title = json.getString("title")
            val body = json.getString("body")
            val createdAt = Tools.parseDate(json.getString("created_at"))
            val updatedAt = Tools.parseDate(json.getString("updated_at"))
            val author = json.getString("author")
            return Question(id, title, body, createdAt, updatedAt, author, ArrayList())
        }
    }
}