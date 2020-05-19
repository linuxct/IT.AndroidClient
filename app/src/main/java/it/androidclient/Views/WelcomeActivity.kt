package it.androidclient.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import it.androidclient.R
import it.androidclient.UserCtx.UserDataDto

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val userNameField = findViewById<EditText>(R.id.editText)
        findViewById<Button>(R.id.continueButton).setOnClickListener {
            if (userNameField.text.isNullOrBlank()){
                userNameField.error="Debes decirme tu nombre para continuar"
                userNameField.invalidate()
                userNameField.requestFocus()
                return@setOnClickListener
            }
            val userDataDto =
                UserDataDto(applicationContext)
            userDataDto.userName = userNameField.text.toString()
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        findViewById<EditText>(R.id.editText).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userNameField.error = null
            }
        })
    }
}
