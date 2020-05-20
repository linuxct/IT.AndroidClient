package it.androidclient.UserCtx

import Preferences
import android.content.Context

class UserDataDto(ctx: Context) : Preferences(ctx) {
    var userName by stringPref()
    var todaysPost by todaysPostPref()
    var needsDyslexicFont by booleanPref()
    var userAchievements by achievementsPref()
}