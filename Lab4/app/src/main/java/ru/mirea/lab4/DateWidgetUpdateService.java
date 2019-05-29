package ru.mirea.lab4;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;


public class DateWidgetUpdateService extends Service {
    public DateWidgetUpdateService() {}

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        widgetsync();
        return super.onStartCommand(intent, flags, startId);
    }

    private void widgetsync(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int [] idlist = appWidgetManager.getAppWidgetIds(new ComponentName(this.getApplicationContext().getPackageName(), DateWidget.class.getName()));
        for (int i = 0; i < idlist.length; i++)
            DateWidget.updateWidget(this.getApplicationContext(), appWidgetManager, idlist[i]);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}