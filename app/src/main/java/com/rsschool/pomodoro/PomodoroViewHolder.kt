package com.rsschool.pomodoro

import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.rsschool.pomodoro.databinding.TimerItemBinding

class PomodoroViewHolder (

    private val binding: TimerItemBinding,
    private val listener: PomodoroInterface,
    private val resources: Resources
) : RecyclerView.ViewHolder(binding.root) {

    private var countDown: CountDownTimer? = null

    fun bind(timer: Timer) {
        binding.timer.text = timer.currentMs.displayTime()

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
                listener.stop(timer.id, timer.currentMs)
            } else {
                listener.start(timer.id, timer.currentMs)
            }
        }

//        binding.restartButton.setOnClickListener { listener.reset(timer.id) }

        binding.deleteButton.setOnClickListener { listener.delete(timer.id) }
    }

    private fun startTimer(timer: Timer) {
        binding.startPauseButton.text = "Stop"

        countDown?.cancel()
        countDown = getCountDownTimer(timer)
        countDown?.start()

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer(timer: Timer) {

        binding.startPauseButton.text = "Start"

        countDown?.cancel()

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun getCountDownTimer(timer: Timer): CountDownTimer {
        return object : CountDownTimer(PERIOD, UNIT_SEC) {
            val interval = UNIT_SEC

            override fun onTick(millisUntilFinished: Long) {
                timer.currentMs -= interval
                binding.timer.text = timer.currentMs.displayTime()
            }

            override fun onFinish() {
                binding.timer.text = timer.currentMs.displayTime()
            }
        }
    }

    private fun Long.displayTime(): String {
        if (this <= 0L) {
            return START_TIME
        }
        val h = this / 1000 / 3600
        val m = this / 1000 % 3600 / 60
        val s = this / 1000 % 60

        return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}"
    }

    private fun displaySlot(count: Long): String {
        return if (count / 10L > 0) {
            "$count"
        } else {
            "0$count"
        }
    }

    private companion object {

        private const val START_TIME = "00:00:00"
        private const val UNIT_SEC = 1000L
        private const val PERIOD = 1000L * 60L * 60L * 24L // Day
    }
}