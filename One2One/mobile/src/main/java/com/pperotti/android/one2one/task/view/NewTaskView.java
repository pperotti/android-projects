package com.pperotti.android.one2one.task.view;

/**
 * Created by pperotti on 12/6/16.
 */
public interface NewTaskView {

    /**
     * A tap on DONE or SEND was executed.
     */
    void onDone();

    /**
     * A tap on BACK was executed.
     */
    void onBack();

    /**
     * Validation errors
     */
    void onValidationError(String errorToDisplay);
}
