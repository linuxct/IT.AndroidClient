package it.androidclient.MicrosoftSDK

import android.annotation.SuppressLint
import android.app.Activity
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import com.microsoft.cognitiveservices.speech.SpeechConfig
import com.microsoft.cognitiveservices.speech.SpeechRecognitionEventArgs
import com.microsoft.cognitiveservices.speech.SpeechRecognizer
import com.microsoft.cognitiveservices.speech.audio.AudioConfig
import it.androidclient.BuildConfig
import it.androidclient.R
import it.androidclient.Services.TodaysPostModel
import it.androidclient.UserCtx.UserDataDto
import kotlinx.android.synthetic.main.toolbar_common.*
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.regex.Pattern

@SuppressLint("DefaultLocale")
open class MicrosoftCSSTUtils (private val activity: Activity, private val workingText: TodaysPostModel.Result) {
    private var s_executorService: ExecutorService? = Executors.newCachedThreadPool()

    private val SpeechSubscriptionKey = BuildConfig.MICROSOFT_CSST_API_KEY
    private val SpeechRegion = "eastus"
    private val SpeechLanguage = "es-ES"

    private lateinit var speechConfig: SpeechConfig
    private var microphoneStream: MicrophoneStream? = null

    private var continuousListeningStarted = false
    private var reco: SpeechRecognizer? = null
    private var audioInput: AudioConfig? = null
    private val content = ArrayList<String>()
    private var pendingReadText = String()
    private var cleanReadText = String()
    private var viewText = String()

    private var badWordCount: Int = 0
    private var goodWordCount: Int = 0
    private val userDataDto = UserDataDto(activity.applicationContext)

