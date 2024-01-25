package com.deskree.mykpvc.activities.main.screens.callSchedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deskree.mykpvc.R
import com.google.accompanist.pager.HorizontalPager

@Composable
fun CallScheduleScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Spacer(modifier = Modifier.padding(top = 5.dp))
            Text(
                modifier = Modifier.padding(horizontal = 15.dp),
                text = "Розклад дзвінків"
            )
            Spacer(modifier = Modifier.padding(top = 5.dp))
            Image(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 3.dp),
                painter = painterResource(R.drawable.mon_tue_wed_fri),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.padding(top = 1.dp))
            Image(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 3.dp),
                painter = painterResource(R.drawable.thur),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.padding(top = 5.dp))
        }
    }
}