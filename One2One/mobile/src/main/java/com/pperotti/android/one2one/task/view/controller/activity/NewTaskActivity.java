package com.pperotti.android.one2one.task.view.controller.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pperotti.android.one2one.R;
import com.pperotti.android.one2one.contacts.controller.fragment.ContactListActivity;
import com.pperotti.android.one2one.model.Group;
import com.pperotti.android.one2one.model.Task;
import com.pperotti.android.one2one.task.presenter.NewTaskPresenter;
import com.pperotti.android.one2one.task.presenter.NewTaskPresenterImpl;
import com.pperotti.android.one2one.task.view.NewTaskView;

/**
 * Created by pperotti on 12/6/16.
 */
public class NewTaskActivity extends AppCompatActivity implements NewTaskView {

    public static final int REQUEST_ID_GROUP_SELECTION = 0x2000;
    public static final String TAG = NewTaskActivity.class.getSimpleName();

    /**
     * Buttons
     */
    ImageView mBack;
    ImageView mDone;
    Button mSend;
    Button mClear;
    ImageView mContacts;
    ImageView mRemoveGroup;

    /**
     * Fields
     */
    EditText mSubject;
    EditText mBody;
    EditText mSelectedGroup;

    //Buttons
    Button mBold, mItalic, mLink;

    //Presenter
    NewTaskPresenter mNewTaskPresenter;

    Group group;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, NewTaskActivity.class));
    }

    public NewTaskActivity() {
        mNewTaskPresenter = new NewTaskPresenterImpl(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        mBack = (ImageView) findViewById(R.id.newTask_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewTaskPresenter.selectBack();
            }
        });

        mDone = (ImageView) findViewById(R.id.newTask_done);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canProceed()) {
                    mNewTaskPresenter.selectDone(getApplicationContext(), newTask());
                }
            }
        });

        mSend = (Button) findViewById(R.id.newTask_send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canProceed()) {
                    mNewTaskPresenter.selectDone(getApplicationContext(), newTask());
                }
            }
        });

        mClear = (Button) findViewById(R.id.newTask_clear);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rawText = mBody.getText().toString();
                if (rawText.length()>0) {
                    mBody.setText(rawText);
                }
                Log.d(TAG, Html.toHtml(mBody.getText()));
            }
        });

        mSubject = (EditText) findViewById(R.id.newTask_subject);
        mBody = (EditText) findViewById(R.id.newTask_content);

        mBold = (Button) findViewById(R.id.bold);
        mBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStyle(android.graphics.Typeface.BOLD);
            }
        });
        mItalic = (Button) findViewById(R.id.italic);
        mItalic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStyle(Typeface.ITALIC);
            }
        });

        mLink = (Button) findViewById(R.id.link);
        mLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLink();
            }
        });

        mContacts = (ImageView) findViewById(R.id.contacts);
        mContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(NewTaskActivity.this, ContactListActivity.class), REQUEST_ID_GROUP_SELECTION);
            }
        });

        mSelectedGroup = (EditText) findViewById(R.id.newTask_selected_group);

        mRemoveGroup = (ImageView) findViewById(R.id.newTask_clear_group);
        mRemoveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedGroup.setText("");
            }
        });
    }

    private void updateLink() {
        int start = mBody.getSelectionStart();
        int end = mBody.getSelectionEnd();

        boolean isNoSelection = start == 0 && end == 0;

        if (!isNoSelection) {
            Linkify.addLinks(mBody, Linkify.WEB_URLS);
        }
    }

    private void updateStyle(int style) {
        int start = mBody.getSelectionStart();
        int end = mBody.getSelectionEnd();

        boolean isNoSelection = start == 0 && end == 0;

        if (!isNoSelection) {

            //Obtain selected text
            CharSequence selection = mBody.getText().subSequence(start, end);

            final SpannableStringBuilder str = new SpannableStringBuilder(selection);
            str.setSpan(new android.text.style.StyleSpan(style), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            //Remove selected text.
            mBody.getText().delete(start, end).insert(start, str);

        }
    }

    private boolean canProceed() {

        if (mSelectedGroup.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.error_no_group, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mSubject.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.error_no_subject, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mBody.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.error_no_body, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private Task newTask() {
        Task t = new Task();
        t.setSubject(mSubject.getText().toString());
        t.setContent(Html.toHtml(mBody.getText()));
        t.setTimeInMillis(System.currentTimeMillis());
        t.setGroupId(group.getGroupId());

        return t;
    }

    @Override
    public void onDone() {
        finishSelection();
    }

    @Override
    public void onBack() {
        finishSelection();
    }

    @Override
    public void onValidationError(String errorToDisplay) {
        Toast.makeText(this, errorToDisplay, Toast.LENGTH_LONG).show();
    }

    private void finishSelection() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_ID_GROUP_SELECTION == requestCode) {
            if (Activity.RESULT_OK == resultCode && data != null) {

                int groupId = data.getIntExtra("id", -1);
                String groupName = data.getStringExtra("name");
                Log.d(TAG, "ID=" + groupId + " NAME=" + groupName);

                group = new Group(groupId, groupName, 0);

                updateGroupName(groupName);
            }
        }
    }

    private void updateGroupName(final String name) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSelectedGroup.setText(name);
            }
        });
    }


}
