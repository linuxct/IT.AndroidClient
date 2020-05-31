package it.androidclient.Views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import it.androidclient.R
import it.androidclient.Util.Loggable

class WelcomeActivity : AppCompatActivity(), Loggable {
    private val Log = seqLog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        Log.information("Hello from {lang}!", "Java")

        findViewById<Button>(R.id.continueButton).setOnClickListener {
            val intent = Intent(applicationContext, PrivacyPolicySetupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
