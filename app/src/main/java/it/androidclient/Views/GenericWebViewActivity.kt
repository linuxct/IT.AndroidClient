package it.androidclient.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import it.androidclient.R
import kotlinx.android.synthetic.main.activity_generic_web_view.*

class GenericWebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generic_web_view)
        val url = intent.extras?.get("url") as String?

        if (url != null){
            webViewGeneric.loadUrl(url)
        } else {
            finish()
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }
}