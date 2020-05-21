package it.androidclient.Util

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import it.androidclient.R
import it.androidclient.UserCtx.UserDataDto
import java.time.LocalDateTime

class DateHas4ActivityDecorator(context: Activity?, private val achievementCounterHelper: AchievementCounterHelper) : DayViewDecorator {
    private val drawable: Drawable?
    private val ctx = context as Activity
    override fun shouldDecorate(day: CalendarDay): Boolean {
        val userDataDto = UserDataDto(ctx.applicationContext)
        val localDate = LocalDateTime.of(day.year, day.month, day.day, 0, 0, 0)
        val count: Int = achievementCounterHelper.countTodaysAchievements(userDataDto, localDate)

        return count == 4
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable!!)
    }

    init {
        // You can set background for Decorator via drawable here
        drawable = ContextCompat.getDrawable(context!!, R.drawable.day_decorator_4)
    }
}