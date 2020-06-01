package it.androidclient.Views

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import it.androidclient.R
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import it.androidclient.BroadcastReceivers.AlarmBroadcastReceiver
import it.androidclient.UserCtx.UserDataDto
import kotlinx.android.synthetic.main.activity_user_model_setup.*
import java.util.*

class UserModelSetupActivity : AppCompatActivity() {
    private var timeWasSet: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_model_setup)
        val userDataDto = UserDataDto(applicationContext)

        improvedReaderConstrainedLayout.visibility = if (userDataDto.acceptedMicrosoftPrivacyPolicy!!) View.VISIBLE else View.GONE

        configureReminderSetup.setOnClickListener {
            val mcurrentTime: Calendar = Calendar.getInstance()
            val hour: Int = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute: Int = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this@UserModelSetupActivity,
                OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    Toast.makeText(applicationContext, "${getString(R.string.newTimeSet)}, $selectedHour:${selectedMinute.toString().padStart(2, '0')}", Toast.LENGTH_LONG).show()
                    startAlarmBroadcastReceiver(applicationContext, selectedHour, selectedMinute)
                    timeWasSet = true
                }, hour, minute, true
            )

            mTimePicker.setTitle(getString(R.string.setTimeText))
            mTimePicker.show()
        }

        continueButton.setOnClickListener {
            if (textInputFieldName.text.isNullOrBlank()){
                textInputFieldName.error=getString(R.string.mustTellName)
                textInputFieldName.invalidate()
                textInputFieldName.requestFocus()
                return@setOnClickListener
            }

            if (!timeWasSet){
                Toast.makeText(applicationContext, getString(R.string.mustSetTime), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            userDataDto.userName = textInputFieldName.text.toString()
            userDataDto.userSurname = textInputFieldSurname.text.toString()
            userDataDto.wantsReaderV2 = switchWantsReaderV2.isChecked
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        textInputFieldName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textInputFieldName.error = null
            }
        })

    }

    private fun startAlarmBroadcastReceiver(context: Context, selectedHour: Int, selectedMinute: Int) {
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, selectedHour)
            set(Calendar.MINUTE, selectedMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.cancel(pendingIntent)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        UserDataDto(context.applicationContext).notificationTimeSetHour = selectedHour
        UserDataDto(context.applicationContext).notificationTimeSetMinute = selectedMinute
    }
}