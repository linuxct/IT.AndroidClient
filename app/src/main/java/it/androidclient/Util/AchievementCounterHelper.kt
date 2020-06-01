package it.androidclient.Util

import it.androidclient.UserCtx.AchievementsModel
import it.androidclient.UserCtx.UserDataDto
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class AchievementCounterHelper {
    fun countTodaysAchievements(userDataDto: UserDataDto, localDate: LocalDateTime): Int{
        val userAchievements = performLocalSafeUserAchievementsInitialization(userDataDto, localDate)
        var count = 0
        AchievementsModel.DailyMilestonesAchieved (
            achievedAbstractThinking = userAchievements.achievedAbstractThinking.also { achievedAbstractThinking->
                if (achievedAbstractThinking){
                    count += 1
                }
            },
            achievedConcentration = userAchievements.achievedConcentration.also { achievedConcentration ->
                if (achievedConcentration){
                    count += 1
                }
            },
            achievedLanguage = userAchievements.achievedLanguage.also { achievedLanguage ->
                if (achievedLanguage){
                    count += 1
                }
            },
            achievedPraxias = userAchievements.achievedPraxias.also { achievedPraxias ->
                if (achievedPraxias){
                    count += 1
                }
            },
            achievedReading = userAchievements.achievedReading.also { achievedReading ->
                if (achievedReading){
                    count += 1
                }
            },
            achievedSensorial = userAchievements.achievedSensorial.also { achievedSensorial ->
                if (achievedSensorial){
                    count += 1
                }
            }
        )

        return count
    }

    private fun performLocalSafeUserAchievementsInitialization(userDataDto: UserDataDto, parsedDate: LocalDateTime): AchievementsModel.DailyMilestonesAchieved {
        if (userDataDto.userAchievements == null){
            userDataDto.userAchievements = AchievementsModel.UserCalendarModel(hashMapOf())
        }

        val currentAchievements = userDataDto.userAchievements as AchievementsModel.UserCalendarModel
        if (currentAchievements.achievementList[parsedDate] == null) {
            currentAchievements.achievementList[parsedDate] =
                AchievementsModel.DailyMilestonesAchieved(
                    achievedReading = false,
                    achievedLanguage = false,
                    achievedAbstractThinking = false,
                    achievedConcentration = false,
                    achievedPraxias = false,
                    achievedSensorial = false
                )
        }
        return currentAchievements.achievementList[parsedDate.truncatedTo(ChronoUnit.DAYS)] as AchievementsModel.DailyMilestonesAchieved
    }

}