package com.example.lijiang.bookandmovie.Utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by cnrobin on 17-8-14.
 * Just Enjoy It!!!
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int betweenSpace;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = betweenSpace;
        outRect.right = betweenSpace;
        super.getItemOffsets(outRect, view, parent, state);
    }

    public SpaceItemDecoration(int betweenSpace) {
        this.betweenSpace = betweenSpace;
    }
}
