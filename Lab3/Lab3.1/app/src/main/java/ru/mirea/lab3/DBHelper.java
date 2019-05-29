package ru.mirea.lab3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String DATABASE_NAME = "Students.db";
    public static final String STUDENTS_TABLE_NAME = "students";
    public static final int DATABASE_VERSION= 1;
    public static final String STUDENT_TABLE__ID = BaseColumns._ID;
    public static final String STUDENT = "student";;
    public static final String STUDENT_FIO = "fio";
    public static final String STUDENT_CREATEDATESTR = "createdateStr";

    public List<String> studentfam = Arrays.asList("Ivanov", "Sudarikov", "Maksimov", "Bashkin","Sagdeev","Trofimov", "Shvedkov", "Bolshakov", "Maddyson", "Nuclearov");
    public List<String> studentnam = Arrays.asList("Kirill", "Maksim", "Arkadiy", "Anton", "Ilya", "Andrey", "Xeophant", "Xerx", "Thanos", "Oxy");
    public List<String> studentsec = Arrays.asList("Alekseevich", "Kirillovich", "Arkadievich", "Atheistovich", "Vlasovich", "Xenophantovich","Naumovich", "Removich", "Oxxxymironovich", "Palladievich");

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + STUDENTS_TABLE_NAME +
                        "( " + STUDENT_TABLE__ID + " INTEGER PRIMARY KEY, " + STUDENT_FIO + " TEXT, " + STUDENT_CREATEDATESTR + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ STUDENTS_TABLE_NAME);
        onCreate(db);
    }



    private long getDate(String day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(day);
        } catch (ParseException e) {}
        return date.getTime();
    }

    private long getTextcreateDate(String day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(day);
        } catch (ParseException e) {}
        return date.getTime();
    }

    public boolean insert(String fio, String createdateStr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_FIO, fio);
        contentValues.put(STUDENT_CREATEDATESTR, getTextcreateDate(createdateStr));
        db.insert(STUDENTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean update(String id, String fio, String createdateStr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_FIO, fio);
        contentValues.put(STUDENT_CREATEDATESTR, getTextcreateDate(createdateStr));
        db.update(STUDENTS_TABLE_NAME, contentValues, STUDENT_TABLE__ID + " = ? ", new String[]{id});
        return true;
    }

    public Cursor getcreateddatesortedData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
                res = db.rawQuery("select * from " + STUDENTS_TABLE_NAME + " order by " + STUDENT_CREATEDATESTR + " asc", null);
                return  res;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
                res = db.rawQuery("select * from " + STUDENTS_TABLE_NAME, null);
        return res;
    }


    public Cursor getDataSpecific(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
                res = db.rawQuery("select * from " + STUDENTS_TABLE_NAME + " WHERE "+STUDENT_TABLE__ID+" = '" + id + "' order by " + STUDENT_CREATEDATESTR + " asc", null);
        return res;
    }

    public void deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
                db.delete(STUDENTS_TABLE_NAME, STUDENT_TABLE__ID+" = ?", new String[] {id});
    }

    public long getNumberOfStrings(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
                c =  db.rawQuery("select * from "+ STUDENTS_TABLE_NAME +" order by "+STUDENT_CREATEDATESTR+" asc", null);
        return c.getCount();
    }

    public long getMillis(int i){
        SQLiteDatabase db = this.getReadableDatabase();
        long date; Cursor cur; String query;
                query = "SELECT " + STUDENT_CREATEDATESTR + " FROM " + STUDENTS_TABLE_NAME;
                cur = db.rawQuery(query, null);
                cur.moveToPosition(i);
                date = cur.getLong(cur.getColumnIndex(STUDENT_CREATEDATESTR));
        return date;
    }

    private long yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTimeInMillis();
    }

    public void updateTable(){
        Date date = new Date();
        SQLiteDatabase db = this.getWritableDatabase();
        String query; Cursor cur;
                query = "SELECT " + STUDENT_CREATEDATESTR + " FROM " + STUDENTS_TABLE_NAME;
                cur = db.rawQuery(query, null);
                while(cur.moveToNext()){
                    long time = cur.getLong(cur.getColumnIndex(STUDENT_CREATEDATESTR));
                    date.setTime(time);
                    if(date.getTime()<yesterday())
                        db.delete(STUDENTS_TABLE_NAME, STUDENT_CREATEDATESTR + "=" + cur.getLong(cur.getColumnIndex(STUDENT_CREATEDATESTR)), null);
        }
    }

    public void deleteEmptyRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + STUDENTS_TABLE_NAME + " WHERE " + STUDENT_FIO + " IS NULL OR trim(" + STUDENT_FIO + ") = '';");
    }

    public void deleteAllRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + STUDENTS_TABLE_NAME + ";");
    }

    public int studentgenerator() {
        return Math.round((float) Math.random() * 9);
    }

    public boolean ivanovtransformer(String createdateStr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_FIO, "Ivanov Ivan Ivanovich");
        contentValues.put(STUDENT_CREATEDATESTR, getTextcreateDate(createdateStr));
        db.update(STUDENTS_TABLE_NAME, contentValues, STUDENT_TABLE__ID + " = (SELECT MAX("+STUDENT_TABLE__ID+")  FROM "+STUDENTS_TABLE_NAME+") ",null);
        return true;
    }

    public void student5roulette(String createdateStr) {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, DATABASE_VERSION, DATABASE_VERSION);
        for (int i = 0; i < 5; i++) {
            addrandomstudent(createdateStr);
        }
    }

    public void addrandomstudent(String createdateStr) {
        insert(studentfam.get(studentgenerator()) + " " + studentnam.get(studentgenerator()) + " " + studentsec.get(studentgenerator()), createdateStr);
        }
    }