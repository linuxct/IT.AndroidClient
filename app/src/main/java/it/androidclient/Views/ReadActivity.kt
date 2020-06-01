package it.androidclient.Views

import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Typeface
import android.os.Bundle
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import it.androidclient.R
import it.androidclient.Services.TodaysPostModel
import it.androidclient.Services.TodaysPostService
import it.androidclient.UserCtx.AchievementsModel
import it.androidclient.UserCtx.UserDataDto
import kotlinx.android.synthetic.main.activity_read.*
import kotlinx.android.synthetic.main.toolbar_common.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.NullPointerException
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.log


class ReadActivity : AppCompatActivity() {

    private val todaysPostService by lazy { TodaysPostService.create() }
    private val dispatcherIoScope = CoroutineScope(Dispatchers.IO)
    private lateinit var userDataDto: UserDataDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        userDataDto = UserDataDto(applicationContext)
        if (userDataDto.todaysPost is TodaysPostModel.Result && postAppliesToToday(userDataDto.todaysPost as TodaysPostModel.Result)){
            val value = userDataDto.todaysPost as TodaysPostModel.Result
            displayResult(value)
        } else {
            beginFetchPostSuspend()
        }

        if (userDataDto.needsDyslexicFont as Boolean){
            val am: AssetManager = applicationContext.assets
            val typeface = Typeface.createFromAsset(am,
                java.lang.String.format(Locale.US, "fonts/%s", "OpenDyslexic-Regular.ttf")
            )

            textTitle.typeface = typeface
            textRead.typeface = typeface
            textTitle.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)
            textRead.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics)
        }

        mouse_face.setImageResource(R.drawable.mouse_decided_neutral)
        toolbar_title.apply {
            animateText(resources.getString(R.string.cheering).replace("{0}", userDataDto.userName.toString().capitalize()))
        }
        button.apply {
            setOnClickListener {
                if (userDataDto.userAchievements == null){
                    userDataDto.userAchievements = AchievementsModel.UserCalendarModel(hashMapOf())
                }

                val currentAchievements = userDataDto.userAchievements as AchievementsModel.UserCalendarModel
                if (currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)] == null) {
                    currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)] =
                        AchievementsModel.DailyMilestonesAchieved(
                            achievedReading = false,
                            achievedLanguage = false,
                            achievedAbstractThinking = false,
                            achievedConcentration = false,
                            achievedPraxias = false,
                            achievedSensorial = false
                        )
                }
                currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)]?.achievedReading = true
                userDataDto.userAchievements = currentAchievements

                val intent = Intent(context.applicationContext, CongratulationsActivity::class.java)
                intent.putExtra("comesFrom", getString(R.string.applicationSectionNameReading).toLowerCase())
                intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

    private fun postAppliesToToday(todaysPost: TodaysPostModel.Result): Boolean {
        val currDate: Date = Calendar.getInstance().time
        val comp = todaysPost.date.day != currDate.day || todaysPost.date.month != currDate.month || todaysPost.date.year != currDate.year
        return !comp
    }

    private fun beginFetchPostSuspend() {
        dispatcherIoScope.launch {
            try {
                val result = todaysPostService.todaysPost()
                withContext(Dispatchers.Main) {
                    displayResult(result)
                    if (result.pageContents.isNotEmpty()){
                        userDataDto.todaysPost = result
                    }
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    toastError(exception.message)
                    if (exception.message == "timeout"){
                        beginFetchPostSuspend()
                    }
                }
            }
        }
    }

    private fun displayResult(result: TodaysPostModel.Result) {
        if (result.title.isBlank() || result.pageContents.isEmpty()){
            return
        }

        try {
            textTitle.apply {
                text = result.title
            }
            val stringBuilder = StringBuilder()
            result.pageContents.forEach { p ->
                stringBuilder.append(p)
                stringBuilder.append("\n\n")
            }

            textRead.apply {
                text = stringBuilder.toString()
                justificationMode = JUSTIFICATION_MODE_INTER_WORD
            }
        } catch (exception: NullPointerException){
            Log.e("LINUXCT", "There was an error while unparsing info from the server", exception)
        }
    }

    private fun toastError(error: String? = null) {
        Toast.makeText(this, error ?: "Unknown Error", Toast.LENGTH_SHORT).show()
    }

}
