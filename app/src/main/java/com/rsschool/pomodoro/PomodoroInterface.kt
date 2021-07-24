package com.rsschool.pomodoro

interface PomodoroInterface {
    fun start(id: Int, initialValue: Long, currentMs: Long)
    fun stop(id: Int, initialValue: Long, currentMs: Long)
    fun finish(id: Int, initialValue: Long, currentMs: Long)
    fun delete(id: Int)
}