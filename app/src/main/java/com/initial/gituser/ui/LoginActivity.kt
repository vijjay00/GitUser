package com.initial.gituser.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.initial.gituser.R
import com.initial.gituser.utils.toast


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val account = Auth0(applicationContext.resources.getString(R.string.client_id), applicationContext.resources.getString(R.string.domain))
        account.isOIDCConformant = true
     login(account)
    }

    private fun login(account: Auth0) {
        WebAuthProvider.login(account)
            .withScheme("demo")
            .withAudience(getString(R.string.domain)
                )
            .start(this, object : AuthCallback {
                override fun onFailure(dialog: Dialog) {
                    toast("failed login")
                }

                override fun onFailure(exception: AuthenticationException) {
                    toast(exception.description.toString())
                }

                override fun onSuccess(credentials: Credentials) {
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                }
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

