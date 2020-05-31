package it.androidclient.MicrosoftSDK

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.microsoft.cognitiveservices.speech.audio.AudioStreamFormat
import com.microsoft.cognitiveservices.speech.audio.PullAudioInputStreamCallback

/**
 * MicrophoneStream exposes the Android Microphone as an PullAudioInputStreamCallback
 * to be consumed by the Speech SDK.
 * It configures the microphone with 16 kHz sample rate, 16 bit samples, mono (single-channel).
 */
class MicrophoneStream : PullAudioInputStreamCallback() {
    private val format: AudioStreamFormat
    private lateinit var recorder: AudioRecord

    override fun read(bytes: ByteArray): Int {
        val ret = recorder.read(bytes, 0, bytes.size).toLong()
        return ret.toInt()
    }

    override fun close() {
        recorder.release()
    }

    private fun initMic() {
        // Note: currently, the Speech SDK support 16 kHz sample rate, 16 bit samples, mono (single-channel) only.
        val af = AudioFormat.Builder()
            .setSampleRate(SAMPLE_RATE)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
            .build()
        recorder = AudioRecord.Builder()
            .setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
            .setAudioFormat(af)
            .build()
        recorder.startRecording()
    }

    companion object {
        private const val SAMPLE_RATE = 16000
    }

    init {
        format = AudioStreamFormat.getWaveFormatPCM(
            SAMPLE_RATE.toLong(),
            16.toShort(),
            1.toShort()
        )
        initMic()
    }
}