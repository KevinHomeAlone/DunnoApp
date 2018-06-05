package com.pieprzak.kevin.dunno.Login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import com.afollestad.materialdialogs.MaterialDialog
import com.pieprzak.kevin.dunno.MainActivity
import com.pieprzak.kevin.dunno.R
import com.pieprzak.kevin.dunno.Utilities.DialogFactory
import com.pieprzak.kevin.dunno.Utilities.ServerConnection
import com.pieprzak.kevin.dunno.Utilities.SharedPreferencesConnection
import com.pieprzak.kevin.dunno.Utilities.Validators
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var progressDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val accessToken = SharedPreferencesConnection.getAccessToken(this)

        if (!accessToken.isBlank()) {
            runMainActivity()
            finish()
        }else
            bindUI()
    }

    private fun bindUI(){
        setContentView(R.layout.activity_login)

        // Handle enter key press as login action
        password_input_text.setOnEditorActionListener { _, id, _ ->

            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
            }
            false
        }

        signin_button.setOnClickListener {
            if (Validators.validateAnyTextField(this, login_input_text)
                    && Validators.validateAnyTextField(this, password_input_text)) {
                attemptLogin()
            }
        }

        register_button.setOnClickListener {
            runRegisterActivity(login_input_text.text.toString())
        }

        forgot_password_text.setOnClickListener {
            recoverPassword()
        }
    }

    private fun runMainActivity() {

        Log.d("Login", "Running MainActivity")

        val intent = Intent(this, MainActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.finish()
    }

    private fun attemptLogin() {

        progressDialog = DialogFactory.buildProgressDialog(this, getString(R.string.sign_in), getString(R.string.please_wait))
        progressDialog!!.show()

        val login = login_input_text.text.toString()
        val password = password_input_text.text.toString()

        ServerConnection.loginWithLogin(login, password, { token ->
            SharedPreferencesConnection.setAccessToken(this, token)
            SharedPreferencesConnection.setLogin(this, login)

            progressDialog!!.dismiss()
            runMainActivity()
        }, {
            progressDialog!!.dismiss()
            DialogFactory.showErrorDialog(this, it)
        })
    }

    private fun runRegisterActivity(login: String?) {

    }

    private fun recoverPassword() {

    }
}
