package it.androidclient.Line

import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import it.androidclient.R
import it.androidclient.UserCtx.AchievementsModel
import it.androidclient.UserCtx.UserDataDto
import it.androidclient.Views.ReadActivity
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

    override fun onBindViewHolder(holder: LineHolder, position: Int) {
        holder.title.text = String.format(
            Locale.getDefault(), "%s",
            mModel!![position].title
        )

        var color = when (position) {
            0 -> R.color.applicationList1
            1 -> R.color.applicationList2
            2 -> R.color.applicationList3
            3 -> R.color.applicationList4
            4 -> R.color.applicationList5
            5 -> R.color.applicationList6
            else -> R.color.applicationList1
        }

        color = when (mModel[position].completed){
            true -> R.color.applicationListDone
            else -> color
        }

        holder.isCompleted.visibility = if (mModel[position].completed) View.VISIBLE else View.GONE

        val listener = when (position){
            0 -> readListener
            1 -> languageListener
            2 -> abstractListener
            3 -> concentrationListener
            4 -> praxiasListener
            5 -> perceptionListener
            else -> readListener
        }
        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context.applicationContext, color))
        holder.itemView.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return mModel?.size ?: 0
    }

    //region Listeners
    private val readListener =
        View.OnClickListener { v ->
            val intent = Intent(v.context.applicationContext, ReadActivity::class.java)
            intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
            v.context.applicationContext.startActivity(intent)
        }

    private val languageListener =
        View.OnClickListener { v ->
            if (userDataDto.userAchievements == null){
                userDataDto.userAchievements = AchievementsModel.UserCalendarModel(hashMapOf())
            }

            val currentAchievements = userDataDto.userAchievements as AchievementsModel.UserCalendarModel
            if (currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)] == null) {
                currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)] =
                    AchievementsModel.DailyMilestonesAchieved(
                        achievedReading = false,
                        achievedLanguage = false,
                        achievedAbstractThinking = false,
                        achievedConcentration = false,
                        achievedPraxias = false,
                        achievedSensorial = false
                    )
            }
            currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)]?.achievedLanguage = true
            userDataDto.userAchievements = currentAchievements
            Toast.makeText(v.context.applicationContext, "Lenguaje not implemented", Toast.LENGTH_SHORT).show()
            notifyDataSetChanged()
        }

    private val abstractListener =
        View.OnClickListener { v ->
            if (userDataDto.userAchievements == null){
                userDataDto.userAchievements = AchievementsModel.UserCalendarModel(hashMapOf())
            }

            val currentAchievements = userDataDto.userAchievements as AchievementsModel.UserCalendarModel
            if (currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)] == null) {
                currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)] =
                    AchievementsModel.DailyMilestonesAchieved(
                        achievedReading = false,
                        achievedLanguage = false,
                        achievedAbstractThinking = false,
                        achievedConcentration = false,
                        achievedPraxias = false,
                        achievedSensorial = false
                    )
            }
            currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)]?.achievedAbstractThinking = true
            userDataDto.userAchievements = currentAchievements
            Toast.makeText(v.context.applicationContext, "Pensamiento abstracto not implemented", Toast.LENGTH_SHORT).show()
            notifyDataSetChanged()
        }

    private val concentrationListener =
        View.OnClickListener { v ->
            if (userDataDto.userAchievements == null){
                userDataDto.userAchievements = AchievementsModel.UserCalendarModel(hashMapOf())
            }

            val currentAchievements = userDataDto.userAchievements as AchievementsModel.UserCalendarModel
            if (currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)] == null) {
                currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)] =
                    AchievementsModel.DailyMilestonesAchieved(
                        achievedReading = false,
                        achievedLanguage = false,
                        achievedAbstractThinking = false,
                        achievedConcentration = false,
                        achievedPraxias = false,
                        achievedSensorial = false
                    )
            }
            currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)]?.achievedConcentration = true
            userDataDto.userAchievements = currentAchievements
            Toast.makeText(v.context.applicationContext, "Concentracion not implemented", Toast.LENGTH_SHORT).show()
            notifyDataSetChanged()
        }

    private val praxiasListener =
        View.OnClickListener { v ->
            if (userDataDto.userAchievements == null){
                userDataDto.userAchievements = AchievementsModel.UserCalendarModel(hashMapOf())
            }

            val currentAchievements = userDataDto.userAchievements as AchievementsModel.UserCalendarModel
            if (currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)] == null) {
                currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)] =
                    AchievementsModel.DailyMilestonesAchieved(
                        achievedReading = false,
                        achievedLanguage = false,
                        achievedAbstractThinking = false,
                        achievedConcentration = false,
                        achievedPraxias = false,
                        achievedSensorial = false
                    )
            }
            currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)]?.achievedPraxias = true
            userDataDto.userAchievements = currentAchievements
            Toast.makeText(v.context.applicationContext, "Praxias not implemented", Toast.LENGTH_SHORT).show()
            notifyDataSetChanged()
        }

    private val perceptionListener =
        View.OnClickListener { v ->
            if (userDataDto.userAchievements == null){
                userDataDto.userAchievements = AchievementsModel.UserCalendarModel(hashMapOf())
            }

            val currentAchievements = userDataDto.userAchievements as AchievementsModel.UserCalendarModel
            if (currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)] == null) {
                currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)] =
                    AchievementsModel.DailyMilestonesAchieved(
                        achievedReading = false,
                        achievedLanguage = false,
                        achievedAbstractThinking = false,
                        achievedConcentration = false,
                        achievedPraxias = false,
                        achievedSensorial = false
                    )
            }
            currentAchievements.achievementList[LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)]?.achievedSensorial = true
            userDataDto.userAchievements = currentAchievements
            Toast.makeText(v.context.applicationContext, "Percepcion sensorial not implemented", Toast.LENGTH_SHORT).show()
            notifyDataSetChanged()
        }

    //endregion

    init {
        mModel = model
    }
}