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

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

import static com.hfad.musicwizard.R.id.navigation_home;

public class LyricActivity extends AppCompatActivity {

    private TextView textViewLyrics;
    private EditText editTextSong;
    private EditText editTextArtist;

    private String artistName;
    private String trackName;
    private String lyricsBody;

    private String apiKey = "4ab5ae9e96c6b208d9601e182c4af443";
    MusixMatch musixMatch = new MusixMatch(apiKey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);

        textViewLyrics = findViewById(R.id.textview_lyricactivity);
        editTextArtist = findViewById(R.id.edittext_lyricactivity_artistname);
        editTextSong = findViewById(R.id.edittext_lyricactivity_songname);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(LyricActivity.this, LoginActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.bottom_navigation:
                        break;

                    case R.id.navigation_concerts:
                        Intent intent3 = new Intent(LyricActivity.this, ConcertActivity.class);
                        startActivity(intent3);
                        break;
                }

                return false;
            }
        });

        Button buttonLyrics = findViewById(R.id.button_lyrics);
        buttonLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Lyrics().execute(artistName, trackName);
                textViewLyrics.setText(lyricsBody);
            }
        });
    }


    public class Lyrics extends AsyncTask<String, String, String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            try {
                trackName = editTextSong.getText().toString();
                artistName = editTextArtist.getText().toString();
                Track track = musixMatch.getMatchingTrack(trackName, artistName);
                TrackData data = track.getTrack();
                int trackID = data.getTrackId();

                org.jmusixmatch.entity.lyrics.Lyrics lyrics = musixMatch.getLyrics(trackID);
                lyricsBody = lyrics.getLyricsBody();
            }
            catch (MusixMatchException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String lyricsBody) {
            super.onPostExecute(lyricsBody);
        }
    }
}
