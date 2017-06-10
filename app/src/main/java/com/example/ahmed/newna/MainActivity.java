package com.example.ahmed.newna;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private final String URL_TO_HIT = "https://newsapi.org/v1/articles?source=bbc-news&apiKey=5690a4f95de64e9db3fe6fa4fba3a193";
    private ListView List;
    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        //3shan y3ml show lil images
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        //3shan y3ml load lil images kol mara yft7 feha
        ImageLoader.getInstance().init(config);

        List = (ListView)findViewById(R.id.List);


        //3shan ybd2 y3ml fitch lil url w abd2 anady 3ala onexecute w onbackgroud hytnada 3leha
        new JSONTask().execute(URL_TO_HIT);
    }
///class da 3shan a2ra el data eli fe el file
    public class JSONTask extends AsyncTask<String,String, List<newsItem> >{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<newsItem> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("articles");

                List<newsItem> newsItemList = new ArrayList<>();

                Gson gson = new Gson();
                for(int i=0; i<parentArray.length(); i++) {
                    //ha2ra jason object w a7to fe object mn newItem
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    newsItem newsItem = gson.fromJson(finalObject.toString(), newsItem.class);

                    newsItem.setNewsHeading(finalObject.getString("title"));
                    newsItem.setNewsDesc(finalObject.getString("description"));
                    newsItem.setDate(finalObject.getString("publishedAt"));
                    newsItem.setUrl(finalObject.getString("url"));
                    newsItem.setImageURL(finalObject.getString("urlToImage"));
                    newsItem.setAuthor(finalObject.getString("author"));



                    // kol object b2rah b7to fe el list
                    newsItemList.add(newsItem);
                }
                return newsItemList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;
        }
//b3d my3ml exexute w do onbackground onpostexcute htsht3'l 3shan y3ml setup lil adapter 3shan y3rd el rows
        @Override
        protected void onPostExecute(final List<newsItem> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(result != null) {
                NewsAdapter adapter = new NewsAdapter(getApplicationContext(), R.layout.row, result);
                List.setAdapter(adapter);
                List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        newsItem newsItem = result.get(position);
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("newsItem", new Gson().toJson(newsItem));
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public class NewsAdapter extends ArrayAdapter{

        private List<newsItem> newsItemList;
        private int resource;
        private LayoutInflater inflater;
        public NewsAdapter(Context context, int resource, List<newsItem> objects) {
            super(context, resource, objects);
            newsItemList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);

                holder.NewsImage = (ImageView)convertView.findViewById(R.id.NewsImage);
                holder.newsHeading = (TextView)convertView.findViewById(R.id.heading);

                holder.date = (TextView)convertView.findViewById(R.id.date);

                holder.author = (TextView)convertView.findViewById(R.id.author);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar);
            // y3rd el images

            ImageLoader.getInstance().displayImage(newsItemList.get(position).getImageURL(), holder.NewsImage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                }
            });

            holder.newsHeading.setText(newsItemList.get(position).getNewsHeading());
            //holder.newsDesc.setText(newsItemList.get(position).getNewsDesc());
            holder.date.setText("Date: " + newsItemList.get(position).getDate());
            //  holder.url.setText("Duration:" + newsItemList.get(position).getUrl());
            holder.author.setText("Author:" + newsItemList.get(position).getAuthor());

            // rating bar



            return convertView;
        }


        class ViewHolder{


            private ImageView NewsImage;
            private TextView newsHeading;
            private TextView newsDesc;
            private TextView date;
            private TextView url;
            private TextView author;

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            new JSONTask().execute(URL_TO_HIT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
