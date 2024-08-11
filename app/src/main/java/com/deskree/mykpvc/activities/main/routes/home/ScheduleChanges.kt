package com.deskree.mykpvc.activities.main.routes.home

import android.graphics.Bitmap
import android.util.Log
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deskree.mykpvc.activities.main.ML
import com.deskree.mykpvc.activities.main.routes.settings.IS_DARK_THEME
import com.deskree.mykpvc.activities.main.routes.settings.LOGGED_IN_ACCOUNT
import com.deskree.mykpvc.activities.main.routes.settings.MAIN_PREFERENCE_KEY
import com.deskree.mykpvc.image_creator.CreateChangesImage
import com.deskree.mykpvc.requests.changes.checkRep
import com.deskree.mykpvc.requests.changes.getChanges
import com.deskree.mykpvc.utils.SCHE_CHANGES_DIR_NAME
import com.deskree.mykpvc.utils.isNoImage
import com.deskree.mykpvc.utils.readAllImgs
import com.deskree.mykpvc.utils.writeImg
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScheduleChanges() {
    val context = LocalContext.current
    val scheduleChangesPagerState = rememberPagerState()
    val materialColor = MaterialTheme.colorScheme
    val createChangesImage = CreateChangesImage(materialColor.background, materialColor.onSurface)

    val images = remember { mutableStateListOf<Bitmap>() }

    val pref = context.getSharedPreferences(MAIN_PREFERENCE_KEY, ComponentActivity.MODE_PRIVATE)
    val activeAccountLogin = pref.getString(LOGGED_IN_ACCOUNT, "").toString()
    val accountToken = pref.getString(activeAccountLogin, "").toString()
    val isDarkTheme = pref.getBoolean(IS_DARK_THEME, isSystemInDarkTheme())

    LaunchedEffect(Unit) {
        val theme = if (isDarkTheme) "night" else "light"

        checkRep(
            accountToken,
            returnChangesRep = { checkRep ->
                val newTimeStamp = "${checkRep.timeStamp
                    .replace(" ", "_")
                    .replace(":", "-")}-$theme"

                if (isNoImage(context, null, SCHE_CHANGES_DIR_NAME, theme)) {
                    getChanges(
                        accountToken = accountToken,
                        countChangesC = 5,
                        returnChanges = { timeStamp, tableChanges ->
                            var editedTimeStamp = timeStamp
                                .replace(" ", "_")
                                .replace(":", "-")
                            val bitmap = createChangesImage.getBitmap(tableChanges)

                            writeImg(
                                context,
                                bitmap,
                                "$editedTimeStamp-$theme",
                                SCHE_CHANGES_DIR_NAME
                            )
                            images.add(bitmap)
                        },
                        err = {
                            Log.d(ML, "Error getChanges: $it")
                        }
                    )
                } else if (isNoImage(context, newTimeStamp, SCHE_CHANGES_DIR_NAME, theme)) {
                    getChanges(
                        accountToken = accountToken,
                        returnChanges = { timeStamp, tableChanges ->
                            var editedTimeStamp = timeStamp
                                .replace(" ", "_")
                                .replace(":", "-")
                            val newBitmap = createChangesImage.getBitmap(tableChanges)

                            writeImg(
                                context,
                                newBitmap,
                                "$editedTimeStamp-$theme",
                                SCHE_CHANGES_DIR_NAME
                            )

                            readAllImgs(
                                context = context,
                                child = SCHE_CHANGES_DIR_NAME,
                                theme = theme,
                                returnBitmap = { bitmap ->
                                    images.add(bitmap)
                                }
                            )
                        },
                        err = {
                            Log.d(ML, "Error getChanges: $it")
                        }
                    )
                } else {
                    readAllImgs(
                        context = context,
                        child = SCHE_CHANGES_DIR_NAME,
                        theme = theme,
                        returnBitmap = { bitmap ->
                            images.add(bitmap)
                        }
                    )
                }
            },
            err = {
                Log.d(ML, "Error checkRep: $it")
            }
        )
    }

    ScheduleChangesCard(images, scheduleChangesPagerState)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScheduleChangesCard(
    images: SnapshotStateList<Bitmap>,
    scheduleChangesPagerState: PagerState
) {
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
