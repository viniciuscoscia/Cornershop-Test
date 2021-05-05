package com.cornershop.counterstest.presentation.ui.welcome

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.counterstest.R
import com.cornershop.counterstest.presentation.ui.main.MainActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        findViewById<Button>(R.id.buttonStart).setOnClickListener {
            startActivity(MainActivity.newIntent(this))
        }
    }
}