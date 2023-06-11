package space.timur.workouttimer.domain.repository

import android.content.Context

interface SettingsRepository {

    fun setRoundTime(seconds: Long, context: Context)

    fun setRestTime(seconds: Long, context: Context)

    fun setNumberOfRounds(number: Int, context: Context)

    fun getRoundTime( context: Context): Long

    fun getRestTime(context: Context): Long

    fun getNumberOfRounds(context: Context): Int

}