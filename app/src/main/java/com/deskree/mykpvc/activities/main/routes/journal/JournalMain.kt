package com.deskree.mykpvc.activities.main.routes.journal

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.deskree.mykpvc.R
import com.deskree.mykpvc.activities.main.ML
import com.deskree.mykpvc.activities.main.routes.settings.LOGGED_IN_ACCOUNT
import com.deskree.mykpvc.activities.main.routes.settings.MAIN_PREFERENCE_KEY
import com.deskree.mykpvc.data.TeacherItem
import com.deskree.mykpvc.data.journal.all_journals.Journals
import com.deskree.mykpvc.requests.journal.getAllJournals
import com.deskree.mykpvc.requests.teachers.TEACHER_AVATAR
import com.deskree.mykpvc.requests.teachers.getTeacherInfo

@Composable
fun JournalMain() {
    val context = LocalContext.current
    val pref = context.getSharedPreferences(MAIN_PREFERENCE_KEY, Context.MODE_PRIVATE)
    val activeAccountLogin = pref.getString(LOGGED_IN_ACCOUNT, "").toString()
    val accountToken = pref.getString(activeAccountLogin, "").toString()

//    testInvokeJournal(accountToken)

    var listOfJournals by remember { mutableStateOf(arrayOf<Journals>()) }
    var selectedJournalText by remember { mutableStateOf("Не обрано") }
    var selectedJournal by remember { mutableStateOf<Journals?>(null) }
    var teacher by remember { mutableStateOf<TeacherItem?>(null) }
    var isUpdateTeacherInfo by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        getAllJournals(
            accountToken = accountToken,
            returnJournals = { list ->
                listOfJournals = list
                selectedJournalText = list.firstOrNull()?.subject?.subjectName ?: "Не обрано"
                selectedJournal = list.firstOrNull()
                isUpdateTeacherInfo = true
            },
            err = {}
        )
    }

    if (isUpdateTeacherInfo) {
        LaunchedEffect(Unit) {
            getTeacherInfo(
                accountToken = accountToken,
                teacherId = selectedJournal!!.teacherId,
                returnTeacherInfo = { teacherInfo ->
                    teacher = teacherInfo
                },
                err = {}
            )
            isUpdateTeacherInfo = false
        }
    }

    LazyColumn {
        item {
            JournalSelector(
                listOfJournals,
                selectedJournal = selectedJournalText,
                onJournalSelected = { journal ->
                    selectedJournalText = journal.subject.subjectName
                    selectedJournal = journal
                    isUpdateTeacherInfo = true
                }
            )
        }
        item {
            ProfileCard(
                selectedJournal,
                teacher
            )
        }
        items(30) {

            GradesCard(
                title = "Тести. І. Нечуй-Левицький",
                date = "2023-09-22",
                maxGrade = 12,
                mark = "10"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalSelector(
    listOfJournals: Array<Journals>,
    selectedJournal: String,
    onJournalSelected: (Journals) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(4.dp),
            onClick = { expanded = !expanded },
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
            )
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedJournal,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(),
                    textStyle = TextStyle(textAlign = TextAlign.Center)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.height(400.dp)
                ) {
                    listOfJournals.forEach { journal ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = journal.subject.subjectName,
                                )
                            },
                            onClick = {
                                onJournalSelected(journal)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileCard(
    selectedJournal: Journals?,
    teacher: TeacherItem?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
        )
    ) {
        Row(
            modifier = Modifier
        ) {
            Card(
                modifier = Modifier
                    .width(120.dp)
                    .padding(8.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (selectedJournal != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(TEACHER_AVATAR.format(selectedJournal.teacherId))
                            .crossfade(true)
                            .build(),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_person),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            colorFilter = ColorFilter.tint(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                if (selectedJournal != null && teacher != null) {
                    Column(
                        modifier = Modifier
                    ) {
                        Text(
                            text = selectedJournal.subject.subjectName,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = teacher.fullName,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(Modifier.padding(top = 40.dp))
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
                Column(
                    modifier = Modifier
                ) {
                    Text(
                        text = "Н/А - неатестований",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "Зар - зараховано ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun GradesCard(
    title: String,
    date: String,
    maxGrade: Int,
    mark: String
) {
    Card(
        modifier = Modifier
            .padding(4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f).padding(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Text(
                text = "$mark з $maxGrade",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
