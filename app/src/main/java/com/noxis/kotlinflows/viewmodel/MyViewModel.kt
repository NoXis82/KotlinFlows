package com.noxis.kotlinflows.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectFlow() {

        viewModelScope.launch {
          val flow1 = (1..5).asFlow()
            //Преобразует элементы, испускаемые исходным потоком, применяя transform,
            // который возвращает другой поток, а затем конкатенирует и сплющивает эти потоки.
            //flatMapMerge
            //flatMapLatest
            flow1.flatMapConcat { value ->
                println("Flow1 value: $value")
                flow {
                    emit(value + 1)
                    delay(500)
                }
            }.collect{
                println("New Flow value: $it")
            }
        }
    }

    companion object {
        const val COUNT: Int = 10
    }
}