package com.noxis.kotlinflows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noxis.kotlinflows.ui.theme.KotlinFlowsTheme
import com.noxis.kotlinflows.viewmodel.MyViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Этот блок кода реализует поведение как для Jetpack Compose, но при Xml разметки
         */
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateCount.collectLatest { number ->
//                     binding.textCount.text = number.toString()
                }
            }
        }
        /**
         * Можно реализацию вынести в отдельную extens функцию.
         */
        collectLatestFlow(viewModel.stateCount) { number ->
         //   binding.textCount.text = number.toString()
        }

        setContent {
            KotlinFlowsTheme {
                val viewModel = viewModel<MyViewModel>()
                val time = viewModel.countDownFlow.collectAsState(initial = MyViewModel.COUNT)
                val count = viewModel.stateCount.collectAsState(initial = 0)
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = count.value.toString(),
                            fontSize = 30.sp,
                        )
                        Button(onClick = {
                            viewModel.incrementCount()
                        }) {
                            Text(text = "Inc")
                        }
                    }
                }
            }
        }
    }
}

fun <T> ComponentActivity.collectLatestFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}
