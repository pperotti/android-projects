package com.pperotti.android.one2one.navigator;

import android.content.Context;

import com.pperotti.android.one2one.task.view.controller.activity.NewTaskActivity;

/**
 * Created by pperotti on 12/6/16.
 */
public class NavigatorImpl implements Navigator {

    @Override
    public void openNewTask(Context context) {
        NewTaskActivity.startActivity(context);
    }
}
