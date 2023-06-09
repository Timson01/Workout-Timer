package space.timur.workouttimer.framework.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import space.timur.workouttimer.domain.repository.TimerRepository
import space.timur.workouttimer.presentation.timer.TimerViewModel
import javax.inject.Inject

@AndroidEntryPoint
class TimerExpiredReceiver @Inject constructor(
    private val timerRepository : TimerRepository,
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        timerRepository.setTimerState(TimerViewModel.TimerState.Stopped, context)
        timerRepository.setAlarmSetTime(0, context)
    }
}