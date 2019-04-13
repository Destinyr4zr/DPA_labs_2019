package ru.mirea.lab2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;

public class ListViewAdapter extends BaseAdapter {

    public ListViewAdapter(int length, Context context) {
        this.itemCount = length;
        this.context = context;
    }

    private class ViewHolder
    {
        public View linearLayout;
        private ImageView image;
        private TextView text;
        private View view;

        public ViewHolder (View itemview)
        {
//            super(itemview);
            image = itemview.findViewById(R.id.itemImage);
            text = itemview.findViewById(R.id.itemText);
            linearLayout = itemview.findViewById(R.id.row);
            this.view = itemview;
        }
    }

    @Override
    public int getCount() {
        return itemCount;
    }

    @Override
    public long getItemId (int index) {return 0;}

    @Override
    public Object getItem (int index) {return null;}

    @Override
    public View getView (int position, View convertView, ViewGroup parent)
    {
        View view = new View (context);
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = view.getTag();
        }

        final int index = position + 1;
        JSONArray data = JSONHolder.getInstance().getData();

        TextView text = listtransfer.text;
        try {
            text.setText(data.getJSONObject(index).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView image = listtransfer.image;

        DownloadImageTask load_image_task = new DownloadImageTask(image);
        String base_url = "https://raw.githubusercontent.com/wesleywerner/ancient-tech/02decf875616dd9692b31658d92e64a20d99f816/src/images/tech/";
        try {
            load_image_task.execute(base_url + data.getJSONObject(index).getString("graphic"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int backgroundColor = ContextCompat.getColor(listtransfer.itemView.getContext(),
                (index) % 2 == 0 ? R.color.gray : R.color.white);

        listtransfer.linearLayout.setBackgroundColor(backgroundColor);

        listtransfer.view.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View arg0) {
                                                     Intent intent = new Intent(context, ViewPagerClass.class);

                                                     intent.putExtra("position", index-1);
                                                     System.out.println(index-1);

                                                     context.startActivity(intent);
                                                 }
                                             }
        );
        return view;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlarray = urls[0];
            Bitmap bump = null;
            try {
                InputStream in = new java.net.URL(urlarray).openStream();
                bump = BitmapFactory.decodeStream(in);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return bump;
        }

        protected void onPostExecute(Bitmap result) {
            if(result != null) {
                bmImage.setImageBitmap(result);
            }
        }
    }

    private int itemCount = 0;
    private Context context;
    private LayoutInflater inflater;
}
