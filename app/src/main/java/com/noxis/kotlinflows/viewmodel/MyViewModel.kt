package com.noxis.kotlinflows.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
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

    private fun collectFlow() {

        viewModelScope.launch {
            //Возвращает количество элементов, соответствующих заданному предикату
            val count = countDownFlow.count { it > 3 }
            println("Returns count predicate: $count")

            //Накапливает значение, начиная с первого элемента, и
            // применяет операцию к текущему значению аккумулятора и каждому элементу.
            // Выбрасывает NoSuchElementException, если поток был пуст
            val reduceResult = countDownFlow.reduce { accumulator, value ->
                accumulator + value
            }
            println("Returns Accumulates value: $reduceResult")

            //Накапливает значение, начиная с начального значения и
            // применяя операцию текущее значение аккумулятора и каждого элемента
            val reduceResult2 = countDownFlow.fold(5) { accumulator, value ->
                accumulator + value
            }
            println("Returns Accumulates fold value: $reduceResult2")
        }
    }

    companion object {
        const val COUNT: Int = 10
    }
}