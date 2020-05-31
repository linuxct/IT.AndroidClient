package it.androidclient.External

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import it.androidclient.UserCtx.UserDataDto

class Typewriter : AppCompatTextView {
    private var mText: CharSequence? = null
    private var mIndex = 0
    private var mDelay: Long = 70 //Default was 500ms delay

    constructor(context: Context?) : super(context) { }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { }

    private val mHandler = Handler()
    private val characterAdder: Runnable = object : Runnable {
        override fun run() {
            text = mText!!.subSequence(0, mIndex++)
            if (mIndex <= mText!!.length) {
                mHandler.postDelayed(this, mDelay)
            }
        }
    }

    fun animateText(text: CharSequence) {
        if (UserDataDto(context.applicationContext).wantsFasterText!!){
            setText(text)
            return
        }
        mText = text
        mIndex = 0
        setText("")
        mHandler.removeCallbacks(characterAdder)
        mHandler.postDelayed(characterAdder, mDelay)
    }

    fun setCharacterDelay(millis: Long) {
        mDelay = millis
    }
}