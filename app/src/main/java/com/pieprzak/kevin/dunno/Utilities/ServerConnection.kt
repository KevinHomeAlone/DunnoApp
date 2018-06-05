package com.pieprzak.kevin.dunno.Utilities

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import org.json.JSONObject

object ServerConnection {

    //TODO: delete when normal authentication implemented
    var creditials = HashMap<String, String>()
    /**
     * Init specifies base path for Fuel
     * To be used as HTTP request root directory
     */
    init {
        FuelManager.instance.basePath = "http://37.233.102.13:3000/"

        //TODO: delete when normal authentication implemented
        creditials["admin"] = "admin"
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
}