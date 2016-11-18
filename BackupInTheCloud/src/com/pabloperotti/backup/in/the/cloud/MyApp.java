package com.pabloperotti.backup.in.the.cloud;

import com.google.android.gms.auth.GoogleAuthUtil;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MyApp extends Application {

	public static final String TAG = "BackupInTheCloud";
	public static MyApp INSTANCE = null;
	public static final AccountInfo ACCOUNTINFO = new AccountInfo();

	private SharedPreferences sharedPreferences = null;
	public enum SharedPreferencesProperties {
		LASTSYNC;
	}


	public void onCreate() {
		Log.i(TAG, "onCreate");

		INSTANCE = this;

		ACCOUNTINFO.setSelectedAccount(getGoogleAccount());

		sharedPreferences = getSharedPreferences("BITCSP", Context.MODE_PRIVATE);
	}

	public void onTerminate() {
		Log.i(TAG, "onTerminate");
	}

	public SharedPreferences getSharedPreferences() {
		return sharedPreferences;
	}

	private String getGoogleAccount() {
		String singleAccount = null;

		AccountManager mAccountManager = AccountManager.get(this);
	    Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
	    if ( accounts.length > 0 ) {
	    	singleAccount = accounts[0].name;;
	    }

		return singleAccount;
	}

}
