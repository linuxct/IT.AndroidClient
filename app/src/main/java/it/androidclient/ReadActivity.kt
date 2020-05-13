package it.androidclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView

class ReadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        findViewById<TextView>(R.id.textTitle).apply {
            setText(R.string.example_title)
        }
        findViewById<TextView>(R.id.textRead).apply {
            setText(R.string.example_text)
        }
    }
}
