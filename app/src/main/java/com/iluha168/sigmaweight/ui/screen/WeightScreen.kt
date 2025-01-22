package com.iluha168.sigmaweight.ui.screen

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iluha168.sigmaweight.R
import com.iluha168.sigmaweight.logic.combinationsAll
import com.iluha168.sigmaweight.ui.container.FABContainer
import com.iluha168.sigmaweight.ui.container.rememberDialogOpener
import com.iluha168.sigmaweight.ui.leaf.DoubleInput
import com.iluha168.sigmaweight.ui.leaf.WeightedItem
import com.iluha168.sigmaweight.ui.leaf.WeightedItemData
import com.iluha168.sigmaweight.ui.leaf.rememberConfirmationDialog
import kotlin.math.absoluteValue
import kotlin.math.roundToLong

const val GRID_SIZE = 8

private enum class FocusOn {
    ITEM_GRID,
    BUTTON,
    WEIGHT_INPUT
}

private enum class Step(
    val focusOn: FocusOn,
    @StringRes val buttonText: Int,
    val buttonAsLabel: Boolean
) {
    MEASURE_NEW(FocusOn.WEIGHT_INPUT, R.string.new_measurement_add, true),
    MEASUREMENT_STORE(FocusOn.ITEM_GRID, R.string.new_measurement_place, false)
}

private class WeightScreenStateManager {
    var step by mutableStateOf(Step.MEASURE_NEW)

    var targetWeight by mutableDoubleStateOf(200.0)
    var maxTargetWeightError by mutableDoubleStateOf(2.0)
    val targetWeightRange: String
        get() = "${targetWeight-maxTargetWeightError}-${targetWeight+maxTargetWeightError}"

    private var lastItemId = 0
    val items = mutableStateMapOf<Int, WeightedItemData>()
    val itemPlacements = IntArray(GRID_SIZE*GRID_SIZE)

    init {
        itemPlacements.fill(-1)
    }

    fun deleteItem(key: Int) {
        items.remove(key)
        itemPlacements[itemPlacements.indexOfFirst { it == key }] = -1
    }

    fun addItem(data: WeightedItemData) {
        items[++lastItemId] = data
        itemPlacements[itemPlacements.indexOfFirst { it < 0 }] = lastItemId
    }

    fun isLastItem(id: Int) = step == Step.MEASUREMENT_STORE && lastItemId == id
}

@Preview
@Preview(locale = "ru")
@Composable
fun WeightScreen() {
    BackHandler {  }
    val state = remember { WeightScreenStateManager() }

    val (bestItemCombo, bestItemComboWeight, bestItemComboError) = state.items
        .asIterable()
        .combinationsAll()
        .map { Pair(it, it.sumOf { entry -> entry.value.weight }) }
        .map { Triple(it.first, it.second, (it.second - state.targetWeight).absoluteValue) }
        .minByOrNull { it.third }!! // There is always at least 1 element: the empty combination
    val bestItemComboIds = bestItemCombo.map { it.key }.toIntArray()
    val isErrorInRange = bestItemComboError <= state.maxTargetWeightError

    val dlgCombinations = rememberDialogOpener(R.string.weighted_item_combination_dlg_title) { close ->
        DoubleInput(
            label = stringResource(R.string.target_weight),
            defaultValue = state.targetWeight,
            additionalCheck = { it > 0.0 },
            onSubmit = { state.targetWeight = it }
        )
        DoubleInput(
            label = stringResource(R.string.target_weight_error),
            defaultValue = state.maxTargetWeightError,
            additionalCheck = { it > 0.0 },
            onSubmit = { state.maxTargetWeightError = it }
        )
        Text(state.targetWeightRange, fontSize = 10.sp)
        Spacer(modifier = Modifier.height(20.dp))
        val bestCookieComboErrorPercent = (10000.0*bestItemComboError/state.targetWeight).roundToLong() / 100.0
        Text(
            stringResource(R.string.best_item_combo_stats, bestItemComboWeight, bestItemComboError, bestCookieComboErrorPercent),
            fontSize = 12.sp
        )
        Button(
            enabled = isErrorInRange,
            modifier = Modifier.fillMaxWidth(),
            onClick = rememberConfirmationDialog {
                bestItemComboIds.forEach(state::deleteItem)
                close()
            }
        ) {
            Text(stringResource(R.string.best_item_combo_remove))
        }
    }

    Column {
        Column (
            modifier = Modifier.weight(1f).fillMaxWidth(),
        ) {
            val imgAdd = rememberVectorPainter(Icons.Default.Add)
            val imgCookie = painterResource(R.drawable.cookie)
            val imgEmpty = painterResource(R.drawable.dot)

            for(y in 0..<GRID_SIZE)
                Row(
                    modifier = Modifier.weight(1f).height(IntrinsicSize.Max),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for(x in 0..<GRID_SIZE) {
                        val itemId = state.itemPlacements[x+y*GRID_SIZE]
                        val item = state.items[itemId]
                        val isLastInsertedItem = state.isLastItem(itemId)
                        WeightedItem(
                            modifier = Modifier.weight(1f).fillMaxHeight().padding(1.dp),
                            onDeleteRequest = state::deleteItem,
                            data = if(item == null) null else Pair(itemId, item),
                            shouldBlink = isLastInsertedItem,
                            color = when {
                                itemId in bestItemComboIds -> if(isErrorInRange) Color.Green else MaterialTheme.colorScheme.surfaceVariant
                                else -> MaterialTheme.colorScheme.surface
                            },
                            icon = when {
                                isLastInsertedItem -> imgAdd
                                itemId > 0 -> imgCookie
                                else -> imgEmpty
                            }
                        )
                    }
                }
        }

        Button(
            modifier = Modifier.weight(if(state.step.focusOn == FocusOn.BUTTON) 0.5f else 0.2f).fillMaxWidth(),
            enabled = !state.step.buttonAsLabel,
            onClick = when (state.step) {
                Step.MEASUREMENT_STORE -> {-> state.step = Step.MEASURE_NEW }
                else -> {->}
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
            shape = RectangleShape,
        ) {
            Text(stringResource(state.step.buttonText))
        }

        val isWeightInputOn = state.step.focusOn == FocusOn.WEIGHT_INPUT
        AnimatedVisibility(isWeightInputOn) {
            DoubleInput(stringResource(R.string.input_weight), isWeightInputOn, additionalCheck = {true}) { weight ->
                state.addItem(WeightedItemData(weight))
                state.step = Step.MEASUREMENT_STORE
            }
        }
    }

    FABContainer {
        FloatingActionButton(onClick = dlgCombinations) {
            Icon(Icons.Default.Star, stringResource(R.string.weighted_item_combination_dlg_title))
        }
    }
}
