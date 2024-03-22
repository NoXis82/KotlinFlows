package com.noxis.kotlinflows.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    val countDownFlow = flow<Int> {
        var currentCount = COUNT
        emit(COUNT)
        while (currentCount > 0) {
            delay(1000L)
            currentCount--
            emit(currentCount)
        }
    }

    init {
        collectFlow()
    }
    private fun collectFlow() {
        viewModelScope.launch {
         //   countDownFlow.collect {
        //        println("Collect count: $it")
        //    }
            countDownFlow.collectLatest {
                delay(1500L)
                println("Collect latest count: $it")
            }
        }
    }
    companion object {
        const val COUNT: Int = 10
    }
}