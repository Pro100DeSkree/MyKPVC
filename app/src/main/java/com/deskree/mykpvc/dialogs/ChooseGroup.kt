package com.deskree.mykpvc.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deskree.mykpvc.ui.theme.Purple40
import com.deskree.mykpvc.ui.theme.dialogColor
import kotlin.math.ceil

@Composable
fun ChooseGroupDialog(
    groups: ArrayList<String>,
    openChooseGroupDialog: MutableState<Boolean>,
    onClickBack: () -> Unit,
    onClickGroup: (String) -> Unit
    ) {
    var numbersOfBtnInRow = 2
    if (groups.size > 6) {
        numbersOfBtnInRow = 3
    }
    val numbersOfLines = ceil(groups.size.toDouble() / numbersOfBtnInRow).toInt()

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = dialogColor
        )
    ) {
        Column(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.padding(top = 10.dp))
            Text(
                text = "Оберіть групу",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            var index = 0
            Spacer(Modifier.padding(top = 10.dp))
            for (i in 0 until numbersOfLines) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (j in 0 until numbersOfBtnInRow) {
                        var btnIndex = i * numbersOfBtnInRow + j
                        if (btnIndex >= groups.size) {
                            btnIndex -= groups.size
                        }
                        TextButton(
                            modifier = Modifier.padding(start = 3.dp, end = 3.dp),
                            onClick = {
                                onClickGroup(groups[btnIndex])
                                openChooseGroupDialog.value = false
                            },
                            border = BorderStroke(3.dp, Purple40)
                        ) {
                            Text(text = groups[btnIndex])
                        }
                        index++
                        if (index >= groups.size) {
                            break
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier.padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    openChooseGroupDialog.value = false
                    onClickBack.invoke()
                },
                colors = ButtonDefaults.filledTonalButtonColors(Color.Transparent),
                contentPadding = PaddingValues(8.dp, 8.dp)
                ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "icon_back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.padding(start = 2.dp))
                    Text(
                        text = "Назад",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        Spacer(Modifier.padding(top = 5.dp))
    }
}

