package com.example.filmschecker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 28

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        auth.signOut()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("288195316574-7q62bf7mpnrhusu8m4popv6clkp0anjf.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        GoogleSignIn.getLastSignedInAccount(this)
        //mGoogleSignInClient.signOut()

        //updateUI(account)

        val signInButton: SignInButton = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener{
            Log.i("click","sign in")
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent,RC_SIGN_IN)
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {}

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("rq", requestCode.toString())
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.i("task", task.isSuccessful.toString())
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account!!.idToken!!)
            Log.i("login","success")
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        } catch (e: ApiException) {
            Log.w("result fail", "signInResult:failed code=" + e.statusCode)
        }
    }
}