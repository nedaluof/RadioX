package com.nedaluof.radiox.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import com.nedaluof.radiox.ui.main.radio_stations.RadioStationsScreen
import com.nedaluof.radiox.ui.theme.RadioXTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      RadioXTheme {
        with(this.window){
          statusBarColor = MaterialTheme.colorScheme.secondary.toArgb()
          navigationBarColor = MaterialTheme.colorScheme.secondary.toArgb()
        }
        RadioStationsScreen()
      }
    }
  }
}