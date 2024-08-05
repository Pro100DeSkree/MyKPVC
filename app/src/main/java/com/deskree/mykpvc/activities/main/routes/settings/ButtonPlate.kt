package com.deskree.mykpvc.activities.main.routes.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonPlate(
    title: String,
    signature: String,
    isDescription: Boolean,
    description: String,
    btnText: String,
    cardCornersRadius: Dp,
    onClick: () -> Unit
) {

    Column {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = title,
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(cardCornersRadius),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = signature,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                    if (isDescription) {
                        Spacer(modifier = Modifier.padding(bottom = 2.dp))
                        Text(
                            text = description,
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Thin),
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Button(
                    onClick = {
                        onClick.invoke()
                    },
                    shape = RoundedCornerShape(cardCornersRadius)
                ){
                    Text(text = btnText)
                }
            }
        }
        Spacer(modifier = Modifier.padding(2.dp))
    }
}