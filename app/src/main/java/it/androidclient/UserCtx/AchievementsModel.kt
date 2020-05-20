package it.androidclient.UserCtx

import java.time.LocalDateTime
import kotlin.collections.HashMap

object AchievementsModel {
    data class UserCalendarModel(var achievementList: HashMap<LocalDateTime, DailyMilestonesAchieved>)
    data class DailyMilestonesAchieved(var achievedReading: Boolean, var achievedLanguage: Boolean, var achievedAbstractThinking: Boolean, var achievedConcentration: Boolean, var achievedPraxias: Boolean, var achievedSensorial: Boolean)
}