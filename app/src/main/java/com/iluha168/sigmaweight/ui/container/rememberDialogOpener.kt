package com.iluha168.sigmaweight.ui.container

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun rememberDialogOpener(
    @StringRes title: Int,
    dialog: @Composable ColumnScope.(close: () -> Unit) -> Unit
): () -> Unit {
    var isOpen by remember { mutableStateOf(false) }
    if(isOpen)
        Dialog(
            onDismissRequest = {isOpen = false},
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true,
            )
        ) {
            Card {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(stringResource(title), Modifier.align(Alignment.CenterHorizontally), fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(5.dp))
                    dialog({isOpen = false})
                }
            }
        }
    return {isOpen = true}
}