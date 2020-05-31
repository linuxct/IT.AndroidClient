package it.androidclient.Util

import it.androidclient.BuildConfig
import serilogj.ILogger
import serilogj.Log
import serilogj.LoggerConfiguration
import serilogj.events.LogEventLevel
import serilogj.sinks.seq.SeqSinkConfigurator

interface Loggable {
    fun seqLog(): ILogger {
        Log.setLogger(
            LoggerConfiguration()
            .writeTo(SeqSinkConfigurator.seq(BuildConfig.API_SEQ_BASE))
            .setMinimumLevel(LogEventLevel.Information)
            .createLogger())
        return Log.getLogger()
    }
}