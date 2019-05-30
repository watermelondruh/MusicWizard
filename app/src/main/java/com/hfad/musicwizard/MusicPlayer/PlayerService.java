package com.hfad.musicwizard.MusicPlayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class PlayerService extends Service {

    private final IBinder iBinder = new PlayerBinder();
    private PreviewPlayer previewPlayer = new PreviewPlayer();

    public static Intent getIntent(Context context) {
        return new Intent(context, PlayerService.class);
    }

    public class PlayerBinder extends Binder {
        public Player getService() {
            return previewPlayer;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onDestroy() {
        previewPlayer.release();
        super.onDestroy();
    }

}
