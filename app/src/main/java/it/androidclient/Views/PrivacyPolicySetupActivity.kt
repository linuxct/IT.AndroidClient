package it.androidclient.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import it.androidclient.R
import it.androidclient.UserCtx.UserDataDto
import kotlinx.android.synthetic.main.activity_privacy_policy_setup.*

class PrivacyPolicySetupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_privacy_policy_setup)
        val userDataDto = UserDataDto(applicationContext)

        viewOurPrivacyPolicy.setOnClickListener {
            val intent = Intent(applicationContext, GenericWebViewActivity::class.java)
            intent.putExtra("url", getString(R.string.privacypolicyurl))
            intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)
        }

        viewMicrosoftPrivacyPolicy.setOnClickListener {
            val intent = Intent(applicationContext, GenericWebViewActivity::class.java)
            intent.putExtra("url", getString(R.string.microsoftprivacypolicyurl))
            intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)
        }

        if (intent.hasExtra("onlyMicrosoftPolicy") && (intent.extras!!["onlyMicrosoftPolicy"]!! as Boolean?) == true){
            linearLayoutOurPrivacyPolicy.visibility = View.GONE
            checkBoxOurPrivacyPolicy.visibility = View.GONE
            viewOurPrivacyPolicy.visibility = View.GONE
            checkBoxMicrosoftPrivacyPolicy.setOnCheckedChangeListener { v, isChecked ->
                continueButton.visibility = if (isChecked) View.VISIBLE else View.GONE
            }

            continueButton.setOnClickListener {
                userDataDto.acceptedOurPrivacyPolicy = checkBoxOurPrivacyPolicy.isChecked
                userDataDto.acceptedMicrosoftPrivacyPolicy = checkBoxMicrosoftPrivacyPolicy.isChecked
                userDataDto.wantsReaderV2 = true

                val intent = Intent(applicationContext, SettingsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

            return
        }

        checkBoxOurPrivacyPolicy.setOnCheckedChangeListener { _, isChecked ->
            continueButton.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        continueButton.setOnClickListener {
            userDataDto.acceptedOurPrivacyPolicy = checkBoxOurPrivacyPolicy.isChecked
            userDataDto.acceptedMicrosoftPrivacyPolicy = checkBoxMicrosoftPrivacyPolicy.isChecked

            val intent = Intent(applicationContext, UserModelSetupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}