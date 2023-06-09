package space.timur.workouttimer.presentation.timer

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import space.timur.workouttimer.utils.PrefUtil

class TimerViewModel(application: Application) : AndroidViewModel(application) {

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

    init {
        _timerState.value = TimerState.Stopped
        _secondsRemaining.value = 0L
        _timerLengthSeconds.value = 0L
    }

    fun startTimer() {
        _timerState.value = TimerState.Running

        timer = object : CountDownTimer(_secondsRemaining.value!! * 1000, 1000) {
            override fun onFinish() {
                onTimerFinished()
            }

            override fun onTick(millisUntilFinished: Long) {
                _secondsRemaining.value = millisUntilFinished / 1000
            }
        }.start()
    }

    fun onPause(){
        if (_timerState.value == TimerState.Running){
            timer.cancel()
            //TODO: start background timer and show notification
        }
        else if (_timerState.value == TimerState.Paused){
            //TODO: show notification
        }

        PrefUtil.setPreviousTimerLengthSeconds(_timerLengthSeconds.value ?: 0L, getApplication())
        PrefUtil.setSecondsRemaining(_secondsRemaining.value ?: 0L, getApplication())
        PrefUtil.setTimerState(_timerState.value ?: TimerState.Stopped, getApplication())
    }

    fun pauseTimer() {
        timer.cancel()
        _timerState.value = TimerState.Paused
    }

    fun stopTimer() {
        timer.cancel()
        onTimerFinished()
    }

    fun initTimer() {
        _timerState.value = PrefUtil.getTimerState(getApplication())
        println(_timerState.value.toString())

        if (_timerState.value == TimerState.Stopped) {
            setNewTimerLength()
        } else {
            setPreviousTimerLength()
        }

        _secondsRemaining.value = if (_timerState.value == TimerState.Running || _timerState.value == TimerState.Paused) {
            PrefUtil.getSecondsRemaining(getApplication())
        } else {
            _timerLengthSeconds.value
        }

        if (_timerState.value == TimerState.Running) {
            startTimer()
        }
    }

    private fun onTimerFinished() {
        _timerState.value = TimerState.Stopped
        setNewTimerLength()

        _secondsRemaining.value = _timerLengthSeconds.value
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtil.getTimerLength(getApplication())
        _timerLengthSeconds.value = (lengthInMinutes * 60L)
    }

    private fun setPreviousTimerLength() {
        _timerLengthSeconds.value = PrefUtil.getPreviousTimerLengthSeconds(getApplication())
    }
}