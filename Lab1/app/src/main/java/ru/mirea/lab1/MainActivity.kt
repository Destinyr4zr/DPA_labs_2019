package ru.mirea.lab1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val webView = findViewById<WebView>(Loadview.id);
        val basicThread = object : Thread() {
            override fun run() {
                try {
                    super.run()
                    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    webView.getSettings().setLoadsImagesAutomatically(true);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadUrl("file:///android_asset/1.gif");
                    Thread.sleep(2000)
                }
                catch (e: Exception) {
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
