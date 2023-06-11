package space.timur.workouttimer.data.repository

import android.content.Context
import space.timur.workouttimer.common.Constants
import space.timur.workouttimer.common.Constants.ALARM_SET_TIME_ID
import space.timur.workouttimer.common.Constants.NUMBER_OF_ROUNDS_ID
import space.timur.workouttimer.common.Constants.REST_TIME_ID
import space.timur.workouttimer.common.Constants.ROUND_TIME_ID
import space.timur.workouttimer.domain.repository.TimerRepository
import space.timur.workouttimer.presentation.timer.TimerViewModel

class TimerRepositoryImpl : TimerRepository {

    override fun getPreviousTimerLengthSeconds(context: Context): Long {
        val preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return preferences.getLong(Constants.PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
    }

    override fun setPreviousTimerLengthSeconds(seconds: Long, context: Context) {
        val editor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        editor.putLong(Constants.PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
        editor.apply()
    }

    override fun getTimerState(context: Context): TimerViewModel.TimerState {
        val preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val ordinal = preferences.getInt(Constants.TIMER_STATE_ID, 0)
        return TimerViewModel.TimerState.values()[ordinal]
    }

    override fun setTimerState(state: TimerViewModel.TimerState, context: Context) {
        val editor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        val ordinal = state.ordinal
        editor.putInt(Constants.TIMER_STATE_ID, ordinal)
        editor.apply()
    }

    override fun getSecondsRemaining(context: Context): Long {
        val preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return preferences.getLong(Constants.SECONDS_REMAINING_ID, 0)
    }

    override fun setSecondsRemaining(seconds: Long, context: Context) {
        val editor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        editor.putLong(Constants.SECONDS_REMAINING_ID, seconds)
        editor.apply()
    }

    override fun getAlarmSetTime(context: Context): Long {
        val preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return preferences.getLong(ALARM_SET_TIME_ID, 0)
    }

    override fun setAlarmSetTime(time: Long, context: Context) {
        val editor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        editor.putLong(ALARM_SET_TIME_ID, time)
        editor.apply()
    }

}