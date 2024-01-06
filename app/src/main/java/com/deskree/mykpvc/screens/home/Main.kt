package com.deskree.mykpvc.screens.home

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deskree.mykpvc.screens.preference.ChooseGroup
import com.deskree.mykpvc.screens.preference.MAIN_PREFERENCE_KEY
import com.deskree.mykpvc.screens.preference.PREF_GROUP_NUM_KEY
import com.deskree.mykpvc.utils.getNumWeekday
import com.deskree.mykpvc.utils.readImgCollection
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val context = LocalContext.current

        val scheduleBitmapList = remember { mutableStateListOf<Bitmap>() }
        val coroutineScope = rememberCoroutineScope()
        val schedulePagerState = rememberPagerState()
        val update = remember { mutableStateOf(false) }

        val pref = context.getSharedPreferences(MAIN_PREFERENCE_KEY, ComponentActivity.MODE_PRIVATE)
        if (scheduleBitmapList.isEmpty()) {
            UpdateScheduleBitmapList(context, scheduleBitmapList, pref){
                update.value = true
            }
        } else if(scheduleBitmapList.isNotEmpty()){
            update.value = true
        }

        if(update.value && scheduleBitmapList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(1) {
                    ScheduleChanges()
                }
                if (scheduleBitmapList.isNotEmpty()) {
                    items(1) {
                        ScheduleGroup(scheduleBitmapList, schedulePagerState, coroutineScope)
                    }
                }
            }
        }else{
            update.value = false
            UpdateScheduleBitmapList(context, scheduleBitmapList, pref){
                update.value = true
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScheduleChanges() {
    val scheduleChangesBitmapList = readImgCollection(LocalContext.current, "changes").reversed()
    val scheduleChangesPagerState = rememberPagerState()

    Spacer(Modifier.padding(top = 5.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Spacer(modifier = Modifier.padding(top = 5.dp))
        Text(
            modifier = Modifier.padding(horizontal = 15.dp),
            text = "Зміни в розкладі"
        )
        Spacer(modifier = Modifier.padding(top = 5.dp))
        HorizontalPager(
            count = scheduleChangesBitmapList.size,
            state = scheduleChangesPagerState,
            itemSpacing = 3.dp,
            verticalAlignment = Alignment.Top
        ) { page ->
            Image(
                modifier = Modifier.fillMaxWidth().padding(3.dp),
                painter = BitmapPainter(scheduleChangesBitmapList[page].asImageBitmap()),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScheduleGroup(
    scheduleBitmapList: MutableList<Bitmap>,
    schedulePagerState: PagerState,
    coroutineScope: CoroutineScope
) {
    Spacer(Modifier.padding(top = 5.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Spacer(modifier = Modifier.padding(top = 5.dp))
        Text(
            modifier = Modifier.padding(horizontal = 15.dp),
            text = "Розклад"
        )
        Spacer(modifier = Modifier.padding(top = 5.dp))
        HorizontalPager(
            count = scheduleBitmapList.size,
            state = schedulePagerState,
            itemSpacing = 3.dp,
            verticalAlignment = Alignment.Top
        ) { page ->
            Image(
                modifier = Modifier.fillMaxWidth().padding(3.dp),
                painter = BitmapPainter(scheduleBitmapList[page].asImageBitmap()),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        }
    }
    LaunchedEffect(true) {
        // Скрол до теперішнього дня тижня
        coroutineScope.launch {
            var numWeekday = getNumWeekday()
            if (numWeekday in 5..6) numWeekday = 0
            schedulePagerState.scrollToPage(numWeekday)
        }
    }
}

@Composable
fun UpdateScheduleBitmapList(
    context: Context,
    scheduleBitmapList: MutableList<Bitmap>,
    pref: SharedPreferences,
    isLoadedBitmapList: () -> Unit
) {
    val groupNum = pref.getString(PREF_GROUP_NUM_KEY, "-1")!!
    if (groupNum != "-1") {
        scheduleBitmapList.addAll(
            readImgCollection(
                context,
                groupNum
            )
        )
        isLoadedBitmapList.invoke()
    } else {
        ChooseGroup(pref){ lGroupNum ->
            scheduleBitmapList.addAll(
                readImgCollection(
                    context,
                    lGroupNum
                )
            )
            isLoadedBitmapList.invoke()
        }
    }
}
