package com.pperotti.android.one2one.task.presenter;

import android.content.Context;

import com.pperotti.android.one2one.mail.SendMailTask;
import com.pperotti.android.one2one.model.Task;
import com.pperotti.android.one2one.task.view.NewTaskView;

/**
 * Created by pperotti on 12/6/16.
 */
public class NewTaskPresenterImpl implements NewTaskPresenter {

    NewTaskView newTaskView;

    public NewTaskPresenterImpl(NewTaskView newTaskView) {
        this.newTaskView = newTaskView;
    }

    @Override
    public void selectDone(Context context, Task task) {

        String validationError = getValidationError(task);
        if (validationError == null) {

            task.save();

            SendMailTask sendMailTask = new SendMailTask(context, task);
            sendMailTask.execute();

            newTaskView.onDone();
        } else {
            newTaskView.onValidationError(validationError);
        }

    }

    @Override
    public void selectBack() {
        newTaskView.onBack();
    }

    private String getValidationError(Task task) {
        String validationError = null;

        //Add validations

        return validationError;
    }
}
