package it.androidclient.Services

import java.util.*

object TodaysPostModel {
    data class Result(val date: Date, val title: String, val pageContents: List<String>)
}