package it.androidclient.Views

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import it.androidclient.R
import it.androidclient.UserCtx.UserDataDto
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.toolbar_common.*
import java.util.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar_title.text = "Aqui puedes cambiar lo que necesites, no lo dudes"

        val userDataDto = UserDataDto(applicationContext)
        if (userDataDto.needsDyslexicFont != null){
            switchDyslexic.isChecked = userDataDto.needsDyslexicFont!!
        }

        switchDyslexic.setOnCheckedChangeListener { _, isChecked ->
            userDataDto.needsDyslexicFont = isChecked
        }

        textInputField.setText(userDataDto.userName.toString(), TextView.BufferType.EDITABLE)
        buttonOkName.setOnClickListener {
            if (textInputField.text.isNullOrBlank()){
                textInputField.setText(userDataDto.userName.toString(), TextView.BufferType.EDITABLE)
                Toast.makeText(applicationContext, "Cambios deshechos", Toast.LENGTH_LONG).show()
            } else {
                userDataDto.userName = textInputField.text.toString()
                Toast.makeText(applicationContext, "Cambios guardados", Toast.LENGTH_LONG).show()
            }
        }

        configureReminder.setOnClickListener {
            val mcurrentTime: Calendar = Calendar.getInstance()
            val hour: Int = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute: Int = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this@SettingsActivity,
                OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    Toast.makeText(applicationContext, "Nueva hora fijada $selectedHour:$selectedMinute", Toast.LENGTH_LONG).show()
                }, hour, minute, true
            )

            mTimePicker.setTitle("Elige una hora")
            mTimePicker.show()
        }

        button.setOnClickListener { finish() }
    }
}
