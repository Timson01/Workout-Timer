package space.timur.workouttimer.presentation.timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import space.timur.workouttimer.R
import space.timur.workouttimer.domain.repository.SettingsRepository
import space.timur.workouttimer.domain.repository.TimerRepository
import space.timur.workouttimer.framework.broadcast.TimerExpiredReceiver
import space.timur.workouttimer.presentation.notification.NotificationUtil
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val settingsRepository: SettingsRepository,
    private val context: Context
) : ViewModel() {


    fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long {
        val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TimerExpiredReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
        timerRepository.setAlarmSetTime(nowSeconds, context)
        return wakeUpTime
    }

    fun removeAlarm(context: Context) {
        val intent = Intent(context, TimerExpiredReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        timerRepository.setAlarmSetTime(0, context)
    }

    val nowSeconds: Long
        get() = Calendar.getInstance().timeInMillis / 1000

    enum class TimerState {
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private val _timerState = MutableLiveData<TimerState>()
    val timerState: LiveData<TimerState> = _timerState

    private val _secondsRemaining = MutableLiveData<Long>()
    val secondsRemaining: LiveData<Long> = _secondsRemaining

    private val _timerLengthSeconds = MutableLiveData<Long>()
    val timerLengthSeconds: LiveData<Long> = _timerLengthSeconds

    private val _numberOfRounds = MutableLiveData<Int>()
    val numberOfRounds: LiveData<Int> = _numberOfRounds

    private var _restTime = false
    private var _mediaPlayer: MediaPlayer? = null

    init {
        _timerState.value = TimerState.Stopped
        _secondsRemaining.value = 0L
        _timerLengthSeconds.value = 0L
        _numberOfRounds.value = 0
    }

    override fun onCleared() {
        super.onCleared()
        _mediaPlayer?.release()
        _mediaPlayer = null
    }

    fun startTimer() {
        _timerState.value = TimerState.Running
        playSound()

        timer = object : CountDownTimer(_secondsRemaining.value!! * 1000, 1000) {
            override fun onFinish() {
                playSound()
                onTimerFinished()
            }

            override fun onTick(millisUntilFinished: Long) {
                _secondsRemaining.value = millisUntilFinished / 1000
            }
        }.start()
    }

    fun onPause() {
        if (_timerState.value == TimerState.Running) {
            timer.cancel()
            val wakeUpTime = setAlarm(context, nowSeconds, _secondsRemaining.value ?: 0L)
            NotificationUtil.showTimerRunning(context, wakeUpTime)
        } else if (_timerState.value == TimerState.Paused) {
            NotificationUtil.showTimerPaused(context)
        }

        timerRepository.setPreviousTimerLengthSeconds(_timerLengthSeconds.value ?: 0L, context)
        timerRepository.setSecondsRemaining(_secondsRemaining.value ?: 0L, context)
        timerRepository.setTimerState(_timerState.value ?: TimerState.Stopped, context)
    }

    fun pauseTimer() {
        timer.cancel()
        _timerState.value = TimerState.Paused
    }

    fun stopTimer() {
        timer.cancel()
        _timerState.value = TimerState.Stopped
        setNewTimerLength()
        setNewNumberOfRounds()
        _secondsRemaining.value = _timerLengthSeconds.value
    }

    fun initTimer() {
        _timerState.value = timerRepository.getTimerState(context)

        if (_timerState.value == TimerState.Stopped) {
            setNewTimerLength()
            setNewNumberOfRounds()
        } else {
            setPreviousTimerLength()
        }

        _secondsRemaining.value =
            if (_timerState.value == TimerState.Running || _timerState.value == TimerState.Paused) {
                timerRepository.getSecondsRemaining(context)
            } else {
                _timerLengthSeconds.value
            }

        val alarmSetTime = timerRepository.getAlarmSetTime(context)
        if (alarmSetTime > 0)
            _secondsRemaining.value?.let { secondsRemaining ->
                _secondsRemaining.value = secondsRemaining - (nowSeconds - alarmSetTime)
            }

        if((_secondsRemaining.value ?: 0L) <= 0){
           onTimerFinished()
        } else if (_timerState.value == TimerState.Running) {
            startTimer()
        }
    }

    private fun onTimerFinished() {
        if(_numberOfRounds.value!! > 0){
            if(!_restTime){
                _numberOfRounds.value = _numberOfRounds.value!! - 1
                val restTime = settingsRepository.getRestTime(context)
                _secondsRemaining.value = restTime
            } else {
                val roundTime = settingsRepository.getRoundTime(context)
                _secondsRemaining.value = roundTime
            }
            _restTime = !_restTime
            startTimer()
        } else {
            _timerState.value = TimerState.Stopped
            setNewTimerLength()
            setNewNumberOfRounds()
            _secondsRemaining.value = _timerLengthSeconds.value
        }
    }

    private fun setNewNumberOfRounds(){
        val number = settingsRepository.getNumberOfRounds(context)
        _numberOfRounds.value = number
    }

    fun getNumberOfRounds(): Int{
        return settingsRepository.getNumberOfRounds(context)
    }

    private fun setNewTimerLength() {
        val lengthInSeconds = settingsRepository.getRoundTime(context)
        _timerLengthSeconds.value = lengthInSeconds
    }

    private fun setPreviousTimerLength() {
        _timerLengthSeconds.value = timerRepository.getPreviousTimerLengthSeconds(context)
    }

    private fun playSound() {
        _mediaPlayer = MediaPlayer.create(context, R.raw.timer_sound)
        _mediaPlayer?.start()
    }
}