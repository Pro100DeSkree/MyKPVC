package com.deskree.mykpvc.activities.main.routes.settings

import android.content.ClipboardManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val cardCornersRadius = 10.dp

// TODO: Ідеї для налаштувань:
// Виділяти мою групу поміж інших в змінах
// Пари для викладача

@Composable
fun SettingsScreen(
    settings: Settings,
) {
    val context = LocalContext.current
    val pref = context.getSharedPreferences(MAIN_PREFERENCE_KEY, MODE_PRIVATE)

    val isDarkTheme = pref.getBoolean(IS_DARK_THEME, isSystemInDarkTheme())
    val isDynamicColors = pref.getBoolean(IS_DYNAMIC_COLORS, true)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 10.dp),
        ) {
            item {  // Вийти з акаунту
                LogOutOfAccount(settings)
            }

            item { // Увімкнути/вимкнути темну тему
                ChangeTheme(isDarkTheme) { isDark ->
                    settings.changeTheme(isDark)
                }
            }

            item { // Увімкнути/вимкнути динамічні кольори
                ChangeDynamicColors(isDynamicColors) { isDynamic ->
                    settings.changeDynamicColors(isDynamic)
                }
            }

//            item {  // Підтримати проект
//                PrefCardProjectSupport()
//            }

            item { // Фідбек
                PrefCardFeedback()
            }
        }
    }
}

@Composable
fun LogOutOfAccount(
    settings: Settings,
) {
    ButtonPlate(
        title = "Вийти",
        signature = "Вихід з акаунту",
        isDescription = false,
        description = "",
        btnText = "Вийти",
        cardCornersRadius = cardCornersRadius
    ) {
        settings.logOut()
    }
}

@Composable
fun ChangeTheme(isDarkTheme: Boolean, onChange: (Boolean) -> Unit) {
    val isChecked = remember { mutableStateOf(isDarkTheme) }

    SwitchPlate(
        title = "Тема",
        signatureOn = "Увімкнути світлу тему",
        signatureOff = "Увімкнути темну тему",
        isDescription = false,
        description = "",
        isChecked = isChecked,
        cardCornersRadius = cardCornersRadius
    ) { isDarkThemeChanged ->
        onChange.invoke(isDarkThemeChanged)
    }
}

@Composable
fun ChangeDynamicColors(isDynamicColors: Boolean, onChange: (Boolean) -> Unit) {
    if (Build.VERSION.SDK_INT >= 31) {
        val isChecked = remember { mutableStateOf(isDynamicColors) }
        SwitchPlate(
            title = "Динамічні кольори",
            signatureOn = "Вимкнути динамічні кольори",
            signatureOff = "Увімкнути динамічні кольори",
            isDescription = true,
            description = "При увімкненому положенні будуть використовуватись три основні кольори вашої системи",
            isChecked = isChecked,
            cardCornersRadius = cardCornersRadius
        ) { isDynamicColorsChanged ->
            onChange.invoke(isDynamicColorsChanged)
        }
    }
}

@Composable
fun PrefCardProjectSupport() {
    ButtonPlate(
        title = "Підтримка проекту",
        signature = "Оформити Premium підписку",
        isDescription = true,
        description = "Також підтримати проект можна одноразовим платижем на карту",
        // Цей проєкт існує тільки завдяки вашій фінансовій підтримці.
        // Тому якщо хочете, щоб проєкт існував надалі підтримайте його фінансово.
        // Підтримати можна як підпискою на Premium, так і одноразовим платежем на карту.
        btnText = "Підписатися",
        cardCornersRadius = cardCornersRadius
    ) {
        //TODO: Клік кнопки підписатися
    }
}

@Composable
fun PrefCardFeedback() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "Зворотній зв'язок",
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                SupportText("Element: ", "@deskree:matrix.org", matrixUrl)
                Spacer(Modifier.padding(vertical = 2.dp))
                SupportText("Instagram: ", "@DeSkree.23_12", instUrl)
                Spacer(Modifier.padding(vertical = 2.dp))
                SupportText("Telegram: ", "@DeSkree", tgUrl)
            }
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

interface Settings {
    fun logOut()
    fun changeTheme(isDark: Boolean)
    fun changeDynamicColors(isDynamic: Boolean)
}
