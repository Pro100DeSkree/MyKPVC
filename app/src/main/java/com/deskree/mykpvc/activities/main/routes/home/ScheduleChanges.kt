package com.deskree.mykpvc.activities.main.routes.home

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deskree.mykpvc.activities.main.routes.settings.IS_DARK_THEME
import com.deskree.mykpvc.activities.main.routes.settings.LOGGED_IN_ACCOUNT
import com.deskree.mykpvc.activities.main.routes.settings.MAIN_PREFERENCE_KEY
import com.deskree.mykpvc.image_creator.CreateChangesImage
import com.deskree.mykpvc.requests.changes.getChanges
import com.deskree.mykpvc.utils.writeImg
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScheduleChanges() {
    val context = LocalContext.current
    val countScheduleChanges = 5
    val scheduleChangesPagerState = rememberPagerState()
    val materialColor = MaterialTheme.colorScheme
    val createChangesImage = CreateChangesImage(materialColor.background, materialColor.onSurface)

    val images = remember { mutableStateListOf<Bitmap>() }

    val pref = context.getSharedPreferences(MAIN_PREFERENCE_KEY, ComponentActivity.MODE_PRIVATE)
    val activeAccountLogin = pref.getString(LOGGED_IN_ACCOUNT, "").toString()
    val accountToken = pref.getString(activeAccountLogin, "").toString()
    val isDarkTheme = pref.getBoolean(IS_DARK_THEME, isSystemInDarkTheme())

    LaunchedEffect(Unit) {
        val endingName = if (isDarkTheme) "night" else "light"
        getChanges(
            accountToken,
            countScheduleChanges,
            returnChanges = { changesList ->
                changesList.forEachIndexed() { index, oneDayListChanges ->
                    val bitmap = createChangesImage.getBitmap(oneDayListChanges)
                    writeImg(context, bitmap, "$index-$endingName", "sche_changes")
                    images.add(bitmap)
                }
            },
            err = {}
        )
    }

    Spacer(Modifier.padding(top = 5.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Spacer(modifier = Modifier.padding(top = 5.dp))
        Text(
            modifier = Modifier.padding(horizontal = 15.dp),
            text = "Зміни в розкладі"
        )
        Spacer(modifier = Modifier.padding(top = 5.dp))
        if (images.isNotEmpty()) {
            HorizontalPager(
                count = images.size,
                state = scheduleChangesPagerState,
                itemSpacing = 3.dp,
                verticalAlignment = Alignment.Top
            ) { page ->
                Image(
                    modifier = Modifier.fillMaxWidth().padding(3.dp),
                    painter = BitmapPainter(images[page].asImageBitmap()),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 3.dp
                )
            }
            Spacer(modifier = Modifier.padding(top = 10.dp))
        }
    }
}