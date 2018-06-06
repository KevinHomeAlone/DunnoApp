package com.pieprzak.kevin.dunno.Model

import com.beust.klaxon.Json

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
    var answers : ArrayList<Answer> = ArrayList())