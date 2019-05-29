package ru.mirea.lab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class DateWidget extends AppWidgetProvider {
    static String curDate = "";
    static int year, month, dayOfMonth, started;
    static long days;
    private Timer ticker = new Timer();

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int id : appWidgetIds) {
            startTimer(context, appWidgetManager, appWidgetIds);
            updateWidget(context, appWidgetManager, id);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        DBHelper dbHelper = new DBHelper(context);
        ticker.cancel();
        SharedPreferences.Editor editor = context.getSharedPreferences(
                CalendarActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();
        for (int widgetID : appWidgetIds) {
            dbHelper.deleteDate(widgetID);
            editor.remove(CalendarActivity.WIDGET_TEXT + widgetID);
            editor.remove(CalendarActivity.WIDGET_COLOR + widgetID);
        }
        editor.apply();
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        context.deleteDatabase("DateDB");
        ticker.cancel();
    }

    private void startTimer(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        ticker.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < appWidgetIds.length; i++)
                    updateWidget(context, appWidgetManager, appWidgetIds[i]);
            }
        }, 0, 30000);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID) {
        SharedPreferences sp = context.getSharedPreferences(CalendarActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        String remainingdays = sp.getString( CalendarActivity.WIDGET_TEXT + widgetID, null);
        if (remainingdays == null) return;
        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
        loadDate(context, widgetID);
        long days = getDaysDiff(context, dayOfMonth, month, year);

        SharedPreferences.Editor editor = sp.edit();

        if (days == -2){
            editor.putString(CalendarActivity.WIDGET_TEXT + widgetID,String.valueOf(0)).apply();
            widgetView.setTextViewText(R.id.textView, "До наступления времени осталось менее часа");
        }
        else
        if (days == -1){
            widgetView.setTextViewText(R.id.textView, "Время пришло");
            if (started == 0) {
                notificationEventStarted(context, widgetID);
                started = 1;
                DBHelper dbHelper = new DBHelper(context);
                dbHelper.setStarted(started, widgetID);
            }
        }else
        if (days == 0){
            editor.putString(CalendarActivity.WIDGET_TEXT + widgetID,String.valueOf(0)).apply();
            widgetView.setTextViewText(R.id.textView, "Скоро вы всё узнаете");
        }else
        if (days > 0){
            editor.putString(CalendarActivity.WIDGET_TEXT + widgetID,String.valueOf(days)).apply();
            widgetView.setTextViewText(R.id.textView, sp.getString(CalendarActivity.WIDGET_TEXT + widgetID, null) + " - количество оставшихся дней");
        }
        Intent configIntent = new Intent(context, CalendarActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE).putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        PendingIntent pIntent = PendingIntent.getActivity(context, widgetID, configIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.r_layout, pIntent);
        appWidgetManager.updateAppWidget(widgetID, widgetView);
    }

    public static void loadDate(Context context, int widgetID){
        if (context != null) {
            DBHelper dbHelper = new DBHelper(context);
            Integer[] date = dbHelper.getDate(widgetID);
            if (date != null) {
                dayOfMonth = date[0];
                month = date[1];
                year = date[2];
                started = date[3];
                curDate = dayOfMonth + "/" + month + "/" + year;
            }
        }
    }

    public static long getDaysDiff(Context context, int dayOfMonth, int month, int year){
        String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        String[] array = date.split("/");
        if (year > Integer.valueOf(array[2])) {
            return computeDays(dayOfMonth, month, year);
        }else {
            if (year == Integer.valueOf(array[2])) {
                if (month > Integer.valueOf(array[1])) {
                    return computeDays(dayOfMonth, month, year);
                } else {
                    if (month == Integer.valueOf(array[1])) {
                        if (dayOfMonth > Integer.valueOf(array[0])) {
                            return computeDays(dayOfMonth, month, year);
                        }else
                        if (dayOfMonth == Integer.valueOf(array[0])){
                            String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                            String hours = time.split(":")[0];
                            long hoursLong = Long.valueOf(hours);
                            if (hoursLong >= 9)
                                return -1;
                            else
                            if (hoursLong == 8)
                                return -2;
                            else
                                return 0;
                        }
                    }
                }
            }
        }
        return -1;
    }

    private static long computeDays(int dayOfMonth, int month, int year){
        Calendar tempcal = Calendar.getInstance();
        tempcal.set(Calendar.YEAR, year);
        tempcal.set(Calendar.MONTH, month);
        tempcal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return ((tempcal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis())/ (24 * 60 * 60 * 1000))-30;
    }

    public static void notificationEventStarted(Context context, int widgetID){
            Intent notificationIntent = new Intent(context, CalendarActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            Resources res = context.getResources();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setChannelId("КУРЛЫК НОТИФИКАЦИЯ");
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("ВАЛЕРА НАСТАЛО ТВОЁ ВРЕМЯ")
                    .setContentText("Поступило уведомление от виджета номер " + widgetID)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_notification))
                    .setTicker("БУМ, здесь идёт уведомление")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true);

            builder.setPriority(Notification.PRIORITY_HIGH);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1488, builder.build());
        }
    }