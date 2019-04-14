package ru.mirea.lab2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

public class TechListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.list_layout, container, false);
        JSONHolder dataHolder = JSONHolder.getInstance();
        JSONArray data = dataHolder.getData();
        Context context = getActivity();
        ListView list = view.findViewById(R.id.list);
        if (data!=null) {
            ListViewAdapter adapter = new ListViewAdapter(data.length() - 1, context);
            list.setAdapter(adapter);
        }
        else
            Toast.makeText(this.getContext(), "Данные не загружены", Toast.LENGTH_SHORT).show();
        return view;
    }
}
