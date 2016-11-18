package com.pabloperotti.backup.in.the.cloud.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pabloperotti.backup.in.the.cloud.Constants;
import com.pabloperotti.backup.in.the.cloud.MyApp;
import com.pabloperotti.backup.in.the.cloud.R;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

	Button btnAuth = null;
	Button btnSync = null;
	AccountManager mAccountManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authenticator);

		mAccountManager = AccountManager.get(this);

		Log.i(MyApp.TAG, "AccountAuthenticator.onCreate()");

		btnAuth = (Button) findViewById(R.id.button_authenticate);
		btnAuth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.i(MyApp.TAG, "AccountAuthenticator.addingAuth");
				final Account account = new Account("pablo.perotti@gmail.com",
						Constants.ACCOUNT_TYPE);
				mAccountManager.addAccountExplicitly(account, Long.toString(System.currentTimeMillis()), null);

				ContentResolver.setSyncAutomatically(account, Constants.AUTHORITY, true);

				// Perform the authentication
				final Intent intent = new Intent();
				intent.putExtra(AccountManager.KEY_ACCOUNT_NAME,
						"pablo.perotti@gmail.com");
				intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE,
						Constants.ACCOUNT_TYPE);
				setAccountAuthenticatorResult(intent.getExtras());
				setResult(RESULT_OK, intent);

				finish();
			}
		});

		btnSync = (Button) findViewById(R.id.button_sync);
		btnSync.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				//Get the account
				//If any, force to sync
				Account[] accounts = mAccountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
				if ( accounts != null && accounts.length > 0 ) {
					Account firstAccount = accounts[0];

					//Refactor to make it asynchronous (check SyncObserver)
					ContentResolver.requestSync(firstAccount, Constants.AUTHORITY, null);
				}
			}
		});
	}

	// startActivityForResult(credential.newChooseAccountIntent(),
	// REQUEST_CODE_ACCOUNT_PICKER);

}
