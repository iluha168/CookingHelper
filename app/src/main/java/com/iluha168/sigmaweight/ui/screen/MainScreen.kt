package com.iluha168.sigmaweight.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.iluha168.sigmaweight.R

@Composable
fun MainScreen(toScreenWeight: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val w = Modifier.width(IntrinsicSize.Max)
            Button(onClick = toScreenWeight, modifier = w) {
                Text(stringResource(R.string.to_screen_weight))
            }
        }
    }
}

@Preview
@Preview(locale = "ru")
@Composable
private fun MainScreenPreview() {
    MainScreen {  }
}