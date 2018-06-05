package com.pieprzak.kevin.dunno.Model

import com.beust.klaxon.Json

data class Answer (
        var id : Int,
        var body : String,
        var questionId : Int,
        var createdAt : String,
        var updatedAt : String,
        var author : String
){
}