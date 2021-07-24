package com.rsschool.pomodoro

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsschool.pomodoro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), PomodoroInterface {
    private lateinit var binding: ActivityMainBinding

    private val pomodoroAdapter = PomodoroAdapter(this)
    private val timers = mutableListOf<Timer>()
    private var nextId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = pomodoroAdapter
        }

        binding.addNewTimerButton.setOnClickListener {
            val minutes = binding.timerValue.text.toString()
            if(validateInput(minutes)) {
                val initialValue = toMilliseconds(minutes)
                timers.add(Timer(nextId++, initialValue, currentMs = initialValue, isStarted = false, isFinished = false))
                pomodoroAdapter.submitList(timers.toList())
                binding.timerValue.text.clear()
            }
        }
    }

    private fun validateInput(minutes: String): Boolean {
        return if (minutes.isEmpty()) {
            Toast.makeText(this, "The field can not be empty", Toast.LENGTH_SHORT)
                .show()
            false
        } else true
    }

    override fun start(id: Int, initialValue: Long, currentMs: Long) {
        changeTimer(id, initialValue, currentMs, isStarted = true, isFinished = false)
    }

    override fun stop(id: Int, initialValue: Long, currentMs: Long) {
        changeTimer(id, initialValue, currentMs, isStarted = false, isFinished = false)
    }

    override fun finish(id: Int, initialValue: Long, currentMs: Long) {
        changeTimer(id, initialValue, currentMs, isStarted = false, isFinished = true)
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        pomodoroAdapter.submitList(timers.toList())
    }

    private fun toMilliseconds(minutes: String): Long {
        return minutes.toLong()*60*1000
    }

    private fun changeTimer(id: Int, initialValue: Long?, currentMs: Long?, isStarted: Boolean, isFinished: Boolean) {
        val newTimers = mutableListOf<Timer>()
        timers.forEach {
            if (it.id == id) {
                newTimers.add(Timer(it.id, initialValue ?: it.initialValue, currentMs ?: it.currentMs, isStarted, isFinished))
            } else {
                newTimers.add(it)
            }
        }
        pomodoroAdapter.submitList(newTimers)
        timers.clear()
        timers.addAll(newTimers)
    }
}