package com.noxis.kotlinflows

import app.cash.turbine.test
import com.noxis.kotlinflows.viewmodel.DispatcherProvider
import com.noxis.kotlinflows.viewmodel.MyViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {

    private lateinit var viewModel: MyViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        testDispatchers = TestDispatchers()
        viewModel = MyViewModel(testDispatchers)
    }

    @Test
    fun `countDownFlow`() = runBlocking {
        viewModel.countDownFlow.test {
            for (i in 10 downTo 0) {
                testDispatchers.testDispatcher.scheduler.apply {
                    advanceTimeBy(1000L); runCurrent()
                }
                val emission = awaitItem()
                println(emission)
                assertEquals(i, emission)
            }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `squareNumber`() = runBlocking {
        val job = launch {
            viewModel.stateShared.test {
                val emission = awaitItem()
                println(emission)
                assertEquals(9, emission)
                cancelAndConsumeRemainingEvents()
            }

        }
        viewModel.squareNumber(3)
        job.join()
        job.cancel()
    }


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}