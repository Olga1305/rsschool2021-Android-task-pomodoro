package com.rsschool.pomodoro

data class Timer(
    val id: Int,
    val initialValue: Long,
    var currentMs: Long,
    var isStarted: Boolean,
    var isFinished: Boolean
)
