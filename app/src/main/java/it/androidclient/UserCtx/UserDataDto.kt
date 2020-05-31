package it.androidclient.UserCtx

import Preferences
import android.content.Context
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class UserDataDto(ctx: Context) : Preferences(ctx) {
    fun performSafeUserAchievementsInitialization() {
        if (this.userAchievements == null){
            this.userAchievements = AchievementsModel.UserCalendarModel(hashMapOf())
        }

        val currentAchievements = this.userAchievements as AchievementsModel.UserCalendarModel
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
            this.userAchievements = currentAchievements
        }
    }

    var userName by stringPref()
    var userSurname by stringPref()
    var todaysPost by todaysPostPref()
    var needsDyslexicFont by booleanPref()
    var userAchievements by achievementsPref()
    var notificationTimeSetHour by intPref()
    var notificationTimeSetMinute by intPref()
    var wantsReaderV2 by booleanPref()
    var wantsFasterText by booleanPref()
    var acceptedMicrosoftPrivacyPolicy by booleanPref()
    var acceptedOurPrivacyPolicy by booleanPref()
}

