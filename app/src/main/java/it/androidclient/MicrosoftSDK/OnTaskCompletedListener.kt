package it.androidclient.MicrosoftSDK

interface OnTaskCompletedListener<T> {
    fun onCompleted(taskResult: T)
}