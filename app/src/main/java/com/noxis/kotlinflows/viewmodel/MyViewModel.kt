package com.noxis.kotlinflows.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip

class MyViewModel : ViewModel() {


    private val isAuthentication = MutableStateFlow(false)

    private val flowZip1 = (1..10).asFlow().onEach { delay(1000L) }
    private val flowZip2 = (10..20).asFlow().onEach { delay(300L) }
    private val flowZip3 = (1..5).asFlow().onEach { delay(500L) }
    var numberStr by mutableStateOf("")

    /**
     * Возвращает поток, значения которого сгенерированы
     * с помощью функции transform путем объединения последних выданных значений каждого потока.
     */
    fun combineTest() {
        numberStr = ""
        isAuthentication.combine(flowZip3) { isAuthentication, num ->
            numberStr += "isAuthentication: $isAuthentication, Num: $num\n"
        }.launchIn(viewModelScope)
    }

    fun clear() { numberStr = "" }
    fun auth() {
        isAuthentication.value = !isAuthentication.value
    }

    /**
     * Захватывает значения из текущего потока (this) с другим потоком,
     * используя предоставленную функцию преобразования,
     * применяемую к каждой паре значений. Результирующий поток завершается,
     * как только завершается один из потоков, а на оставшемся потоке вызывается cancel.
     */
    fun zipTest() {
        numberStr = ""
        flowZip1.zip(flowZip2) { num1, num2 ->
            numberStr += "[$num1, $num2]\n"
        }.launchIn(viewModelScope)
    }

    /**
     * Объединяет заданные потоки в один поток без сохранения порядка элементов.
     * Все потоки объединяются одновременно, без ограничения
     * на количество одновременно собираемых потоков.
     */
    fun mergeTest() {
        numberStr = ""
        merge(flowZip1, flowZip2).onEach {
            numberStr += "[$it]\n"
        }.launchIn(viewModelScope)
    }
}