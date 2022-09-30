package com.bjelor.erste.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickersteComposeApp(::onShare)
        }
    }

    private fun onShare(title: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, title)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, title)
        startActivity(shareIntent)
    }
}
