package ru.mirea.lab1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.Toast

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val listView = findViewById<ListView>(R.id.listView)
        val adapter = ItemsAdapter(this)
        listView?.adapter = adapter
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Переход к списку", Toast.LENGTH_SHORT).show()
    }
}