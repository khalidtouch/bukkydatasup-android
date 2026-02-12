package com.kxtdev.bukkydatasup.common.utils

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {
    private var userSessionTimer: CountDownTimer? = null
    private val mTimerState = MutableStateFlow<TimerState>(TimerState())
    val timerState: StateFlow<TimerState> = mTimerState.asStateFlow()

    init {
        userSessionTimer = object : CountDownTimer(
            Settings.SESSION_IDLE_DELAY,
            Settings.SESSION_TIMER_INTERVAL
        ) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                mTimerState.update { it.copy(isTimedOut = true) }
            }

        }.start()
    }


    fun launch(
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        callback: suspend () -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            callback.invoke()
            resetTimer()
        }
    }

    private fun resetTimer() = viewModelScope.launch {
        userSessionTimer?.cancel()
        mTimerState.update { it.copy(isTimedOut = null) }
        userSessionTimer?.start()
    }

    abstract fun onHandledThrowable()
}

data class TimerState(
    val isTimedOut: Boolean? = null
)