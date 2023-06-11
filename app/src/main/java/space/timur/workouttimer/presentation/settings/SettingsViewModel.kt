package space.timur.workouttimer.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import space.timur.workouttimer.domain.repository.SettingsRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val context: Context
): ViewModel() {

    fun setRoundTime(seconds: Long){
        settingsRepository.setRoundTime(seconds, context)
    }

    fun setRestTime(seconds: Long){
        settingsRepository.setRestTime(seconds, context)
    }

    fun setNumberOfRounds(number: Int){
        settingsRepository.setNumberOfRounds(number, context)
    }

    fun getRoundTime() : Long {
        return settingsRepository.getRoundTime(context)
    }

    fun getRestTime(): Long {
        return settingsRepository.getRestTime(context)
    }

    fun getNumberOfRounds(): Int {
        return settingsRepository.getNumberOfRounds(context)
    }

}