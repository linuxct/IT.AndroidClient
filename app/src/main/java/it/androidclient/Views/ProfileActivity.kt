package it.androidclient.Views

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import it.androidclient.R
import it.androidclient.UserCtx.AchievementsModel
import it.androidclient.UserCtx.UserDataDto
import it.androidclient.Util.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar_common.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class ProfileActivity : AppCompatActivity() {
    private lateinit var userDataDto: UserDataDto
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        userDataDto = UserDataDto(applicationContext)

        userDataDto.userAchievements as AchievementsModel.UserCalendarModel?
        var localDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val todayDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)
        if (userDataDto.userAchievements != null && (userDataDto.userAchievements as AchievementsModel.UserCalendarModel).achievementList.any()){
            val list = (userDataDto.userAchievements as AchievementsModel.UserCalendarModel).achievementList
            localDate = list.keys.min()!!
        }

        calendarView.state().edit().setMinimumDate(CalendarDay.from(localDate.year, localDate.monthValue, localDate.dayOfMonth))
            .setMaximumDate(CalendarDay.from(todayDate.year, todayDate.monthValue, todayDate.dayOfMonth)).commit()
        calendarView.setDateSelected(CalendarDay.from(todayDate.year, todayDate.monthValue, todayDate.dayOfMonth), true)

        val achievementCounterHelper = AchievementCounterHelper()
        calendarView.addDecorators(DateHas0ActivityDecorator(this@ProfileActivity, achievementCounterHelper))
        calendarView.addDecorators(DateHas1ActivityDecorator(this@ProfileActivity, achievementCounterHelper))
        calendarView.addDecorators(DateHas2ActivityDecorator(this@ProfileActivity, achievementCounterHelper))
        calendarView.addDecorators(DateHas3ActivityDecorator(this@ProfileActivity, achievementCounterHelper))
        calendarView.addDecorators(DateHas4ActivityDecorator(this@ProfileActivity, achievementCounterHelper))
        calendarView.addDecorators(DateHas5ActivityDecorator(this@ProfileActivity, achievementCounterHelper))
        calendarView.addDecorators(DateHas6ActivityDecorator(this@ProfileActivity, achievementCounterHelper))
        calendarView.setOnDateChangedListener { _, date, _ ->
            val selectedDate = LocalDateTime.of(date.year, date.month, date.day, 0, 0, 0)
            val selectedDateIsToday = todayDate.isEqual(selectedDate)
            setAchievementsResultText(achievementCounterHelper.countTodaysAchievements(userDataDto, selectedDate), selectedDate, selectedDateIsToday)
        }
        setToolbarText(achievementCounterHelper.countTodaysAchievements(userDataDto, todayDate))
        setAchievementsResultText(achievementCounterHelper.countTodaysAchievements(userDataDto, todayDate), todayDate, true)
        button.setOnClickListener { finish() }
    }

    private fun setToolbarText(count: Int) {
        var properGreet = when(count){
            0 -> resources.getString(R.string.topToolbarProfile_0)
            1 -> resources.getString(R.string.topToolbarProfile_1_5)
            2 -> resources.getString(R.string.topToolbarProfile_1_5)
            3 -> resources.getString(R.string.topToolbarProfile_1_5)
            4 -> resources.getString(R.string.topToolbarProfile_1_5)
            5 -> resources.getString(R.string.topToolbarProfile_1_5)
            6 -> resources.getString(R.string.topToolbarProfile_6)
            else -> resources.getString(R.string.topToolbarProfile_generic)
        }

        val resourceMouse = when(count){
            0 -> R.drawable.mouse_sad
            6 -> R.drawable.mouse_good_surprise
            else -> R.drawable.mouse_happy_neutral
        }

        mouse_face.setImageResource(resourceMouse)
        toolbar_title.animateText(properGreet)
    }

    private fun setAchievementsResultText(count: Int, dateTime: LocalDateTime, isToday: Boolean){
        var properGreet = when(count){
            0 -> resources.getString(R.string.profileContent_0)
            1 -> resources.getString(R.string.profileContent_1to3)
            2 -> resources.getString(R.string.profileContent_1to3)
            3 -> resources.getString(R.string.profileContent_1to3)
            4 -> resources.getString(R.string.profileContent_4to5)
            5 -> resources.getString(R.string.profileContent_4to5)
            6 -> resources.getString(R.string.profileContent_6)
            else -> resources.getString(R.string.profileContent_0)
        }
        properGreet = if (!isToday) resources.getString(R.string.profileContent_pastDate) else properGreet
        val mainText = if (isToday) resources.getString(R.string.profileContent_today) else resources.getString(R.string.profileContent)
        achievementsResult.text = mainText
            .replace("{0}", count.toString())
            .replace("{1}", resources.getString(R.string.currentMaxTasksSize))
            .replace("{2}", userDataDto.userName.toString().capitalize())
            .replace("{3}", properGreet)
            .replace("{4}", if (isToday) resources.getString(R.string.Today) else dateTime.format(DateTimeFormatter.ofPattern("d MMM")))
    }
}
