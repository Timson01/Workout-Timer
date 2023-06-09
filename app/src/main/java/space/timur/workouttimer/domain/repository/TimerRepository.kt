package space.timur.workouttimer.domain.repository

import android.content.Context
import space.timur.workouttimer.presentation.timer.TimerViewModel

interface TimerRepository {

    fun getTimerLength(context: Context): Int

    fun getPreviousTimerLengthSeconds(context: Context): Long

    fun setPreviousTimerLengthSeconds(seconds: Long, context: Context)

    fun getTimerState(context: Context): TimerViewModel.TimerState

    fun setTimerState(state: TimerViewModel.TimerState, context: Context)

    fun getSecondsRemaining(context: Context): Long

    fun setSecondsRemaining(seconds: Long, context: Context)

    fun getAlarmSetTime(context: Context): Long

    fun setAlarmSetTime(time: Long, context: Context)

}