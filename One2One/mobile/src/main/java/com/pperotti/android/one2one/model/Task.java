package com.pperotti.android.one2one.model;

import com.google.gson.Gson;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pperotti on 13/6/16.
 */
public class Task extends SugarRecord {

    private String mSubject;
    private String mContent;
    private long mTimeInMillis;
    private int groupId;
    private String mRawContacts;

    @Ignore
    private ArrayList<String> mContactEmails;

    public Task() {

    }

    public long getTimeInMillis() {
        return mTimeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        mTimeInMillis = timeInMillis;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<String> getContactEmails() {

        if (mRawContacts != null) {
            Gson gson = new Gson();
            mContactEmails = gson.fromJson(mRawContacts, ArrayList.class);
        }

        return mContactEmails;
    }

    public void setContactEmails(ArrayList<String> contactEmails) {
        this.mContactEmails = contactEmails;

        Gson gson = new Gson();
        mRawContacts = gson.toJson(mContactEmails, ArrayList.class);
    }


}
