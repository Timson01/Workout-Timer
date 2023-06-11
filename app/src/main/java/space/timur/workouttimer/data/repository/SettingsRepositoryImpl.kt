package space.timur.workouttimer.data.repository

import android.content.Context
import space.timur.workouttimer.common.Constants
import space.timur.workouttimer.domain.repository.SettingsRepository

class SettingsRepositoryImpl : SettingsRepository {

    override fun setRoundTime(seconds: Long, context: Context) {
        val editor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        editor.putLong(Constants.ROUND_TIME_ID, seconds)
        editor.apply()
    }

    override fun setRestTime(seconds: Long, context: Context) {
        val editor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        editor.putLong(Constants.REST_TIME_ID, seconds)
        editor.apply()
    }

    override fun setNumberOfRounds(number: Int, context: Context) {
        val editor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        editor.putInt(Constants.NUMBER_OF_ROUNDS_ID, number)
        editor.apply()
    }

    override fun getRoundTime(context: Context): Long {
        val preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return preferences.getLong(Constants.ROUND_TIME_ID, 0)
    }

    override fun getRestTime(context: Context): Long {
        val preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return preferences.getLong(Constants.REST_TIME_ID, 0)
    }

    override fun getNumberOfRounds(context: Context): Int {
        val preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return preferences.getInt(Constants.NUMBER_OF_ROUNDS_ID, 0)
    }

}