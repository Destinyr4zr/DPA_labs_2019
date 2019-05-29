package ru.mirea.lab4;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarActivity extends Activity {

    Activity contextActivity;
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    static String curDate;

    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_TEXT = "widget_text_";
    public final static String WIDGET_COLOR = "widget_color_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Определение вызвавшего виджета
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        setResult(RESULT_CANCELED, resultValue);
        setContentView(R.layout.choose_date_activity_layout);
        Button button = findViewById(R.id.choose_button);
        contextActivity = this;
        final CalendarView calendar = findViewById(R.id.calendarView);
        DateWidget.loadDate(this, widgetID);
        if (DateWidget.curDate != null && !DateWidget.curDate.equals("")){
            Calendar tempcal = Calendar.getInstance();
            tempcal.set(Calendar.YEAR,  DateWidget.year);
            tempcal.set(Calendar.MONTH,  DateWidget.month-1);
            tempcal.set(Calendar.DAY_OF_MONTH,  DateWidget.dayOfMonth);
            long milliTime = tempcal.getTimeInMillis();
            calendar.setDate(milliTime);
        }

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                long curLongDate =  Calendar.getInstance().getTimeInMillis();
                Calendar tempcal = Calendar.getInstance();
                tempcal.set(Calendar.YEAR,  year);
                tempcal.set(Calendar.MONTH,  month);
                tempcal.set(Calendar.DAY_OF_MONTH,  dayOfMonth);
                long differ = tempcal.getTimeInMillis() - curLongDate;
                if (differ < 0){
                    Toast toast = Toast.makeText(contextActivity, "Выберите будущую дату", Toast.LENGTH_LONG);
                    toast.show();
                    String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
                    String[] array = date.split("/");
                    Calendar calendarNow = Calendar.getInstance();
                    year = Integer.valueOf(array[2]);
                    month = Integer.valueOf(array[1])-1;
                    dayOfMonth = Integer.valueOf(array[0]);
                    calendarNow.set(Calendar.YEAR,  year);
                    calendarNow.set(Calendar.MONTH,  month);
                    calendarNow.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    long milliTime = calendarNow.getTimeInMillis();
                    calendar.setDate(milliTime);
                }
                curDate = dayOfMonth + "/" + month + 1 + "/" + year;
                DateWidget.curDate = curDate;
                DateWidget.dayOfMonth = dayOfMonth;
                DateWidget.month = month + 1;
                DateWidget.year = year;
                DateWidget.started = 0;
                DBHelper dbHelper = new DBHelper(contextActivity);
                dbHelper.setDate(dayOfMonth, month + 1, year, 0, widgetID);
                long days = differ / (24 * 60 * 60 * 1000);
                DateWidget.days = days;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(WIDGET_TEXT + widgetID, Long.toString(DateWidget.days));
                editor.apply();
                setResult(RESULT_OK, resultValue);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(contextActivity);
                DateWidget.updateWidget(contextActivity, appWidgetManager, widgetID);
                finish();
            }
        });
    }
}