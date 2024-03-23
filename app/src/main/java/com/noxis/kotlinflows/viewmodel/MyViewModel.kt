package com.noxis.kotlinflows.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MyViewModel(
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    val countDownFlow = flow<Int> {
        var currentCount = COUNT
        emit(COUNT)
        while (currentCount > 0) {
            delay(1000L)
            currentCount--
            emit(currentCount)
        }
    }.flowOn(dispatchers.main)

    private val _stateCount = MutableStateFlow(0)
    val stateCount = _stateCount.asStateFlow()

    private val _stateShared = MutableSharedFlow<Int>(replay = 5)
    val stateShared = _stateShared.asSharedFlow()

    init {
      //  collectFlow()
        squareNumber(3)
        viewModelScope.launch(dispatchers.main) {
            stateShared.collect {
                delay(2000L)
                println("FIRST FLOW: The receive number: $it")
            }
        }
        viewModelScope.launch(dispatchers.main) {
            stateShared.collect {
                delay(3000L)
                println("SECOND FLOW: The receive number: $it")
            }
        }
        //Только когда есть подписка будет осуществлен выброс emit
      //  squareNumber(5)
        //Можно добавить значение replay для sharedFlow
        //тогда выбросу будут кэшироваться и отдаваться последним подписчикам
    }

    fun squareNumber(number: Int) {
        viewModelScope.launch(dispatchers.main) {
            _stateShared.emit(number * number)
        }
    }

    fun incrementCount() {
        _stateCount.value += 1
    }


    private fun collectFlow() {
        viewModelScope.launch {
            val flow = flow {
                delay(250L)
                emit("Закуска")
                delay(1000L)
                emit("Основное блюдо")
                delay(100L)
                emit("Десерт")
            }

            //buffer, conflate, collectLatest
            println("Collect")
            flow.onEach {
                println("Доставлена: $it")
            }
                .collect {
                    println("Сейчас едим: $it")
                    delay(1500)
                    println("Закончили с: $it")
                }
            /**
             * Результат, все emit доставлены и обработаны:
             * Доставлена: Закуска
             * Сейчас едим: Закуска
             * Закончили с: Закуска
             * Доставлена: Основное блюдо
             * Сейчас едим: Основное блюдо
             * Закончили с: Основное блюдо
             * Доставлена: Десерт
             * Сейчас едим: Десерт
             * Закончили с: Десерт
             */

            println("Buffer")
            flow.onEach {
                println("Доставлена: $it")
            }
                //Оператор буфера создает во время выполнения отдельный
                // coroutine для потока, к которому он применяется.
                .buffer()
                .collect {
                    println("Сейчас едим: $it")
                    delay(1500)
                    println("Закончили с: $it")
                }
            /**
             * Результат с buffer:
             * Закончили с: Десерт
             * Доставлена: Закуска
             * Сейчас едим: Закуска
             * Доставлена: Основное блюдо
             * Доставлена: Десерт
             * Закончили с: Закуска
             * Сейчас едим: Основное блюдо
             * Закончили с: Основное блюдо
             * Сейчас едим: Десерт
             * Закончили с: Десерт
             */

            println("Conflate")
            flow.onEach {
                println("Доставлена: $it")
            }
                //Конфликтует выбросы потока по скомбинированному каналу и
                // запускает коллектор в отдельном корутине. В результате эмиттер никогда
                // не приостанавливается из-за медленной работы коллектора,
                // но коллектор всегда получает самое последнее выданное значение.
                .conflate()
                .collect {
                    println("Сейчас едим: $it")
                    delay(1500)
                    println("Закончили с: $it")
                }
            /**
             * Результат с conflate:
             * Доставлена: Закуска
             * Сейчас едим: Закуска
             * Доставлена: Основное блюдо
             * Доставлена: Десерт
             * Закончили с: Закуска
             * Сейчас едим: Десерт
             * Закончили с: Десерт
             */

            println("collectLatest")
            flow.onEach {
                println("Доставлена: $it")
            }
                //Терминальный оператор потока, который собирает заданный поток с
                // заданным действием. Принципиальное отличие от collect заключается в том,
                // что когда исходный поток выдает новое значение, блок действий
                // для предыдущего значения отменяется.
                .collectLatest {
                    println("Сейчас едим: $it")
                    delay(1500)
                    println("Закончили с: $it")
                }
            /**
             * Результат с collectLatest:
             * Доставлена: Закуска
             * Сейчас едим: Закуска
             * Доставлена: Основное блюдо
             * Сейчас едим: Основное блюдо
             * Доставлена: Десерт
             * Сейчас едим: Десерт
             * Закончили с: Десерт
             */

        }
    }

    companion object {
        const val COUNT: Int = 10
    }
}