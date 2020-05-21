package it.androidclient.Views

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import it.androidclient.Line.LineAdapter
import it.androidclient.Line.LineModel
import it.androidclient.R
import it.androidclient.UserCtx.AchievementsModel
import it.androidclient.UserCtx.UserDataDto
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_common.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class MainActivity : AppCompatActivity() {

    private var mAdapter: LineAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userDataDto = UserDataDto(applicationContext)
        if (userDataDto.userName.isNullOrBlank()){
            val intent = Intent(applicationContext, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            return
        }
        setContentView(R.layout.activity_main)
        toolbar_title.text = resources.getString(R.string.greeting).replace("{0}", userDataDto.userName.toString())
        setupRecycler()
    }

    private fun setupRecycler() {
        val layoutManager = LinearLayoutManager(this)
        rvMain.layoutManager = layoutManager

        val list = arrayListOf<LineModel>()
        val userDataDto = UserDataDto(applicationContext)
        val todaysAchievements = (userDataDto.userAchievements as AchievementsModel.UserCalendarModel?)?.achievementList?.get(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))
        list.add(LineModel("LECTURA", todaysAchievements?.achievedReading ?: false))
        list.add(LineModel("LENGUAJE", todaysAchievements?.achievedLanguage ?: false))
        list.add(LineModel("PENSAMIENTO ABSTRACTO", todaysAchievements?.achievedAbstractThinking ?: false))
        list.add(LineModel("CONCENTRACION", todaysAchievements?.achievedConcentration ?: false))
        list.add(LineModel("PRAXIAS", todaysAchievements?.achievedPraxias ?: false))
        list.add(LineModel("PERCEPCION SENSORIAL", todaysAchievements?.achievedSensorial ?: false))
        list.add(LineModel("MI PERFIL", false))
        mAdapter = LineAdapter(list)
        rvMain.adapter = mAdapter

        rvMain.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }
}
