package it.androidclient.Line

import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import it.androidclient.R
import it.androidclient.UserCtx.AchievementsModel
import it.androidclient.UserCtx.UserDataDto
import it.androidclient.Views.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class LineAdapter(model: ArrayList<LineModel>?) : RecyclerView.Adapter<LineHolder>() {
    private val mModel: MutableList<LineModel>?
    private lateinit var userDataDto: UserDataDto
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineHolder {
        userDataDto = UserDataDto(parent.context.applicationContext)
        return LineHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_main_item, parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: LineHolder, position: Int) {

        holder.setIsRecyclable(false)

        var color = when (position) {
            0 -> R.color.applicationList1
            1 -> R.color.applicationList2
            2 -> R.color.applicationList3
            3 -> R.color.applicationList4
            4 -> R.color.applicationList5
            5 -> R.color.applicationList6
            6 -> R.color.applicationList7
            else -> R.color.applicationList1
        }

        color = when (mModel!![position].completed){
            true -> R.color.applicationListDone
            else -> color
        }

        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context.applicationContext, color))

        if (position == 6){
            holder.coreLayout.visibility = View.GONE
            holder.specialLayout.visibility = View.VISIBLE

            holder.leftTextView.text = mModel[position].leftText
            holder.rightTextView.text = mModel[position].rightText

            holder.specialLayout.setOnTouchListener{ _, motEvent ->
                val metrics = holder.specialLayout.context.applicationContext.resources.displayMetrics
                val width = metrics.widthPixels
                val x = motEvent.rawX
                if (motEvent.action == MotionEvent.ACTION_UP){
                    if (width/2 > x){
                        val intent = Intent(holder.specialLayout.context.applicationContext, ProfileActivity::class.java)
                        intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
                        holder.specialLayout.context.applicationContext.startActivity(intent)
                    } else {
                        val intent = Intent(holder.specialLayout.context.applicationContext, SettingsActivity::class.java)
                        intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
                        holder.specialLayout.context.applicationContext.startActivity(intent)
                    }
                }
                return@setOnTouchListener true
            }
            return
        }

        holder.title.text = String.format(
            Locale.getDefault(), "%s",
            mModel[position].title
        )

        holder.isCompleted.visibility = if (mModel[position].completed) View.VISIBLE else View.GONE

        val listener = when (position){
            0 -> readListener
            1 -> languageListener
            2 -> abstractListener
            3 -> concentrationListener
            4 -> praxiasListener
            5 -> perceptionListener
            else -> profileListener
        }

        holder.itemView.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return mModel?.size ?: 0
    }

    //region Listeners
    private val readListener = View.OnClickListener { v ->
            val intent = if (userDataDto.wantsReaderV2!!){
                Intent(v.context.applicationContext, ReadV2Activity::class.java)
            } else {
                Intent(v.context.applicationContext, ReadActivity::class.java)
            }

            intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
            v.context.applicationContext.startActivity(intent)
        }

    private val languageListener =
        View.OnClickListener { v ->
            userDataDto.performSafeUserAchievementsInitialization()
            val currentAchievements = userDataDto.userAchievements as AchievementsModel.UserCalendarModel
            currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)]?.achievedLanguage = true
            userDataDto.userAchievements = currentAchievements
            val intent = Intent(v.context.applicationContext, CongratulationsActivity::class.java)
            intent.putExtra("comesFrom", "lenguaje")
            intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
            v.context.applicationContext.startActivity(intent)
        }

    private val abstractListener =
        View.OnClickListener { v ->
            userDataDto.performSafeUserAchievementsInitialization()
            val currentAchievements = userDataDto.userAchievements as AchievementsModel.UserCalendarModel
            currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)]?.achievedAbstractThinking = true
            userDataDto.userAchievements = currentAchievements
            val intent = Intent(v.context.applicationContext, CongratulationsActivity::class.java)
            intent.putExtra("comesFrom", "pensamiento abstracto")
            intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
            v.context.applicationContext.startActivity(intent)
        }

    private val concentrationListener =
        View.OnClickListener { v ->
            userDataDto.performSafeUserAchievementsInitialization()
            val currentAchievements = userDataDto.userAchievements as AchievementsModel.UserCalendarModel
            currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)]?.achievedConcentration = true
            userDataDto.userAchievements = currentAchievements
            val intent = Intent(v.context.applicationContext, CongratulationsActivity::class.java)
            intent.putExtra("comesFrom", "concentración")
            intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
            v.context.applicationContext.startActivity(intent)
        }

    private val praxiasListener =
        View.OnClickListener { v ->
            userDataDto.performSafeUserAchievementsInitialization()
            val currentAchievements = userDataDto.userAchievements as AchievementsModel.UserCalendarModel
            currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)]?.achievedPraxias = true
            userDataDto.userAchievements = currentAchievements
            val intent = Intent(v.context.applicationContext, CongratulationsActivity::class.java)
            intent.putExtra("comesFrom", "praxias")
            intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
            v.context.applicationContext.startActivity(intent)
        }

    private val perceptionListener =
        View.OnClickListener { v ->
            userDataDto.performSafeUserAchievementsInitialization()
            val currentAchievements = userDataDto.userAchievements as AchievementsModel.UserCalendarModel
            currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)]?.achievedSensorial = true
            userDataDto.userAchievements = currentAchievements
            val intent = Intent(v.context.applicationContext, CongratulationsActivity::class.java)
            intent.putExtra("comesFrom", "percepción sensorial")
            intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
            v.context.applicationContext.startActivity(intent)
        }

    private val profileListener =
        View.OnClickListener { v ->
            val intent = Intent(v.context.applicationContext, ProfileActivity::class.java)
            intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
            v.context.applicationContext.startActivity(intent)
        }

    private val settingsListener =
        View.OnClickListener { v ->
            val intent = Intent(v.context.applicationContext, SettingsActivity::class.java)
            intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
            v.context.applicationContext.startActivity(intent)
        }

    //endregion

    init {
        mModel = model
    }
}