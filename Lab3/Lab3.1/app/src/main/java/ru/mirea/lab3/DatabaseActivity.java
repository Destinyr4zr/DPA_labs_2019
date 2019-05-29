package ru.mirea.lab3;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static ru.mirea.lab3.MainActivity.mydb;


public class DatabaseActivity extends AppCompatActivity {
    ProgressBar loader;
    ListView listView;
    Activity activity;

    public static final String KEY = "key";
    private static final String FIO = "fio";
    private static final String CREATEDATE = "createdate";

    static ArrayList<HashMap<String, String>> datalist = new ArrayList<>();

    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_view);
        loader = findViewById(R.id.loader);
        listView = findViewById(R.id.listView);
        activity = DatabaseActivity.this;

    }

    @Override
    public void onResume() {
            super.onResume();
            populateData();
    }

    public void populateData() {
        mydb = new DBHelper(activity);
        loader.setVisibility(View.VISIBLE);

        DatabaseActivity.LoadTask loadTask = new DatabaseActivity.LoadTask();
        loadTask.execute();
    }

    class LoadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            datalist.clear();
        }

        protected String doInBackground(String... args) {
            try {
                loadDataList(mydb.getData());
            }
            catch (NullPointerException e)
            {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Table is empty", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent = new Intent(DatabaseActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String string) {

            loadListView(listView);
            loader.setVisibility(View.GONE);
        }
    }

    public void loadDataList(Cursor cursor) {
        HashMap<String, String> hashMap;
        hashMap = new HashMap<>();
        hashMap.put(KEY, "Ключ");
        hashMap.put(FIO, "ФИО");
        hashMap.put(CREATEDATE, "Дата создания");
        datalist.add(hashMap);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                hashMap = new HashMap<>();
                hashMap.put(KEY, cursor.getString(0));
                hashMap.put(FIO, cursor.getString(1));
                hashMap.put(CREATEDATE, Function.Epoch2DateString(cursor.getString(2), "dd.MM.yyyy HH:mm:ss"));
                datalist.add(hashMap);
                cursor.moveToNext();
            }
        }
    }

    public void loadListView(ListView listView) {
        adapter = new SimpleAdapter(this, datalist, R.layout.list_table_item
                , new String[]{KEY, FIO, CREATEDATE},
                new int[]{R.id.keytext, R.id.fiotext, R.id.createdatetext});
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
