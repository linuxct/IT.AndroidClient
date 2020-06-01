package it.androidclient.Views

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import it.androidclient.BroadcastReceivers.AlarmBroadcastReceiver
import it.androidclient.R
import it.androidclient.UserCtx.UserDataDto
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.toolbar_common.*
import java.util.*


class SettingsActivity : AppCompatActivity() {

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mouse_face.setImageResource(R.drawable.mouse_decided_neutral)

        val userDataDto = UserDataDto(applicationContext)
        if (userDataDto.needsDyslexicFont != null){
            switchDyslexic.isChecked = userDataDto.needsDyslexicFont!!
        }
        if (userDataDto.wantsReaderV2 != null) {
            switchWantsReaderV2.isChecked = userDataDto.wantsReaderV2!!
        }
        if (userDataDto.wantsFasterText != null) {
            switchWantsFasterAnimations.isChecked = userDataDto.wantsFasterText!!
        }
        toolbar_title.animateText("Aqui puedes cambiar lo que necesites, no lo dudes.")

        switchDyslexic.setOnCheckedChangeListener { _, isChecked ->
            userDataDto.needsDyslexicFont = isChecked
        }

        switchWantsReaderV2.setOnCheckedChangeListener { _, isChecked ->
            if (userDataDto.acceptedMicrosoftPrivacyPolicy!!){
                userDataDto.wantsReaderV2 = isChecked
            } else {
                val intent = Intent(applicationContext, PrivacyPolicySetupActivity::class.java)
                intent.putExtra("onlyMicrosoftPolicy", true)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        switchWantsFasterAnimations.setOnCheckedChangeListener { _, isChecked ->
            userDataDto.wantsFasterText = isChecked
        }

        textInputField.setText(userDataDto.userName.toString().capitalize(), TextView.BufferType.EDITABLE)
        if (userDataDto.userSurname != null){
            textInputFieldSurname.setText(userDataDto.userSurname.toString().capitalize(), TextView.BufferType.EDITABLE)
        }

        buttonSave.setOnClickListener {
            if (textInputField.text.isNullOrBlank()){
                textInputField.setText(userDataDto.userName.toString().capitalize(), TextView.BufferType.EDITABLE)
                Toast.makeText(applicationContext, getString(R.string.undoChanges), Toast.LENGTH_LONG).show()
            } else {
                userDataDto.userName = textInputField.text.toString()
                Toast.makeText(applicationContext, getString(R.string.savedChanges), Toast.LENGTH_LONG).show()
            }
            userDataDto.userSurname = textInputFieldSurname.text.toString()
        }

        configureReminder.setOnClickListener {
            val mcurrentTime: Calendar = Calendar.getInstance()
            val hour: Int = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute: Int = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this@SettingsActivity,
                OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    Toast.makeText(applicationContext, "${getString(R.string.newTimeSet)}, $selectedHour:${selectedMinute.toString().padStart(2, '0')}", Toast.LENGTH_LONG).show()
                    startAlarmBroadcastReceiver(applicationContext, selectedHour, selectedMinute)
                }, hour, minute, true
            )

            mTimePicker.setTitle(getString(R.string.setTimeText))
            mTimePicker.show()
        }

        button.setOnClickListener { finish() }
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
