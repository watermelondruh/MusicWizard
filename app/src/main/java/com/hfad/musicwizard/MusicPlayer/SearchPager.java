package com.hfad.musicwizard.MusicPlayer;

import com.hfad.musicwizard.SpotifyAPI.SpotifyCallback;
import com.hfad.musicwizard.SpotifyAPI.SpotifyError;
import com.hfad.musicwizard.SpotifyAPI.SpotifyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.client.Response;

public class SearchPager {

    private final SpotifyService spotifyService;
    private int currentOffset;
    private int currentPageSize;
    private String currentQuery;

    public interface CompleteListener {
        void onComplete(List<Track> items);
        void onError(Throwable error);
    }

    public SearchPager(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    public void getFirstPage(String query, int pageSize, CompleteListener listener) {
        currentOffset = 0;
        currentPageSize = pageSize;
        currentQuery = query;
        getData(query, 0, pageSize, listener);
    }

    public void getNextPage(CompleteListener listener) {
        currentOffset += currentPageSize;
        getData(currentQuery, currentOffset, currentPageSize, listener);
    }

    private void getData(String query, int offset, final int limit, final CompleteListener listener) {

        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.OFFSET, offset);
        options.put(SpotifyService.LIMIT, limit);

        spotifyService.searchTracks(query, options, new SpotifyCallback<TracksPager>() {
            @Override
            public void success(TracksPager tracksPager, Response response) {
                listener.onComplete(tracksPager.tracks.items);
            }

            @Override
            public void failure(SpotifyError error) {
                listener.onError(error);
            }
        });
    }

}
