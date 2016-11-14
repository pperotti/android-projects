package com.pperotti.android.one2one.contacts.controller.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pperotti.android.one2one.R;
import com.pperotti.android.one2one.base.ClickListener;
import com.pperotti.android.one2one.base.ItemDivider;
import com.pperotti.android.one2one.base.RecyclerTouchListener;
import com.pperotti.android.one2one.model.Group;

/**
 * Created by pperotti on 7/7/16.
 */
public class ContactListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = ContactListActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0x1000;

    RecyclerView recyclerView;
    ContactListAdapter adapter;

    ImageView mBack, mDoneCheck;
    Button mDone;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_contacts);

        mBack = (ImageView) findViewById(R.id.contacts_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mDone = (Button) findViewById(R.id.contacts_done);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.contacts_entryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemDivider((int) getResources().getDimension(R.dimen.activity_horizontal_margin)));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Group selectedGroup = adapter.getItem(position);

                setResult(RESULT_OK, createResult(selectedGroup.getGroupId(), selectedGroup.getTitle()));
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (PackageManager.PERMISSION_GRANTED == permissionCheck) {
            initLoader();
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    private void initLoader() {
        getSupportLoaderManager().initLoader(0, null, this);
    }

    private void presentErrorWarning() {
        Toast.makeText(this, R.string.error_permission_contact, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    initLoader();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    presentErrorWarning();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");

        Uri baseUri = ContactsContract.Groups.CONTENT_URI;
        String[] projection = {ContactsContract.Groups._ID, ContactsContract.Groups.TITLE};
        return new CursorLoader(this, baseUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");

        int titleColumnIndex = data.getColumnIndex(ContactsContract.Groups.TITLE);
        int groupIdIndex = data.getColumnIndex(ContactsContract.Groups._ID);

        data.moveToFirst();

        adapter = new ContactListAdapter(null);

        while (!data.isAfterLast()) {

            int id= data.getInt(groupIdIndex);
            String name = data.getString(titleColumnIndex);

            Log.d(TAG, "Group Title: " + name + " id=" + id);

            Group newGroup = new Group(id, name, 2);
            adapter.addItem(newGroup);

            data.moveToNext();
        }

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset");
    }

    public Intent createResult(int groupId, String groupName) {
        Bundle result = new Bundle();
        result.putInt("id", groupId);
        result.putString("name", groupName);

        //Update the extras to return to the caller activity.
        Intent intent = getIntent();
        intent.putExtras(result);
        return intent;
    }

}
