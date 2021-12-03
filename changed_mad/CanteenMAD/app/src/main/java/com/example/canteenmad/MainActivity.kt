package com.example.canteenmad

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var bg: ImageView
    lateinit var name:TextView
    lateinit var lottieAnimationView: LottieAnimationView
    lateinit var timer: Timer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bg = findViewById(R.id.back)
        name = findViewById(R.id.textView)
        lottieAnimationView = findViewById(R.id.lottieAnimationView)
        timer = Timer()
        name.animate().translationY(2000f).setDuration(1000).startDelay = 4000
        bg.animate().translationY(-2800f).setDuration(1000).startDelay = 4000
        lottieAnimationView.animate().translationY(2000f).setDuration(1000).startDelay = 4000
        timer.schedule(object : TimerTask() {
            override fun run() {
                val i = Intent(this@MainActivity, Login::class.java)
                startActivity(i)
                finish()
            }
        }, 5000)
    }
}