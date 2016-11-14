package com.pperotti.android.one2one.task.presenter;

import android.content.Context;

import com.pperotti.android.one2one.model.Task;

/**
 * Created by pperotti on 12/6/16.
 */
public interface NewTaskPresenter {

    /**
     * The user tap on send or done.
     */
    void selectDone(Context context, Task task);

    /**
     * The user tap on back.
     */
    void selectBack();

}
