package space.timur.workouttimer.presentation.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import space.timur.workouttimer.R

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    enum class SettingsButton {
        RoundTime, RestTime
    }

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preferences, rootKey)

        val roundTimePreference = findPreference<Preference>("round_time_preference")
        roundTimePreference?.onPreferenceClickListener = this

        val restTimePreference = findPreference<Preference>("rest_time_preference")
        restTimePreference?.onPreferenceClickListener = this

        val numberOfRoundsPreference = findPreference<Preference>("number_of_rounds_preference")
        numberOfRoundsPreference?.onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when(preference.key) {
            "round_time_preference" -> {
                showTimePickerDialog(SettingsButton.RoundTime)
                return true
            }
            "rest_time_preference" -> {
                showTimePickerDialog(SettingsButton.RestTime)
                return true
            }
            "number_of_rounds_preference" -> {
                showNumberOfRoundPickerDialog()
                return true
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        updateSummary()
    }

    private fun updateSummary() {
        val roundTimePreference = findPreference<Preference>("round_time_preference")
        roundTimePreference?.summary = formatTime(viewModel.getRoundTime().toInt())

        val restTimePreference = findPreference<Preference>("rest_time_preference")
        restTimePreference?.summary = formatTime(viewModel.getRestTime().toInt())

        val numberOfRoundsPreference = findPreference<Preference>("number_of_rounds_preference")
        numberOfRoundsPreference?.summary = formatTime(viewModel.getNumberOfRounds())
    }

    private fun showTimePickerDialog(button: SettingsButton) {

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_time_picker, null)
        val minutePicker = dialogView.findViewById<NumberPicker>(R.id.minute_picker)
        val secondPicker = dialogView.findViewById<NumberPicker>(R.id.second_picker)

        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        secondPicker.minValue = 0
        secondPicker.maxValue = 59

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle(
            when(button){
                SettingsButton.RoundTime -> "Select Round Time"
                SettingsButton.RestTime -> "Select Rest Time"
            })
        dialogBuilder.setPositiveButton("OK") { _, _ ->

            val selectedMinutes = minutePicker.value
            val selectedSeconds = secondPicker.value

            val totalTimeInSeconds = (selectedMinutes * 60) + selectedSeconds
            when(button){
                SettingsButton.RoundTime -> {
                    viewModel.setRoundTime(totalTimeInSeconds.toLong())
                    val roundTimePreference = findPreference<Preference>("round_time_preference")
                    roundTimePreference?.summary = formatTime(totalTimeInSeconds)
                }
                SettingsButton.RestTime -> {
                    viewModel.setRestTime(totalTimeInSeconds.toLong())
                    val restTimePreference = findPreference<Preference>("rest_time_preference")
                    restTimePreference?.summary = formatTime(totalTimeInSeconds)
                }
            }
        }
        dialogBuilder.setNegativeButton("Cancel", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun showNumberOfRoundPickerDialog() {

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_number_of_rounds_picker, null)
        val numberOfRounds = dialogView.findViewById<NumberPicker>(R.id.number_of_rounds)

        numberOfRounds.minValue = 0
        numberOfRounds.maxValue = 100

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle(
            "Select number of rounds")
        dialogBuilder.setPositiveButton("OK") { _, _ ->

            val selectedNumberOfRounds = numberOfRounds.value
            viewModel.setNumberOfRounds(selectedNumberOfRounds)

            val numberOfRoundsPreference = findPreference<Preference>("number_of_rounds_preference")
            numberOfRoundsPreference?.summary = "$selectedNumberOfRounds"

        }
        dialogBuilder.setNegativeButton("Cancel", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun formatTime(totalSeconds: Int): String {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

}