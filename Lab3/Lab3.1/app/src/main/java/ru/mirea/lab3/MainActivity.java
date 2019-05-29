package ru.mirea.lab3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = new DBHelper(MainActivity.this);
    }

    public void showDatabaseActivity (View v)
    {
        Intent i = new Intent( MainActivity.this, DatabaseActivity.class);
        startActivity(i);
    }

public void create5students (View v)
{
    Date currentDate = new Date();
    DateFormat finalDateAndTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
    mydb.student5roulette(finalDateAndTime.format(currentDate));
}
public void russianvodka (View v)
{
    Date currentDate = new Date();
    DateFormat finalDateAndTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
    mydb.ivanovtransformer(finalDateAndTime.format(currentDate));
}
public void addrandomrow (View v)
{
    Date currentDate = new Date();
    DateFormat finalDateAndTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
    mydb.addrandomstudent(finalDateAndTime.format(currentDate));
}
}
