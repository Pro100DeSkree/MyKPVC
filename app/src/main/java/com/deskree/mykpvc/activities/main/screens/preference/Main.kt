package com.deskree.mykpvc.activities.main.screens.preference

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

val cardCornersRadius = 20.dp

// Ідеї для налаштувань:
// Виділяти мою групу поміж інших в змінах

@Composable
fun PreferencesScreen(
    logOutListener: Listener,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
//            items(1) {  //TODO: Закоментовані "налаштування" виклик
//                PrefCardDef()
//            }
//            items(1) {
//                PrefCardProjectSupport()
//            }
            items(1) {
                LogOutOfAccount(logOutListener)
            }
            items(1) {
                PrefCardFeedback()
            }
        }
    }
}

//@Composable  //TODO: Закоментовані "налаштування" функції
//fun PrefCardDef() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 20.dp)
//    ) {
//        Text(
//            text = "Видалити рекламу",
//            style = MaterialTheme.typography.titleLarge
//        )
//        Spacer(Modifier.padding(top = 5.dp))
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(cardCornersRadius)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text="Оформити підписку на premium",
//                        modifier = Modifier.weight(1f)
//                    )
//                    TextButton(
//                        onClick = {
//                            //TODO: Клік кнопки підписатися
//                        },
//                        shape = RoundedCornerShape(cardCornersRadius),
//                        colors = ButtonDefaults.buttonColors(Purple40)
//                    ) {
//                        Text(text = "Підписатися")
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun PrefCardProjectSupport() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 20.dp)
//    ) {
//        Text(
//            text = "Підтримати проєкт",
//            style = MaterialTheme.typography.titleLarge
//        )
//        Spacer(Modifier.padding(top = 5.dp))
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(cardCornersRadius)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//            ) {
//                Text(
//                    text = "Підтримати проект можна одноразовим платижем на карту або щомісячною підпискою на Premium"
//                ) // Цей проєкт існує тільки завдяки вашій фінансовій підтримці.
//                  // Тому якщо хочете, щоб проєкт існував надалі підтримайте його фінансово.
//                  // Підтримати можна як підпискою на Premium, так і одноразовим платежем на карту.
//            }
//            Spacer(Modifier.padding(vertical = 10.dp))
//        }
//    }
//}

@Composable
fun LogOutOfAccount(
    logOutListener: Listener,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Text(
            text = "Вийти",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.padding(top = 5.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(cardCornersRadius)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Вийти з акаунту?")
                    Spacer(Modifier.padding(end = 8.dp))
                    Button(
                        onClick = {
                            logOutListener.logOut()
                        }
                    ) {
                        Text("Log out")
                    }
                }
            }
        }
    }
}

@Composable
fun PrefCardFeedback() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Text(
            text = "Зворотній зв'язок",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.padding(top = 5.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(cardCornersRadius)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                SupportText("Element: ", "@deskree:matrix.org", matrixUrl)
                SupportText("Instagram: ", "@DeSkree.23_12", instUrl)
                SupportText("Telegram: ", "@DeSkree", tgUrl)
            }
            Spacer(Modifier.padding(vertical = 10.dp))
        }
    }
}

fun OpenLinkInBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val chooserIntent = Intent.createChooser(intent, "Відкрити посилання в:")
    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(chooserIntent)
}

@Composable
fun SupportText(
    title: String,
    body: String,
    url: String
) {
    val context = LocalContext.current
    Row {
        Text(
            text = title,
            style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize)
        )
        Text(
            text = body,
            style = TextStyle(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = Color.Cyan
            ),
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            val clipboardManager =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clipData =
                                android.content.ClipData.newPlainText(null, body)
                            clipboardManager.setPrimaryClip(clipData)
                            Toast.makeText(context, "Скопійовано", Toast.LENGTH_SHORT).show()
                        },
                        onTap = {
                            OpenLinkInBrowser(context, url)
                        }
                    )
                }
        )
    }
}

interface Listener {
    fun logOut()
}
