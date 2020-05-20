import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import it.androidclient.Services.TodaysPostModel
import it.androidclient.UserCtx.AchievementsModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import kotlin.reflect.KProperty


/**
 * Represents a single [SharedPreferences] file.
 *
 * Usage:
 *
 * ```kotlin
 * class UserPreferences(ctx: Context) : Preferences(ctx) {
 *   var emailAccount by stringPref()
 *   var showSystemAppsPreference by booleanPref()
 * }
 * ```
 */

@Suppress("UNCHECKED_CAST", "unused")
abstract class Preferences(private var context: Context, private val name: String? = null) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(name ?: javaClass.simpleName, Context.MODE_PRIVATE)
    }

    private val listeners = mutableListOf<SharedPrefsListener>()

    abstract class PrefDelegate<T>(val prefKey: String?) {
        abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
        abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
    }

    interface SharedPrefsListener {
        fun onSharedPrefChanged(property: KProperty<*>)
    }

    fun addListener(sharedPrefsListener: SharedPrefsListener) {
        listeners.add(sharedPrefsListener)
    }

    fun removeListener(sharedPrefsListener: SharedPrefsListener) {
        listeners.remove(sharedPrefsListener)
    }

    fun clearListeners() = listeners.clear()

    enum class StorableType {
        String,
        Int,
        Float,
        Boolean,
        Long,
        StringSet,
        TodaysPostObject,
        AchievementsModelObject
    }

    inner class GenericPrefDelegate<T>(prefKey: String? = null, private val defaultValue: T?, val type: StorableType) :
        PrefDelegate<T?>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? =
            when (type) {
                StorableType.String ->
                    prefs.getString(prefKey ?: property.name, defaultValue as String?) as T?
                StorableType.Int ->
                    prefs.getInt(prefKey ?: property.name, defaultValue as Int) as T?
                StorableType.Float ->
                    prefs.getFloat(prefKey ?: property.name, defaultValue as Float) as T?
                StorableType.Boolean ->
                    prefs.getBoolean(prefKey ?: property.name, defaultValue as Boolean) as T?
                StorableType.Long ->
                    prefs.getLong(prefKey ?: property.name, defaultValue as Long) as T?
                StorableType.StringSet ->
                    prefs.getStringSet(prefKey ?: property.name, defaultValue as Set<String>) as T?
                StorableType.TodaysPostObject -> {
                    val serialized = prefs.getString(prefKey ?: property.name, defaultValue as String?)
                    val gson = Gson()
                    gson.fromJson(serialized, TodaysPostModel.Result::class.java) as T?
                }
                StorableType.AchievementsModelObject -> {
                    val serialized = prefs.getString(prefKey ?: property.name, defaultValue as String?)
                    val gson = GsonBuilder().registerTypeAdapter(
                        LocalDateTime::class.java,
                        JsonDeserializer<Any?> { json, _, _ ->
                            LocalDateTime.parse(json.asJsonPrimitive.asString)
                        }).create()
                    gson.fromJson(serialized, AchievementsModel.UserCalendarModel::class.java) as T?
                }
            }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            when (type) {
                StorableType.String -> {
                    prefs.edit().putString(prefKey ?: property.name, value as String?).apply()
                    onPrefChanged(property)
                }
                StorableType.Int -> {
                    prefs.edit().putInt(prefKey ?: property.name, value as Int).apply()
                    onPrefChanged(property)
                }
                StorableType.Float -> {
                    prefs.edit().putFloat(prefKey ?: property.name, value as Float).apply()
                    onPrefChanged(property)
                }
                StorableType.Boolean -> {
                    prefs.edit().putBoolean(prefKey ?: property.name, value as Boolean).apply()
                    onPrefChanged(property)
                }
                StorableType.Long -> {
                    prefs.edit().putLong(prefKey ?: property.name, value as Long).apply()
                    onPrefChanged(property)
                }
                StorableType.StringSet -> {
                    prefs.edit().putStringSet(prefKey ?: property.name, value as Set<String>).apply()
                    onPrefChanged(property)
                }
                StorableType.TodaysPostObject -> {
                    val gson = Gson()
                    val serialized = gson.toJson(value)
                    prefs.edit().putString(prefKey ?: property.name, serialized).apply()
                    onPrefChanged(property)
                }
                StorableType.AchievementsModelObject -> {
                    val gson = Gson()
                    val serialized = gson.toJson(value)
                    prefs.edit().putString(prefKey ?: property.name, serialized).apply()
                    onPrefChanged(property)
                }
            }
        }

    }

    fun stringPref(prefKey: String? = null, defaultValue: String? = null) =
        GenericPrefDelegate(prefKey, defaultValue, StorableType.String)

    fun todaysPostPref(prefKey: String? = null, defaultValue:Any?=null) =
        GenericPrefDelegate(prefKey, defaultValue, StorableType.TodaysPostObject)

    fun achievementsPref(prefKey: String? = null, defaultValue:Any?=null) =
        GenericPrefDelegate(prefKey, defaultValue, StorableType.AchievementsModelObject)

    fun intPref(prefKey: String? = null, defaultValue: Int = 0) =
        GenericPrefDelegate(prefKey, defaultValue, StorableType.Int)

    fun floatPref(prefKey: String? = null, defaultValue: Float = 0f) =
        GenericPrefDelegate(prefKey, defaultValue, StorableType.Float)

    fun booleanPref(prefKey: String? = null, defaultValue: Boolean = false) =
        GenericPrefDelegate(prefKey, defaultValue, StorableType.Boolean)

    fun longPref(prefKey: String? = null, defaultValue: Long = 0L) =
        GenericPrefDelegate(prefKey, defaultValue, StorableType.Long)

    fun stringSetPref(prefKey: String? = null, defaultValue: Set<String> = HashSet()) =
        GenericPrefDelegate(prefKey, defaultValue, StorableType.StringSet)

    private fun onPrefChanged(property: KProperty<*>) {
        listeners.forEach { it.onSharedPrefChanged(property) }
    }
}