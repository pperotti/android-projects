package com.pperotti.android.one2one.base;

import android.view.View;

/**
 * Created by pperotti on 9/7/16.
 */
public interface ClickListener {

    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
