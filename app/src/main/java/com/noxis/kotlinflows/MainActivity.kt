package com.noxis.kotlinflows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noxis.kotlinflows.ui.theme.KotlinFlowsTheme
import com.noxis.kotlinflows.viewmodel.MyViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinFlowsTheme {
                val viewModel = viewModel<MyViewModel>()
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = viewModel.numberStr)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { viewModel.combineTest() }) {
                            Text(text = "Combine")
                        }
                        Button(onClick = { viewModel.auth() }) {
                            Text(text = "Auth")
                        }
                        Button(onClick = { viewModel.clear() }) {
                            Text(text = "Clear")
                        }
                    }
                    Button(onClick = { viewModel.zipTest() }) {
                        Text(text = "Zip")
                    }
                    Button(onClick = { viewModel.mergeTest() }) {
                        Text(text = "Merge")
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
