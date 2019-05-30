package com.hfad.musicwizard.MusicPlayer;

import android.support.annotation.Nullable;

public interface Player {

    void play(String url);
    void pause();
    void resume();

    boolean isPlaying();

    @Nullable
    String getCurrentTrackURL();

    void release();
}
