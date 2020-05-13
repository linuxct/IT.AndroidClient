package it.androidclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.TextView

class ReadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        val userDataDto = UserDataDto(applicationContext)
        findViewById<TextView>(R.id.textTitle).apply {
            setText(R.string.example_title)
        }
        findViewById<TextView>(R.id.textRead).apply {
            setText(R.string.example_text)
        }
        findViewById<TextView>(R.id.toolbar_title).apply {
            text = resources.getString(R.string.cheering).replace("{0}", userDataDto.userName.toString())
        }
        findViewById<Button>(R.id.button).apply {
            setOnClickListener {
                val intent = Intent(context.applicationContext, CongratulationsActivity::class.java)
                intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }
}
