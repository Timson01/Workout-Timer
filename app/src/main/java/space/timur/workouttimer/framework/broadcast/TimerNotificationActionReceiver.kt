package space.timur.workouttimer.framework.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import space.timur.workouttimer.common.Constants
import space.timur.workouttimer.data.repository.SettingsRepositoryImpl
import space.timur.workouttimer.data.repository.TimerRepositoryImpl
import space.timur.workouttimer.presentation.notification.NotificationUtil
import space.timur.workouttimer.presentation.timer.TimerViewModel

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val timerRepository = TimerRepositoryImpl()
        val settingsRepository = SettingsRepositoryImpl()
        val viewModel = TimerViewModel(timerRepository, settingsRepository, context)
        when (intent.action){
            Constants.ACTION_STOP -> {
                viewModel.removeAlarm(context)
                timerRepository.setTimerState(TimerViewModel.TimerState.Stopped, context)
                NotificationUtil.hideTimerNotification(context)
            }
            Constants.ACTION_PAUSE -> {
                var secondsRemaining = timerRepository.getSecondsRemaining(context)
                val alarmSetTime = timerRepository.getAlarmSetTime(context)
                val nowSeconds = viewModel.nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                timerRepository.setSecondsRemaining(secondsRemaining, context)

                viewModel.removeAlarm(context)
                timerRepository.setTimerState(TimerViewModel.TimerState.Paused, context)
                NotificationUtil.showTimerPaused(context)
            }
            Constants.ACTION_RESUME -> {
                val secondsRemaining = timerRepository.getSecondsRemaining(context)
                val wakeUpTime = viewModel.setAlarm(context, viewModel.nowSeconds, secondsRemaining)
                timerRepository.setTimerState(TimerViewModel.TimerState.Running, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            Constants.ACTION_START -> {
                val secondsRemaining = settingsRepository.getRoundTime(context)
                val wakeUpTime = viewModel.setAlarm(context, viewModel.nowSeconds, secondsRemaining)
                timerRepository.setTimerState(TimerViewModel.TimerState.Running, context)
                timerRepository.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}