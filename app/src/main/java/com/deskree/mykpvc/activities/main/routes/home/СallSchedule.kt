package com.deskree.mykpvc.activities.main.routes.home

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deskree.mykpvc.R
import com.deskree.mykpvc.activities.main.routes.settings.IS_DARK_THEME
import com.deskree.mykpvc.activities.main.routes.settings.MAIN_PREFERENCE_KEY
import com.deskree.mykpvc.utils.getNumWeekday
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CallSchedule() {
    val context = LocalContext.current
    val callSchedulePagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val imagesDark = listOf(R.drawable.mon_tue_wed_fri_dark, R.drawable.thur_dark)
    val imagesLight = listOf(R.drawable.mon_tue_wed_fri_light, R.drawable.thur_light)

    val pref = context.getSharedPreferences(MAIN_PREFERENCE_KEY, ComponentActivity.MODE_PRIVATE)
    val isDarkTheme = pref.getBoolean(IS_DARK_THEME, isSystemInDarkTheme())

    Spacer(Modifier.padding(top = 5.dp))
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

        HorizontalPager(
            count = if (isDarkTheme) imagesDark.size else imagesLight.size,
            state = callSchedulePagerState,
            itemSpacing = 3.dp,
            verticalAlignment = Alignment.Top
        ) { page ->
            Image(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 3.dp),
                painter = painterResource(if (isDarkTheme) imagesDark[page] else imagesLight[page]),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        }
        Spacer(modifier = Modifier.padding(top = 5.dp))

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                if (getNumWeekday() == 3)
                    callSchedulePagerState.animateScrollToPage(1)
            }
        }
    }
}