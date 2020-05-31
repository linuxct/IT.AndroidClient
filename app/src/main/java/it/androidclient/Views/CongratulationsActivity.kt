package it.androidclient.Views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import it.androidclient.R
import kotlinx.android.synthetic.main.activity_congratulations.*
import kotlinx.android.synthetic.main.activity_generic_web_view.*


class CongratulationsActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congratulations)
        val title = intent.extras?.get("comesFrom") as String?

        if (title != null){
            textView.text = getString(R.string.congratulationsActivityMainText).replace("{0}", title)
        }

        val handler = Handler()
        handler.postDelayed({
            launchHomeActivity()
        }, 7000)

        constrainedCongratulationsLayout.setOnTouchListener { _, _ ->
            handler.removeCallbacksAndMessages(null)
            launchHomeActivity()
            return@setOnTouchListener true
        }
    }

    private fun launchHomeActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
