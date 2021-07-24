package com.rsschool.pomodoro

import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.rsschool.pomodoro.databinding.TimerItemBinding
import kotlinx.coroutines.*

class PomodoroViewHolder (

    private val binding: TimerItemBinding,
    private val listener: PomodoroInterface,
    private val resources: Resources
) : RecyclerView.ViewHolder(binding.root) {

    private var systemTime = 0L

    private var tickJob: Job? = null

    fun bind(timer: Timer) {

        binding.timer.text = timer.currentMs.displayTime()
        binding.customView.setCurrent(0L)
        binding.item.setBackgroundColor(resources.getColor(R.color.transparent))

        if(timer.isFinished) {
            binding.startPauseButton.isEnabled = false
            binding.item.setBackgroundColor(resources.getColor(R.color.pink))
        }

        if (timer.isStarted) {
            startTimer(timer)
        } else {
            stopTimer(timer)
        }

        initButtonsListeners(timer)
    }

    private fun initButtonsListeners(timer: Timer) {
        binding.startPauseButton.setOnClickListener {
            if (timer.isStarted) {
                listener.stop(timer.id, timer.initialValue, timer.currentMs)
            } else {
                listener.start(timer.id, timer.initialValue, timer.currentMs)
            }
        }

        binding.deleteButton.setOnClickListener { listener.delete(timer.id) }
        binding.customView.setPeriod(timer.initialValue)
    }

    private fun startTimer(timer: Timer) {
        binding.startPauseButton.text = "Stop"

        systemTime = System.currentTimeMillis()

        tickJob?.cancel()

        tickJob = GlobalScope.launch(Dispatchers.Main) {
            val interval = UNIT_SEC

            while (timer.currentMs > 0) {

                timer.currentMs -= interval
                binding.timer.text = timer.currentMs.displayTime()
                binding.customView.setCurrent(timer.initialValue - timer.currentMs)
                if (timer.currentMs <= 0) {
                    listener.finish(timer.id, timer.initialValue, timer.currentMs)
                    break
                }
                delay(interval)
            }
        }

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()

    }

    private fun stopTimer(timer: Timer) {

        binding.startPauseButton.text = "Start"

        tickJob?.cancel()

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private companion object {
        private const val STOP_TIME = "00:00:00"
        private const val UNIT_SEC = 1000L

    }
}