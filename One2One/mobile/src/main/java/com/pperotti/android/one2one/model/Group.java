package com.pperotti.android.one2one.model;

/**
 * Created by pperotti on 8/7/16.
 */
public class Group {

    private int contactNumber;
    private int groupId;
    private String title;

    public Group(int groupId, String title, int contactNumber) {
        this.groupId = groupId;
        this.title = title;
        this.contactNumber = contactNumber;
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getTitle() {
        return title;
    }
}
