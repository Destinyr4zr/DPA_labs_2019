package ru.mirea.lab2;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.ref.SoftReference;


public class TechFragment extends Fragment {
    String graphic;
    String helptext;

    public TechFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.techfragment, container, false);

        if(getArguments().getString("graphic") != null)
            graphic = getArguments().getString("graphic");
        if(getArguments().getString("helptext") != null)
            helptext = getArguments().getString("helptext");

        TextView desc = view.findViewById(R.id.hepltext);

        desc.setText(helptext);

        if(!graphic.equals("")) {
            ImageView image = view.findViewById(R.id.graphic);
            DownloadImageTask image_loading = new DownloadImageTask(image);
            String base_url = "https://raw.githubusercontent.com/wesleywerner/ancient-tech/02decf875616dd9692b31658d92e64a20d99f816/src/images/tech/";
            image_loading.execute(base_url + graphic);
        }
        return view;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        SoftReference<ImageView> bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage =new SoftReference<> (bmImage);
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap Bump = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                Bump = BitmapFactory.decodeStream(in);
                return Bump;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            if(result != null) {
                bmImage.get().setImageBitmap(result);
            }
        }
    }
}
