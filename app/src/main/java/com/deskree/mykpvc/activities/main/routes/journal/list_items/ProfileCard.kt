package com.deskree.mykpvc.activities.main.routes.journal.list_items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.deskree.mykpvc.R
import com.deskree.mykpvc.data.TeacherItem
import com.deskree.mykpvc.data.journal.all_journals.Journals
import com.deskree.mykpvc.requests.teachers.TEACHER_AVATAR


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
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.padding(top = 4.dp))
                        Text(
                            text = teacher.fullName,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(Modifier.padding(vertical = 8.dp))
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