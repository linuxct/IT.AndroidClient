package it.androidclient.Views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var triggeredUsernameMissing: Boolean = false
    private var mAdapter: LineAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(NotificationManagerCompat.from(applicationContext)) {
            cancelAll()
        }
        val userDataDto = UserDataDto(applicationContext)
        if (userDataDto.userName.isNullOrBlank()){
            val intent = if (userDataDto.acceptedOurPrivacyPolicy!!){
                Intent(applicationContext, UserModelSetupActivity::class.java)
            } else {
                Intent(applicationContext, WelcomeActivity::class.java)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            triggeredUsernameMissing = true
            startActivity(intent)
            return
        }
        setContentView(R.layout.activity_main)

        toolbar_title.animateText(resources.getString(R.string.greeting).replace("{0}", userDataDto.userName.toString().capitalize()))
        mouse_face.setImageResource(R.drawable.mouse_happy_neutral)

        setupRecycler(userDataDto.wantsFasterText!!)
    }

    override fun onResume() {
        super.onResume()
        val userDataDto = UserDataDto(applicationContext)
        if (!userDataDto.wantsFasterText!! && !triggeredUsernameMissing) {
            runLayoutAnimation(rvMain)
        }
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context: Context = recyclerView.context
        val controller: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    private fun setupRecycler(wantsFasterText: Boolean) {
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
        list.add(LineModel("MI PERFIL", false, "MI PERFIL", "AJUSTES", true))
        mAdapter = LineAdapter(list)
        rvMain.adapter = mAdapter

        if (wantsFasterText){
            runLayoutAnimation(rvMain)
        }

        rvMain.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }
}
