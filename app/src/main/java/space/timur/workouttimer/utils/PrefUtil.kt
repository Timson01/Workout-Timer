package space.timur.workouttimer.utils

import android.content.Context
import space.timur.workouttimer.common.Constants.PREVIOUS_TIMER_LENGTH_SECONDS_ID
import space.timur.workouttimer.common.Constants.SECONDS_REMAINING_ID
import space.timur.workouttimer.common.Constants.SHARED_PREFERENCES_NAME
import space.timur.workouttimer.common.Constants.TIMER_STATE_ID
import space.timur.workouttimer.presentation.timer.TimerFragment
import space.timur.workouttimer.presentation.timer.TimerViewModel

class PrefUtil {

    companion object {

        fun getTimerLength(context: Context): Int{
            //placeholder
            return 1
        }

        fun getPreviousTimerLengthSeconds(context: Context): Long{
            val preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context){
            val editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        fun getTimerState(context: Context): TimerViewModel.TimerState{
            val preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return TimerViewModel.TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerViewModel.TimerState, context: Context){
            val editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }

        fun getSecondsRemaining(context: Context): Long{
            val preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context){
            val editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }
    }

}