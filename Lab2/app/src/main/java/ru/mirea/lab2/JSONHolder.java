package ru.mirea.lab2;

import org.json.JSONArray;

public class JSONHolder {

    private static volatile JSONHolder instance;
    private JSONArray data;

    public static JSONHolder getInstance() {
        JSONHolder localInstance = instance;
        if (localInstance == null) { instance = localInstance = new JSONHolder(); }
        return localInstance;
    }

    public void setData(JSONArray data){
        this.data = data;
    }

    public JSONArray getData(){
        return this.data;
    }

}