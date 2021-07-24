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
                val currentMs = toMilliseconds(minutes)
                timers.add(Timer(nextId++, currentMs, false))
                pomodoroAdapter.submitList(timers.toList())
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

    override fun start(id: Int, currentMs: Long) {
        changeTimer(id, currentMs, true)
    }

    override fun stop(id: Int, currentMs: Long) {
        changeTimer(id, currentMs, false)
    }

    override fun reset(id: Int) {
        changeTimer(id, 0L, false)
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        pomodoroAdapter.submitList(timers.toList())
    }

    private fun toMilliseconds(minutes: String): Long {
        return minutes.toLong()*60*1000
    }

    private fun changeTimer(id: Int, currentMs: Long?, isStarted: Boolean) {
        val newTimers = mutableListOf<Timer>()
        timers.forEach {
            if (it.id == id) {
                newTimers.add(Timer(it.id, currentMs ?: it.currentMs, isStarted))
            } else {
                newTimers.add(it)
            }
        }
        pomodoroAdapter.submitList(newTimers)
        timers.clear()
        timers.addAll(newTimers)
    }
}