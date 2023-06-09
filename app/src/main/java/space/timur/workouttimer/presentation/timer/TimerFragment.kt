package space.timur.workouttimer.presentation.timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import space.timur.workouttimer.R
import space.timur.workouttimer.databinding.FragmentTimerBinding
import space.timur.workouttimer.presentation.notification.NotificationUtil

@AndroidEntryPoint
class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding
    private val viewModel: TimerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabStart.setOnClickListener {
            viewModel.startTimer()
        }

        binding.fabPause.setOnClickListener {
            viewModel.pauseTimer()
        }

        binding.fabStop.setOnClickListener {
            viewModel.stopTimer()
        }

        binding.settingsButton.setOnClickListener{
            findNavController().navigate(R.id.action_timerFragment_to_settingsFragment)
        }

        viewModel.timerState.observe(viewLifecycleOwner) { timerState ->
            updateButtons(timerState)
        }

        viewModel.secondsRemaining.observe(viewLifecycleOwner) { secondsRemaining ->
            updateCountdownUI(secondsRemaining)
        }

        viewModel.numberOfRounds.observe(viewLifecycleOwner){
            updateNumberOfRoundUI(it)
        }

        viewModel.timerLengthSeconds.observe(viewLifecycleOwner) { timerLengthSeconds ->
            // Update UI with the new timer length value
            binding.progressCountdown.max = timerLengthSeconds.toInt()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.initTimer()
        viewModel.removeAlarm(requireContext())
        NotificationUtil.hideTimerNotification(requireContext())
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun updateNumberOfRoundUI(numberReminder: Int) {
        val numberOfRounds = viewModel.getNumberOfRounds()
        binding.textViewNumberOfRounds.text = "${numberReminder}/${numberOfRounds}"
    }

    private fun updateCountdownUI(secondsRemaining: Long) {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        binding.textViewCountdown.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
        binding.progressCountdown.progress = (viewModel.timerLengthSeconds.value!! - secondsRemaining).toInt()
    }

    private fun updateButtons(timerState: TimerViewModel.TimerState) {
        when (timerState) {
            TimerViewModel.TimerState.Running -> {
                binding.fabStart.isEnabled = false
                binding.fabPause.isEnabled = true
                binding.fabStop.isEnabled = true
            }
            TimerViewModel.TimerState.Stopped -> {
                binding.fabStart.isEnabled = true
                binding.fabPause.isEnabled = false
                binding.fabStop.isEnabled = false
            }
            TimerViewModel.TimerState.Paused -> {
                binding.fabStart.isEnabled = true
                binding.fabPause.isEnabled = false
                binding.fabStop.isEnabled = true
            }
        }
    }

}