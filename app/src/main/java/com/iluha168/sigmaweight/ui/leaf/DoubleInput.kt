package com.iluha168.sigmaweight.ui.leaf

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun DoubleInput(
    label: String,
    enabled: Boolean = true,
    defaultValue: Double? = null,
    additionalCheck: (Double) -> Boolean,
    onSubmit: (Double) -> Unit)
{
    var input by remember { mutableStateOf(
        defaultValue?.toString() ?: ""
    ) }

    val double = input.toDoubleOrNull()
    val pass = double != null && additionalCheck(double)
    val tryOnSubmit = {
        if(pass) {
            onSubmit(double!!)
            input = ""
        }
    }

    TextField(
        label = { Text(label) },
        value = input,
        enabled = enabled,
        maxLines = 2,
        minLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            showKeyboardOnFocus = true,
            imeAction = ImeAction.Done,
            autoCorrectEnabled = false,
        ),
        keyboardActions = KeyboardActions(
            onDone = { tryOnSubmit() }
        ),
        modifier = Modifier.fillMaxWidth(),

        isError = !pass,
        onValueChange = {
            input = it.trim()
            if("\n" in it) tryOnSubmit()
        },
        placeholder = { Text("49.7") }
    )
}