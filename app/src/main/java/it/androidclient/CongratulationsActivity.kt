package it.androidclient

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_congratulations.*


class CongratulationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congratulations)

        val handler = Handler()
        handler.postDelayed({
            launchHomeActivity()
        }, 7000)


        constrainedCongratulationsLayout.setOnTouchListener { _, _ ->
            handler.removeCallbacksAndMessages(null)
            launchHomeActivity()
            true
        }
    }

    private fun launchHomeActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
