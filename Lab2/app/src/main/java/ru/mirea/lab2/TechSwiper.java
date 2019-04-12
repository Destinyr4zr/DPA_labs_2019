package ru.mirea.lab2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import org.json.JSONArray;
import org.json.JSONException;

public class TechSwiper extends FragmentActivity {
    ViewPager pager;
    PagerAdapter pagerAdapter;
    private JSONArray data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.techswiper_fragment);
        JSONHolder dataHolder = JSONHolder.getInstance();
        JSONArray data = dataHolder.getData();

        this.data = data;

        pagerAdapter = new Techformatter(getSupportFragmentManager(), this.data);
        pager = findViewById(R.id.view_pager);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem((int) getIntent().getSerializableExtra("position"));
    }

    private class Techformatter extends FragmentPagerAdapter {

        private JSONArray data;

        public Techformatter(FragmentManager fm, JSONArray data) {
            super(fm);
            this.data = data;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Header";
        }

        @Override
        public Fragment getItem(int position) {
            position++;
            String graphic = "";
            String helptext = "Всё очевидно";
            try {
                graphic = data.getJSONObject(position).getString("graphic");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                helptext = data.getJSONObject(position).getString("helptext");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            TechFragment fragment = new TechFragment();
            Bundle bundle = new Bundle();
            bundle.putString("graphic", graphic);
            bundle.putString("helptext", helptext);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return data.length();
        }
    }
}
