package ru.mirea.lab2;

import android.os.AsyncTask;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {
    private LoadTask loadTask;
    private String dataurl = "https://raw.githubusercontent.com/wesleywerner/ancient-tech/02decf875616dd9692b31658d92e64a20d99f816/src/data/techs.ruleset.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTask = new LoadTask(this);
        loadTask.execute(this.dataurl);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadTask.cancel(true);
    }

    private static class LoadTask extends AsyncTask<String, Void, String> {
        final private SoftReference<SplashActivity> listener;
        OkHttpClient client = new OkHttpClient();

        LoadTask(SplashActivity listener) {
            super();
            this.listener = new SoftReference<>(listener);
        }

        @Override
        protected String doInBackground(String... params) {
            Request.Builder builder = new Request.Builder();
            builder.url(String.valueOf(params[0]));
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            JSONHolder jsondb = JSONHolder.getInstance();

            JSONArray json = null;

            try {
                json = new JSONArray(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsondb.setData(json);
            SplashActivity actilistener = listener.get();
            if (actilistener != null)
                actilistener.onDownloadingEND();
        }
    }
        public void onDownloadingEND() {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slidein, R.anim.slideout);
            finish();
        }
    }

