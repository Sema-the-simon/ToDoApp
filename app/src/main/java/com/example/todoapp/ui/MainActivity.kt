package com.example.todoapp.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.data.datastore.PreferencesManager
import com.example.todoapp.data.datastore.UserPreferences
import com.example.todoapp.ui.navigation.AppNavHost
import com.example.todoapp.ui.themes.TodoAppTheme
import com.example.todoapp.utils.isDarkThemeSetUp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main activity of app that sets up the Compose UI with navigation and theming.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val pref by preferencesManager.userPreferences.collectAsStateWithLifecycle(initialValue = UserPreferences())
            val isDarkTheme = isDarkThemeSetUp(pref.themeMode)

            TodoAppTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(navController)
                }
            }
        }
    }
}