package com.noxis.kotlinflows.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
            countDownFlow
                .filter { it % 2 == 0 }
                .map { "Map $it" }
                /**
                 * countDownFlow.onEach {
                 *    println("onEach: $it")
                 * }.launchIn(viewModelScope)
                 */
                .onEach {
                    println(it)
                }
                .collect {
                println("Collect count: $it")
            }

        }
    }
    companion object {
        const val COUNT: Int = 10
    }
}