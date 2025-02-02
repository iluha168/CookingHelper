package com.iluha168.sigmaweight.ui.leaf

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.iluha168.sigmaweight.R
import com.iluha168.sigmaweight.ui.container.AnimatedBlinkColumn
import com.iluha168.sigmaweight.ui.container.rememberDialogOpener

data class WeightedItemData (
    val weight: Double
)

@Composable
fun WeightedItem(
    modifier: Modifier = Modifier,
    id: Int,
    data: WeightedItemData? = null,
    shouldBlink: Boolean,
    onDeleteRequest: (key: Int) -> Unit,
    color: Color,
    icon: Painter
) {
    val dlgInfo = rememberDialogOpener(R.string.info) { close ->
        if(data == null) return@rememberDialogOpener
        Text(stringResource(R.string.weighted_item)+" "+stringResource(R.string.id, id))
        Text(stringResource(R.string.weighted_item_weight, data.weight), fontSize = 20.sp)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                onDeleteRequest(id)
                close()
            }) {
                Icon(Icons.Default.Delete, stringResource(R.string.delete), tint = MaterialTheme.colorScheme.onError)
            }
        }
    }

    AnimatedBlinkColumn (
        enabled = shouldBlink,
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box (modifier
            .clickable(
                enabled = data != null,
                onClick = { dlgInfo() }
            )
            .background(color)
            .fillMaxSize()
        ) {
            Icon(
                icon, stringResource(R.string.weighted_item),
                modifier = Modifier.align(Alignment.Center)
            )
            if(data != null) {
                Text(
                    text = stringResource(R.string.id, id),
                    fontSize = 8.sp,
                    maxLines = 1,
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Text(
                    text = data.weight.toInt().toString(),
                    fontSize = 10.sp,
                    maxLines = 1,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}