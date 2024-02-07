package com.deskree.mykpvc.activities.main

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.deskree.mykpvc.activities.login.LoginActivity
import com.deskree.mykpvc.activities.main.bottom_nav.BottomItem
import com.deskree.mykpvc.activities.main.screens.preference.LOGGED_IN_ACCOUNT
import com.deskree.mykpvc.activities.main.screens.preference.Listener
import com.deskree.mykpvc.activities.main.screens.preference.MAIN_PREFERENCE_KEY
import com.deskree.mykpvc.data.changes.ScheChanges
import com.deskree.mykpvc.data.changes.TableChanges
import com.deskree.mykpvc.requests.getChanges
import com.deskree.mykpvc.requests.profile.myProfile
import com.deskree.mykpvc.image_creator.CreateChangesImage
import com.deskree.mykpvc.ui.theme.MyKPVCTheme
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    private var accountToken = ""
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var changes: List<TableChanges>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Отримання відповіді від activityLogin
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    recreate()
                } else {
                    finish()
                }
            }

        val pref = getSharedPreferences(MAIN_PREFERENCE_KEY, MODE_PRIVATE)
        // Login
        val activeAccountLogin = pref.getString(LOGGED_IN_ACCOUNT, "").toString()
        accountToken = pref.getString(activeAccountLogin, "").toString()

        // Перевірка токена
        if (accountToken.isEmpty()) {
            startActivityLogin(IS_LOGIN)
        } else {
            myProfile(accountToken, {}) {
                startActivityLogin(IS_LEGACY_TOKEN)
            }
        }

        val createChangesImage = CreateChangesImage()
        getChanges(accountToken, 5) { changesList ->
            changes = changesList
            changesList.forEachIndexed() { index, oneDayListChanges ->
                val bitmap = createChangesImage.getBitmap(oneDayListChanges)
                writeImg(this, bitmap, index.toString())
            }
        }
//
//        // Запуск python
//        if (!Python.isStarted()) {
//            Python.start(AndroidPlatform(this))
//        }

        val tableO = TableChanges(
            onDay = "На «05»  лютого 2024 р., (понеділок, знаменник)",
            oneRowChanges = mutableListOf("Хтось там йде на практику1", "Хтось там виходе з практики2"),
            changes = mutableListOf(
                ScheChanges(groupNum="241", lesNum="1", lesToSchedule="Додатково", lesToChanges="Аблова (статистика)", classNum=""),
                ScheChanges(groupNum="241", lesNum="4", lesToSchedule="Чебукін", lesToChanges="Відсутня", classNum=""),
                ScheChanges(groupNum="382", lesNum="1", lesToSchedule="Додатково", lesToChanges="Філонюк", classNum=""),
                ScheChanges(groupNum="131", lesNum="2", lesToSchedule="Куцак", lesToChanges="Семакова", classNum=""),
                ScheChanges(groupNum="131", lesNum="3", lesToSchedule="Семакова", lesToChanges="Коцегубов", classNum="")
            )
        )

        setContent {
            val navController = rememberNavController()
            val logOutListener = object : Listener {
                override fun logOut() {
                    pref.edit().putString(LOGGED_IN_ACCOUNT, "").apply()
                    navController.navigate(BottomItem.Home.route)
                    startActivityLogin(IS_LOGIN)
                }
            }

//            val createChangesImage = CreateChangesImage()
//            val bitmap = remember { mutableStateOf(createChangesImage.getBitmap(tableO)) }
//            getChanges(accountToken, 1) { changesList ->
//                changes = changesList
//                changesList.forEach { oneDayListChanges ->
//                    bitmap.value = createChangesImage.getBitmap(oneDayListChanges)
//                }
//            }

            MyKPVCTheme(darkTheme = true) {
//                MainScreen(logOutListener, navController)
//            }
                Surface(
                    Modifier
                        .fillMaxSize()
                ) {
//                    Image(
//                        bitmap = bitmap.value.asImageBitmap(),
//                        contentDescription = null
//                    )
                }
            }
        }
    }

    private fun startActivityLogin(target: Int) {
        val myIntent = Intent(this, LoginActivity::class.java)
            .putExtra("target", target)
        launcher.launch(myIntent)
    }

    companion object {
        const val IS_LOGIN = 100
        const val IS_LEGACY_TOKEN = 101
    }
}
fun writeImg(context: Context, bitmap: Bitmap, name: String) {

    // Отримуємо шлях до зовнішньої теки
    val directory = File(context.getExternalFilesDir(null), "img")
    // Перевіряємо наявність теки та створюємо, якщо не існує
    if (!directory.exists()) {
        directory.mkdirs()
    }

    // Створити файл у директорії з ім'ям "дата.png"
    val file = File("$directory", "$name.png")

    val stream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    stream.close()

}
