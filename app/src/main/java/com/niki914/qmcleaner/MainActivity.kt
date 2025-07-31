package com.niki914.qmcleaner

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.niki914.qmcleaner.ui.theme.XposedTheme
import com.niki914.qmcleaner.viewmodels.MainViewModel
import com.zephyr.log.setOnCaughtListener

class MainActivity : ComponentActivity() {

    private val viewModel by lazy { // 根据需求重实现
        ViewModelProvider.create(this)[MainViewModel::class.java]
    }

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setOnCaughtListener { thread, throwable ->
//            throwable?.stackTraceToString()?.report()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermission(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            XposedTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }
}