    private val SPECIAL_REGEX_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]")

    fun performListening(){
        pendingReadText = workingText.pageContents.joinToString(separator = "\n")
        cleanReadText = workingText.pageContents.joinToString(separator = "\n")
        viewText = workingText.pageContents.joinToString(separator = "\n\n")
        speechConfig = SpeechConfig.fromSubscription(SpeechSubscriptionKey, SpeechRegion)

        try {
            if (BuildConfig.DEBUG){
                onDebugConfigSetup()
            } else {
                onReleaseConfigSetup()
            }
        } catch (ex: Exception) {
            Log.d("LINUXCT", "Excepcion al inicializar, $ex")
        }
    }

    private fun onDebugConfigSetup(){
        content.clear()
        audioInput = AudioConfig.fromStreamInput(createMicrophoneStream())
        reco = SpeechRecognizer(speechConfig, SpeechLanguage, audioInput)
        reco!!.recognizing.addEventListener { _: Any?, speechRecognitionResultEventArgs: SpeechRecognitionEventArgs ->
            recognizingHandler(speechRecognitionResultEventArgs)
            val s = speechRecognitionResultEventArgs.result.text
            Log.i("LINUXCT", "Intermediate result received: $s")
            content.add(s)
            //setRecognizedText(TextUtils.join(" ", content))
            Log.w("LINUXCT", "recognizing text is now $content")
            content.removeAt(content.size - 1)
        }
        reco!!.recognized.addEventListener { _: Any?, speechRecognitionResultEventArgs: SpeechRecognitionEventArgs ->
            val s = speechRecognitionResultEventArgs.result.text
            Log.i("LINUXCT", "Final result received: $s")
            content.add(s)
            Log.w("LINUXCT", "recognized text is now $content")
            //setRecognizedText(TextUtils.join(" ", content))
        }
        val task = reco!!.startContinuousRecognitionAsync()
        setOnTaskCompletedListener(
            task,
            object : OnTaskCompletedListener<Void> {
                override fun onCompleted(taskResult: Void) {
                    continuousListeningStarted = true
                }
            }
        )
    }

    private fun onReleaseConfigSetup(){
        audioInput = AudioConfig.fromStreamInput(createMicrophoneStream())
        reco = SpeechRecognizer(speechConfig, SpeechLanguage, audioInput)
        reco!!.recognizing.addEventListener { _: Any?, speechRecognitionResultEventArgs: SpeechRecognitionEventArgs ->
            recognizingHandler(speechRecognitionResultEventArgs)
        }
        val task = reco!!.startContinuousRecognitionAsync()
        setOnTaskCompletedListener(
            task,
            object : OnTaskCompletedListener<Void> {
                override fun onCompleted(taskResult: Void) {
                    continuousListeningStarted = true
                }
            }
        )
    }

    private fun recognizingHandler(speechRecognitionEventArgs: SpeechRecognitionEventArgs) {
        val text = speechRecognitionEventArgs.result.text
        val split = pendingReadText.trim(' ').split("\\s+".toRegex()).toMutableList()
        val splitClean = cleanReadText.trim(' ').split("\\s+".toRegex()).toMutableList()
        split.removeAll { it.isBlank() }
        var currentMicrophoneInput = text
        for (i in split.indices) {
            val internalArrayInput = split[i]
            if (!internalArrayInput.contains("*")) {
                val currentWordInText = internalArrayInput.replace("[^A-zÀ-ú0-9]".toRegex(), "")
                if (currentMicrophoneInput == ""){
                    break
                }

                var previousWord = ""
                var antePreviousWord = ""
                if (i > 2){
                    previousWord = splitClean[i-1]
                    antePreviousWord = splitClean[i-2]
                }
                var nextWord = ""
                if (i < split.indices.count()){
                    nextWord = splitClean[i+1]
                }

                val findLongest = escapeSpecialRegexChars("$antePreviousWord $previousWord $internalArrayInput")
                val findLong = escapeSpecialRegexChars("$previousWord $internalArrayInput")
                val findNext = escapeSpecialRegexChars("$internalArrayInput $nextWord")
                val findShort = escapeSpecialRegexChars(" $internalArrayInput ")
                val findShortest = escapeSpecialRegexChars(internalArrayInput)

                alterText(viewText, findShortest, findShort, findNext, findLong, findLongest)

                if (detectedBadWordActions(currentMicrophoneInput, currentWordInText))
                    break

                goodWordCount+=1
                badWordCount = 0

                performGoodWordCountActions()
                split[i] = split[i].replace(internalArrayInput, "*$currentWordInText*")
                currentMicrophoneInput = currentMicrophoneInput.toLowerCase().replaceFirst(currentWordInText.toLowerCase(), "")
            }
        }
        pendingReadText = String.format("%s", java.lang.String.join(" ", *split.toTypedArray()))
    }

    private fun detectedBadWordActions(currentMicrophoneInput: String, currentWordInText: String): Boolean {
        if (!currentMicrophoneInput.toLowerCase().contains(currentWordInText.toLowerCase()) && !currentWordInText.toLowerCase().contains(currentMicrophoneInput.toLowerCase()))
        {
            badWordCount += 1
            goodWordCount = 0

            val isAcronym = currentMicrophoneInput.all { it.isUpperCase() }
            val isFirstLetterCapital = currentMicrophoneInput.first().isUpperCase()
            val chancesOfBeingInAForeignLanguage = isFirstLetterCapital && badWordCount >= 2

            if (isAcronym || chancesOfBeingInAForeignLanguage){
                return false
            }

            if (badWordCount == 9) {
                activity.runOnUiThread {
                    activity.mouse_face.setImageResource(R.drawable.mouse_sad)
                    activity.toolbar_title.animateText(activity.resources.getString(R.string.cheering_bad).replace("{0}", userDataDto.userName.toString().capitalize()))
                }
            }

            if (badWordCount == 16) {
                activity.runOnUiThread {
                    activity.toolbar_title.animateText(activity.resources.getString(R.string.cheering_skipped_word))
                }
            } else {
                return true
            }
        }
        return false
    }

    private fun performGoodWordCountActions(){
        if (goodWordCount == 1){
            activity.runOnUiThread {
                activity.mouse_face.setImageResource(R.drawable.mouse_decided_neutral)
                activity.toolbar_title.text = activity.resources.getString(R.string.cheering_go_on)
            }
        }

        if (goodWordCount == 5){
            activity.runOnUiThread {
                activity.mouse_face.setImageResource(R.drawable.mouse_happy_neutral)
                activity.toolbar_title.animateText(activity.resources.getString(R.string.cheering_well_done))
            }
        }

        if (goodWordCount == 9){
            activity.runOnUiThread {
                activity.mouse_face.setImageResource(R.drawable.mouse_good_surprise)
                activity.toolbar_title.animateText(activity.resources.getString(R.string.cheering_excellent))
            }
        }
    }

    private fun alterText(text: String, findShortest: String, findShort: String, findNext: String, findLong: String, findLongest: String) {
        val pLongest = Pattern.compile(findLongest)
        val pLong = Pattern.compile(findLong)
        val pNext = Pattern.compile(findNext)
        val pShort = Pattern.compile(findShort)
        val pShortest = Pattern.compile(findShortest)
        val matcherLongest = pLongest.matcher(text)
        val matcherLong = pLong.matcher(text)
        val matcherNext = pNext.matcher(text)
        val matcherShort = pShort.matcher(text)
        val matcherShortest = pShortest.matcher(text)
        val spannable = SpannableStringBuilder(text)
        val span = ForegroundColorSpan(activity.getColor(R.color.wordErroneous))

        if (matcherLongest.find()){
            val lengthOfExtraWords = findLongest.length - findShortest.length - 1
            spannable.setSpan(
                span, matcherLongest.start() + lengthOfExtraWords, matcherLongest.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            applySpan(spannable)
            return
        }

        if (matcherLong.find()){
            val lengthOfExtraWords = findLong.length - findShortest.length - 1
            spannable.setSpan(
                span, matcherLong.start() + lengthOfExtraWords, matcherLong.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            applySpan(spannable)
            return
        }

        if (matcherNext.find()){
            val lengthOfExtraWords = findNext.length - findShortest.length - 1
            spannable.setSpan(
                span, matcherNext.start(), matcherNext.end() - lengthOfExtraWords, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            applySpan(spannable)
            return
        }

        if (matcherShort.find()){
            spannable.setSpan(
                span, matcherShort.start(), matcherShort.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            applySpan(spannable)
            return
        }

        if (matcherShortest.find()){
            spannable.setSpan(
                span, matcherShortest.start(), matcherShortest.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            applySpan(spannable)
        }
    }

    private fun applySpan(spannable: SpannableStringBuilder) {
        val textView = activity.findViewById(R.id.textReadV2) as TextView
        activity.runOnUiThread {
            textView.text = spannable
        }
    }

    private fun escapeSpecialRegexChars(input: String): String {
        return SPECIAL_REGEX_CHARS.matcher(input).replaceAll("\\\\$0")
    }

    fun suspendListening(){
        if (continuousListeningStarted) {
            if (reco != null) {
                val task = reco!!.stopContinuousRecognitionAsync()
                setOnTaskCompletedListener(task,
                    object : OnTaskCompletedListener<Void> {
                        override fun onCompleted(taskResult: Void) {
                            continuousListeningStarted = false
                        }
                    }
                )
            } else {
                continuousListeningStarted = false
            }
        }
    }

    private fun <T> setOnTaskCompletedListener(task: Future<T>, listener: OnTaskCompletedListener<T>) {
        s_executorService!!.submit<Any?> {
            val result = task.get()
            listener.onCompleted(result)
            null
        }
    }

    private fun createMicrophoneStream(): MicrophoneStream {
        if (microphoneStream != null) {
            microphoneStream!!.close()
            microphoneStream = null
        }
        microphoneStream = MicrophoneStream()
        return microphoneStream!!
    }
}