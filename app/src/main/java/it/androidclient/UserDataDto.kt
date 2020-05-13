package it.androidclient

import Preferences
import android.content.Context

class UserDataDto(ctx: Context) : Preferences(ctx) {
    var userName by stringPref()
}