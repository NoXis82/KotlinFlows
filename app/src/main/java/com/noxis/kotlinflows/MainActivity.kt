package com.noxis.kotlinflows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noxis.kotlinflows.ui.theme.KotlinFlowsTheme
import com.noxis.kotlinflows.viewmodel.MyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
