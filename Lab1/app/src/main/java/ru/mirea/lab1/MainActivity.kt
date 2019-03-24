package ru.mirea.lab1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val basicThread = object : Thread() {
            override fun run() {
                try {
                    super.run()
                    val webView = findViewById<WebView>(Loadview.id)
                    webView.post(Runnable { fun run (){webView.loadUrl("file:///android_asset/ic_loadingscreen.gif")} })
                    webView.postDelayed(this, 1000)
                    Thread.sleep(2000)
                }
                finally {
                    startActivity(Intent(this@MainActivity, SecondActivity::class.java))
                    finish()
                }
            }
        }
        basicThread.start()
    }
}
