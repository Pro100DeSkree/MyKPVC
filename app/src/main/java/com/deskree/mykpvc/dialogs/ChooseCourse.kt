package com.deskree.mykpvc.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deskree.mykpvc.ui.theme.Purple40
import com.deskree.mykpvc.ui.theme.dialogColor
import kotlin.math.ceil

@Composable
fun ChooseCourseDialog(
    courses: MutableList<String>,
    openChooseCourseDialog: MutableState<Boolean>,
    chooseCourse: (String) -> Unit
) {
    val numbersOfLines = ceil(courses.size.toDouble() / 2).toInt()
    val numbersOfBtnInRow = 2

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
                text = "Оберіть курс",
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
                        TextButton(
                            modifier = Modifier.padding(start = 3.dp, end = 3.dp),
                            onClick = {
                                chooseCourse(courses[i*numbersOfBtnInRow+j])
                                openChooseCourseDialog.value = false
                            },
                            border = BorderStroke(3.dp, Purple40)
                        ) {
                            Text(text = "Я ${courses[i*numbersOfBtnInRow+j]}-й курс")
                        }
                        index++
                        if(index == courses.size){
                            break
                        }
                    }
                }
            }
            Spacer(Modifier.padding(top = 10.dp))
        }
    }
}