package com.hfad.musicwizard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hfad.musicwizard.MusicPlayer.MainActivity;

import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

import static com.hfad.musicwizard.R.id.navigation_home;

public class LyricActivity extends AppCompatActivity {

    private TextView textViewLyrics;
    private EditText editTextSong;
    private String artistName;
    private String lyricsBody;
    private String trackName;
    private int trackID;
    private TrackData data;
    private Track track;
    private EditText editTextArtist;
    private String apiKey = "4ab5ae9e96c6b208d9601e182c4af443";
    private int x = 0;
    private Button buttonLyrics;
    MusixMatch musixMatch = new MusixMatch(apiKey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_lyrics);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case navigation_home:
                        Intent a = new Intent(LyricActivity.this, MainActivity.class);
                        startActivity(a);
                        return true;
                    case R.id.navigation_lyrics:
                        return true;
                    case R.id.navigation_concerts:
                        Intent b = new Intent(LyricActivity.this, ConcertActivity.class);
                        startActivity(b);
                        return true;
                }

                return false;
            }
        });

        wireWidgets();
        buttonLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Lyrics().execute(artistName, trackName);

            }
        });


    }

    private void wireWidgets() {
        textViewLyrics = findViewById(R.id.textView_music_lyrics);
        editTextArtist = findViewById(R.id.editText_lyric_artist);
        buttonLyrics = findViewById(R.id.button_lyrics);
        editTextSong = findViewById(R.id.editText_lyric_song);
    }


    public class Lyrics extends AsyncTask<String, String, String>

    {


        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            try {

                trackName = editTextSong.getText().toString();
                artistName = editTextArtist.getText().toString();
                track = musixMatch.getMatchingTrack(trackName, artistName);
                data = track.getTrack();
                trackID = data.getTrackId();

                org.jmusixmatch.entity.lyrics.Lyrics lyrics = musixMatch.getLyrics(trackID);
                lyricsBody = lyrics.getLyricsBody();
                textViewLyrics.setText(lyricsBody);


                } catch (MusixMatchException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String lyricsBody) {
            super.onPostExecute(lyricsBody);


    }




    }}
