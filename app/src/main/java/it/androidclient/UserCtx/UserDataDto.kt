package it.androidclient.UserCtx

import Preferences
import android.content.Context

class UserDataDto(ctx: Context) : Preferences(ctx) {
    var userName by stringPref()
}