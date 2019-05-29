package com.hfad.musicwizard.MusicPlayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class PreviewPlayer implements Player, MediaPlayer.OnCompletionListener {

    private static final String TAG = "PreviewPlayer";

    private MediaPlayer mediaPlayer;
    private String currentTrackURL;

    private class OnPreparedListener implements MediaPlayer.OnPreparedListener {

        private final String url;

        public OnPreparedListener(String url) {
            this.url = url;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            currentTrackURL = url;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        release();
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        currentTrackURL = null;
    }

    @Override
    public void play(String url) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        try {
            createMediaPlayer(url);
            currentTrackURL = url;
        } catch (IOException e) {
            Log.e(TAG, "Could not play: " + url, e);
        }
    }

    @Override
    public void pause() {
        Log.d(TAG, "Pause");
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    @Override
    public void resume() {
        Log.d(TAG, "Resume");
        if (mediaPlayer != null)
            mediaPlayer.start();
    }

    @Override
    public boolean isPlaying() {
        return (mediaPlayer != null && mediaPlayer.isPlaying());
    }

    private void createMediaPlayer(String url) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(url);
        mediaPlayer.setOnPreparedListener(new OnPreparedListener(url));
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.prepareAsync();
    }

    @Override
    @Nullable
    public String getCurrentTrackURL() {
        return currentTrackURL;
    }

    //TODO: review
}
