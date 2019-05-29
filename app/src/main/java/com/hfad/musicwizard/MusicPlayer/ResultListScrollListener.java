package com.hfad.musicwizard.MusicPlayer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class ResultListScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = "ResultListListener";
    private final LinearLayoutManager linearLayoutManager;
    private static final int SCROLL_BUFFER = 3;
    private int itemCount = 0;
    private boolean areThereMoreItems = true;

    public ResultListScrollListener(LinearLayoutManager layoutManager) {
        linearLayoutManager = layoutManager;
    }

    public void reset() {
        itemCount = 0;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int itemCount = linearLayoutManager.getItemCount();
        int itemPosition = linearLayoutManager.findLastVisibleItemPosition();

        if (areThereMoreItems && itemCount > this.itemCount) {
            this.itemCount = itemCount;
            areThereMoreItems = false;
        }

        Log.d(TAG, String.format("loading %s, item count: %s/%s, itemPosition %s", areThereMoreItems, this.itemCount, itemCount, itemPosition));

        if (!areThereMoreItems && itemPosition + 1 >= itemCount - SCROLL_BUFFER) {
            areThereMoreItems = true;
            onLoadMore();
        }
    }

    public abstract void onLoadMore();

}
