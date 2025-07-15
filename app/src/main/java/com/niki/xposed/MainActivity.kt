package com.niki.xposed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import com.niki.common.toast
import com.zephyr.log.LogConfig
import com.zephyr.log.LogLevel
import com.zephyr.log.setOnCaughtListener

class MainActivity : ComponentActivity() {

//    private val viewModel by lazy { // 根据需求重实现
//        ViewModelProvider.create(this)[MainViewModel::class.java]
//    }

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialExpressiveTheme {

            }
        }

        LogConfig.edit {
            writeToFile = false
            logLevel = LogLevel.DO_NOT_LOG
        }

        // 全局捕获异常
        setOnCaughtListener { thread, throwable ->
            toast("异常捕获: ${throwable.message}")
        }
    }
}