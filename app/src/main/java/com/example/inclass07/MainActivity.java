package com.example.inclass07;
/*
* MainActiity.java
* Group 29
* Mayuri Jain
* Narendra Pahuja
* Inclass 07
* */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText ed_word;
    Button bt_search;
    SeekBar Sb_progress;
    RadioGroup rg_sorting;
    RadioButton rb_track;
    RadioButton rb_artist;
    ListView listView;
    ProgressBar pg_progress;
    musicAdapter musicAdapter;
    ArrayList<Music> musicList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_word = findViewById(R.id.et_word);
        bt_search = findViewById(R.id.bt_search);
        Sb_progress = findViewById(R.id.seekBar);
        rg_sorting = findViewById(R.id.rg_sorting);
        rb_track = findViewById(R.id.rd_track);
        rb_artist = findViewById(R.id.rd_artist);
        listView = findViewById(R.id.listView);
        pg_progress = findViewById(R.id.pg_progress);

        rb_track.setChecked(true);
        Sb_progress.setMin(5);
        pg_progress.setVisibility(View.INVISIBLE);

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isConnected()){

                    if(ed_word.getText().toString().isEmpty()){
                        ed_word.setError(getResources().getString(R.string.error));
                    }else{
                        String url ="";
                        if(rb_track.isChecked()){
                            url = "http://api.musixmatch.com/ws/1.1/track.search?q="+ed_word.getText()+"&page_size="+Sb_progress.getProgress()+"&s_track_rating=desc&apikey=3173fa988b6104fbfc2def6458d8cae0";
                        }else{
                            url = "http://api.musixmatch.com/ws/1.1/track.search?q="+ed_word.getText()+"&page_size="+Sb_progress.getProgress()+"&s_artist_rating=desc&apikey=3173fa988b6104fbfc2def6458d8cae0";
                        }

                        Log.d("demo",url);
                        new GetDataAsync().execute(url);
                    }

                }else{
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rg_sorting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        String url2 = "";
                musicList.clear();
                switch(radioGroup.getCheckedRadioButtonId()){
                    case R.id.rd_artist:
                        if(ed_word.getText().toString().isEmpty()){
                            ed_word.setError(getResources().getString(R.string.error));
                            break;  }
                        url2 = "http://api.musixmatch.com/ws/1.1/track.search?q="+ed_word.getText()+"&page_size="+Sb_progress.getProgress()+"&s_artist_rating=desc&apikey=3173fa988b6104fbfc2def6458d8cae0";
                        new GetDataAsync().execute(url2);
                        break;
                    case R.id.rd_track:
                        if(ed_word.getText().toString().isEmpty()){
                            ed_word.setError(getResources().getString(R.string.error));
                            break;  }
                        url2 = "http://api.musixmatch.com/ws/1.1/track.search?q="+ed_word.getText()+"&page_size="+Sb_progress.getProgress()+"&s_track_rating=desc&apikey=3173fa988b6104fbfc2def6458d8cae0";
                        new GetDataAsync().execute(url2);
                        break;
                    default:
                        break;
                }

            }
        });



    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private class GetDataAsync extends AsyncTask<String, Void, List<Music>> {
        @Override
        protected void onPreExecute() {
            pg_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Music> doInBackground(String... params) {

            HttpURLConnection connection = null;
            ArrayList<Music> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                Log.d("demo","outside connection");
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("demo","inside connection");
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONObject body = root.getJSONObject("message").getJSONObject("body");
                    JSONArray listarray = body.getJSONArray("track_list");
                    for (int i = 0; i < listarray.length(); i++) {
                        JSONObject trackJsonObject = listarray.getJSONObject(i);
                        JSONObject trackJson = trackJsonObject.getJSONObject("track");
                        String trackName = trackJson.getString("track_name");
                        String albumName = trackJson.getString("album_name");
                        String artistName = trackJson.getString("artist_name");
                        String[] updatedTimearray=  trackJson.getString("updated_time").split("T");
                        String updatedTime=updatedTimearray[0];
                        String track_share_url = trackJson.getString("track_share_url");
                        Music music = new Music(trackName,albumName,artistName,updatedTime,track_share_url);
                        Log.d("demo",music.toString());
                        result.add(music);
                    }
                }
            } catch (Exception e) {
                //Handle Exceptions
            } finally {
                //Close the connections
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<Music> musicsList) {
            pg_progress.setVisibility(View.INVISIBLE);
            for (Music music : musicsList) {
                musicList.add(music);
            }
            //musicAdapter.notifyDataSetChanged();
            musicAdapter = new musicAdapter(MainActivity.this, R.layout.music_items, musicList);
            listView.setAdapter(musicAdapter);
            musicAdapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(musicList.get(i).trackShareUrl));
                    startActivity(intent);
                }
            });
        }
    }


}
