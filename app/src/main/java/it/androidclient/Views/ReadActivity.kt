package it.androidclient.Views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import it.androidclient.R
import it.androidclient.Services.TodaysPostModel
import it.androidclient.Services.TodaysPostService
import it.androidclient.UserCtx.UserDataDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ReadActivity : AppCompatActivity() {

    private val todaysPostService by lazy { TodaysPostService.create() }
    private val dispatcherIoScope = CoroutineScope(Dispatchers.IO)
    private var userDataDto: UserDataDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        userDataDto = UserDataDto(applicationContext)
        if (userDataDto!!.todaysPost is TodaysPostModel.Result && postAppliesToToday(userDataDto!!.todaysPost as TodaysPostModel.Result)){
            val value = userDataDto!!.todaysPost as TodaysPostModel.Result
            displayResult(value)
        } else {
            beginFetchPostSuspend()
        }

        findViewById<TextView>(R.id.toolbar_title).apply {
            text = resources.getString(R.string.cheering).replace("{0}", userDataDto!!.userName.toString())
        }
        findViewById<Button>(R.id.button).apply {
            setOnClickListener {
                val intent = Intent(context.applicationContext, CongratulationsActivity::class.java)
                intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

    private fun postAppliesToToday(todaysPost: TodaysPostModel.Result): Boolean {
        val currDate: Date = Calendar.getInstance().time
        val comp = todaysPost.date.day != currDate.day || todaysPost.date.month != currDate.month || todaysPost.date.year != currDate.year
        return !comp
    }

    private fun beginFetchPostSuspend() {
        dispatcherIoScope.launch {
            try {
                val result = todaysPostService.todaysPost()
                withContext(Dispatchers.Main) {
                    displayResult(result)
                    userDataDto!!.todaysPost = result
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    toastError(exception.message)
                }
            }
        }
    }

    private fun displayResult(result: TodaysPostModel.Result) {
        findViewById<TextView>(R.id.textTitle).apply {
            text = result.title
        }
        val stringBuilder = StringBuilder()
        result.pageContents.forEach { p ->
            stringBuilder.append(p)
        }
        findViewById<TextView>(R.id.textRead).apply {
            text = stringBuilder.toString()
        }
    }

    private fun toastError(error: String? = null) {
        Toast.makeText(this, error ?: "Unknown Error", Toast.LENGTH_SHORT).show()
    }

}
