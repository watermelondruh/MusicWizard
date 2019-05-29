package com.hfad.musicwizard.MusicPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.hfad.musicwizard.ConcertActivity;
import com.hfad.musicwizard.LyricActivity;
import com.hfad.musicwizard.R;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class MainActivity extends AppCompatActivity implements Search.View {

    private SearchView searchView;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView resultListRecyclerView;


    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private ScrollListener scrollListener = new ScrollListener(linearLayoutManager);
    private Search.ActionListener actionListener;
    private SearchResultAdapter adapter;

    public static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "EXTRA_QUERY";


    private class ScrollListener extends ResultListScrollListener {

        public ScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore() {
            actionListener.loadMoreResults();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        String token = getIntent().getStringExtra(EXTRA_TOKEN);

        actionListener = new SearchPresenter(this, this);
        actionListener.init(token);

        wireWidgets();
    }

    private void wireWidgets() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        break;

                    case R.id.navigation_lyrics:
                        Intent intent2 = new Intent(MainActivity.this, LyricActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.navigation_concerts:
                        Intent intent3 = new Intent(MainActivity.this, ConcertActivity.class);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });

        // setup search field
        searchView = findViewById(R.id.searchview_mainactivity_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                actionListener.search(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // setup search results list
        adapter = new SearchResultAdapter(this, new SearchResultAdapter.ItemSelectedListener() {
            @Override
            public void onItemSelected(View itemView, Track item) {
                actionListener.selectTrack(item);
            }
        });

        resultListRecyclerView = findViewById(R.id.recyclerview_mainactivity_searchresults);
        resultListRecyclerView.setHasFixedSize(true);
        resultListRecyclerView.setLayoutManager(linearLayoutManager);
        resultListRecyclerView.addOnScrollListener(scrollListener);
        resultListRecyclerView.setAdapter(adapter);

    }

    @Override
    public void reset() {
        adapter.clearData();
    }

    @Override
    public void addData(List<Track> items) {
        adapter.addData(items);
    }

    @Override
    protected void onPause() {
        super.onPause();
        actionListener.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionListener.resume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (actionListener.getCurrentQuery() != null) {
            outState.putString(KEY_CURRENT_QUERY, actionListener.getCurrentQuery());
        }
    }

    @Override
    protected void onDestroy() {
        actionListener.destroy();
        super.onDestroy();
    }

/*
    Connector.ConnectionListener mConnectionListener = new Connector.ConnectionListener() {
        @Override
        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
            mSpotifyAppRemote = spotifyAppRemote;
            // setup all the things
        }

        @Override
        public void onFailure(Throwable error) {
            if (error instanceof NotLoggedInException || error instanceof UserNotAuthorizedException) {
                // Show login button and trigger the login flow from auth library when clicked
            } else if (error instanceof CouldNotFindSpotifyApp) {
                // Show button to download SpotifyAPI
            }
        }
    };

    private void connect() {
        ConnectionParams connectionParams = new ConnectionParams.Builder(CLIENT_ID).setRedirectUri(REDIRECT_URI).showAuthView(true).build();
        SpotifyAppRemote.connect(getApplication(), connectionParams, new Connector.ConnectionListener() {
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote;
                Log.d("MainActivity", "Connected! Yay!");

                //now you can start interacting with App Remote
                connected();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", throwable.getMessage(), throwable);
            }
        });
    }

    private void connected() {
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(playerState -> {
            final Track track = playerState.track;
            if (track != null) {
                Log.d("MainActivity", track.name + " by " + track.artist.name);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
*/
}
