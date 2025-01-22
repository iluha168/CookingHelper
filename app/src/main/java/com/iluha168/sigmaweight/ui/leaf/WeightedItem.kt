package com.iluha168.sigmaweight.ui.leaf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
    data: Pair<Int, WeightedItemData>? = null,
    shouldBlink: Boolean,
    onDeleteRequest: (key: Int) -> Unit,
    color: Color,
    icon: Painter
) {
    val dlgInfo = rememberDialogOpener(R.string.info) { close ->
        if(data == null) return@rememberDialogOpener
        Text(stringResource(R.string.weighted_item)+" "+stringResource(R.string.id, data.first))
        Text(stringResource(R.string.weighted_item_weight, data.second.weight), fontSize = 20.sp)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = rememberConfirmationDialog {
                onDeleteRequest(data.first)
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
        Surface (
            modifier = modifier.fillMaxSize(),
            onClick = if(data == null) {->} else dlgInfo,
            color = color
        ) {
            if(data != null)
                Text(
                    text = stringResource(R.string.id, data.first),
                    fontSize = 8.sp,
                    maxLines = 1,
                )
            Icon(
                icon,
                stringResource(R.string.weighted_item),
                Modifier.fillMaxSize()
            )
        }
    }
}