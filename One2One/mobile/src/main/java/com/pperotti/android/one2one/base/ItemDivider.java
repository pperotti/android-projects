package com.pperotti.android.one2one.base;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by pperotti on 12/6/16.
 */
public class ItemDivider extends RecyclerView.ItemDecoration {

    private int space;

    public ItemDivider(int space) {
        this.space = space;
    }

    @Override
    public final void getItemOffsets(Rect outRect, View view,
                                     RecyclerView parent, RecyclerView.State state) {
        outRect.top = 0;
        outRect.left = 0;
        outRect.right = 0;
        outRect.bottom = space / 2;
    }

}
