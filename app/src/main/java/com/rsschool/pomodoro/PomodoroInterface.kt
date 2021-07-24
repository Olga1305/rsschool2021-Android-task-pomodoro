package com.rsschool.pomodoro

interface PomodoroInterface {
    fun start(id: Int, currentMs: Long)
    fun stop(id: Int, currentMs: Long)
    fun reset(id: Int)
    fun delete(id: Int)
}