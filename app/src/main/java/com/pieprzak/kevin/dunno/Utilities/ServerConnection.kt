package com.pieprzak.kevin.dunno.Utilities

import android.content.Context
import android.util.Log
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import com.keiferstone.nonet.ConnectionStatus
import com.keiferstone.nonet.NoNet
import com.pieprzak.kevin.dunno.Model.Answer
import com.pieprzak.kevin.dunno.Model.Question
import org.json.JSONObject

object ServerConnection {

    //TODO: delete when normal authentication implemented
    var creditials = HashMap<String, String>()
    /**
     * Init specifies base path for Fuel
     * To be used as HTTP request root directory
     */
    init {
        FuelManager.instance.basePath = "http://37.233.102.13:3000/api/"

        //TODO: delete when normal authentication implemented
        creditials["play_console_tests"] = "play_console_tests"
        creditials["krysia"] = "krysia"
        creditials["kevin"] = "kevin"
        creditials["igor"] = "igor"
        creditials["mateusz"] = "mateusz"
        creditials["pawel"] = "pawel"
        creditials["radek"] = "radek"
    }

    /**
     * Autenthicates user to receive an access token from server
     * @param login already registered login access
     * @param password entered password
     * @param success is a callback, with (Access token granted by server [String])
     * @param error is a callback, with (Exception thrown by connection [Exception])
     */

    fun loginWithLogin(login: String, password: String, success: (String) -> Unit, error: (Exception) -> Unit) {

        /*// HTTP request body
        val body = JSONObject()
        body.put("user", login)
        body.put("password", password)

        Fuel.post("API/login")
                .body(body.toString())
                .responseJson { request, response, result ->
                    // Fold result of query
                    result.fold({ json ->
                        Log.d("JSON login", json.obj().toString())
                        val receivedJSON = json.obj()
                        val token = receivedJSON.getString("token")
                        succes(token)
                    }, { fuelError ->
                        Log.e("ERROR", fuelError.toString())
                        Log.e("RESPONSE", response.responseMessage)
                        Log.e("REQUEST", request.toString())
                        error(fuelError.exception)
                    })
                }*/
        if(creditials.containsKey(login) && creditials[login] == password)
            success("chujTokenJaPierdole")
        else
            error(Exception())

    }

    /**
     * Downloads all questions
     * @param succes is a callback, with (List of questions [ArrayList]<[Question]>)
     * @param error is a callback, with (Exception thrown by connection [Exception])
     */
    fun getAllQuestions(succes: (ArrayList<Question>) -> Unit, error: (Exception) -> Unit) {
        Fuel.get("questions")
                .responseJson { request, response, result ->
                    // Fold result of query
                    result.fold({ json ->
                        Log.d("JSON", json.content)
                        // Deserialize JSON
                        var listOfQuestions = ArrayList<Question>()
                        //FIXME: Why klaxon stops progrss dialog?
                        //val listOfQuestions = Klaxon().parseArray<Question>(json.content)
                        for(i in 0 until json.array().length())
                            listOfQuestions.add(Question.fromJson(json.array().getJSONObject(i)))

                        //val listOfQuestions = Klaxon().parseArray<Question>(json.content)
                        // Call success
                        succes(listOfQuestions)
                    }, { fuelError ->
                        Log.e("ERROR", fuelError.toString())
                        Log.e("RESPONSE", response.responseMessage)
                        Log.e("REQUEST", request.toString())
                        error(fuelError.exception)
                    })
                }
    }

    /**
     * Downloads all answers to question
     * @param id is an id of question that answers should be downloaded
     * @param succes is a callback, with (List of answers [ArrayList]<[Answer]>)
     * @param error is a callback, with (Exception thrown by connection [Exception])
     */
    fun getAnswersToQuestion(id : Int, succes: (ArrayList<Answer>) -> Unit, error: (Exception) -> Unit) {
        Fuel.get("questions/$id/answers")
                .responseJson { request, response, result ->
                    // Fold result of query
                    result.fold({ json ->
                        Log.d("JSON", json.content)
                        // Deserialize JSON
                        var listOfAnswers = ArrayList<Answer>()
                        for(i in 0 until json.array().length())
                            listOfAnswers.add(Answer.fromJson(json.array().getJSONObject(i)))

                        //val listOfQuestions = Klaxon().parseArray<Question>(json.content)
                        // Call success
                        succes(listOfAnswers)
                    }, { fuelError ->
                        Log.e("ERROR", fuelError.toString())
                        Log.e("RESPONSE", response.responseMessage)
                        Log.e("REQUEST", request.toString())
                        error(fuelError.exception)
                    })
                }
    }

    /**
     * Adds new question
     * @param author is username of author [String]
     * @param title is title of a question [String]
     * @param body is body of a question [String]
     * @param succes is a callback
     * @param error is a callback, with (Exception thrown by connection [Exception])
     */
    fun addQuestion(question: Question, succes: () -> Unit, error: (Exception) -> Unit) {
        Fuel.post("questions")
                .body(Klaxon().toJsonString(question))
                .header(Pair("Content-Type", "application/json"))
                .response { request, response, result ->
                    // Fold result of query
                    result.fold({ _ ->
                        // Call success
                        succes()
                    }, { fuelError ->
                        Log.e("ERROR", fuelError.toString())
                        Log.e("RESPONSE", response.responseMessage)
                        Log.e("REQUEST", request.toString())
                        error(fuelError.exception)
                    })
                }
    }

    /**
     * Adds new question
     * @param author is username of author [String]
     * @param title is title of a question [String]
     * @param body is body of a question [String]
     * @param succes is a callback
     * @param error is a callback, with (Exception thrown by connection [Exception])
     */
    fun addAnswer(questionId: Int, answer: Answer, succes: () -> Unit, error: (Exception) -> Unit) {
        Fuel.post("questions/$questionId/answers")
                .body(Klaxon().toJsonString(answer))
                .header(Pair("Content-Type", "application/json"))
                .response { request, response, result ->
                    // Fold result of query
                    result.fold({ _ ->
                        // Call success
                        succes()
                    }, { fuelError ->
                        Log.e("ERROR", fuelError.toString())
                        Log.e("RESPONSE", response.responseMessage)
                        Log.e("REQUEST", request.toString())
                        error(fuelError.exception)
                    })
                }
    }

    /**
     * Checks internet connection by pinging google.com
     * @param context Application context
     * @param online is a callback, when ping is succesfull
     * @param offline is a callback, when ping is unsucessfull
     */
    fun checkInternetConnection(context: Context, online: () -> Unit, offline: () -> Unit) {
        val configuration = NoNet.configure()
                .endpoint("http://google.com")
                .timeout(5)
                .connectedPollFrequency(60)
                .disconnectedPollFrequency(1)
                .build()

        NoNet.check(context)
                .configure(configuration)
                .callback { connectionStatus ->
                    if (connectionStatus == ConnectionStatus.CONNECTED)
                        online()
                    else
                        offline()
                }.start()
    }
}