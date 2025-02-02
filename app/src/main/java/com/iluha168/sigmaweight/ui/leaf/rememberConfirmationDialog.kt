package com.iluha168.sigmaweight.ui.leaf

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iluha168.sigmaweight.R
import com.iluha168.sigmaweight.ui.container.rememberDialogOpener

@Composable
fun rememberConfirmationDialog(onConfirm: () -> Unit) =
    rememberDialogOpener(R.string.dlg_confirm_title) { close ->
        Text(stringResource(R.string.dlg_confirm_body))
        Row {
            val actions = mutableListOf(
                Pair(onConfirm, R.string.dlg_confirm_yes),
                Pair(close, R.string.dlg_confirm_no)
            )
            actions.shuffle()
            for(( clicked, str ) in actions)
                Button(
                    onClick = clicked,
                    modifier = Modifier.weight(1f).padding(5.dp),
                    content = { Text(stringResource(str)) }
                )
        }
    }