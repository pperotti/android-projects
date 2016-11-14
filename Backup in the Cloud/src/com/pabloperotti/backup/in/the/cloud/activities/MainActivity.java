package com.pabloperotti.backup.in.the.cloud.activities;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.pabloperotti.backup.in.the.cloud.Constants;
import com.pabloperotti.backup.in.the.cloud.DriveUtils;
import com.pabloperotti.backup.in.the.cloud.MyApp;
import com.pabloperotti.backup.in.the.cloud.MyApp.SharedPreferencesProperties;
import com.pabloperotti.backup.in.the.cloud.R;

public class MainActivity extends Activity {

	private TextView txtSelectedAccount;
	private Button btnListFiles, btnManualSync, btnSync;
	private static Drive service;
	private GoogleAccountCredential credential;

	// REFACTOR: this variable shall be part of Sync Task
	public static String initialDateTime = null;

	public static int REQUEST_CODE_ACCOUNT_PICKER = 0x1000;
	public static int REQUEST_CODE_ID_IO_USER_AUTH = 0x1001;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtSelectedAccount = (TextView) findViewById(R.id.txtSelectedAccount);
		btnListFiles = (Button) findViewById(R.id.button_list_files);
		btnListFiles.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnListFiles.setEnabled(false);
				listFiles();
			}
		});
		btnManualSync = (Button) findViewById(R.id.button_manual_sync);
		btnManualSync.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// new
				// CreateFolderIfDoesntExists().execute("Backup in the Cloud");
				new SyncFiles().execute(Constants.DEV_FOLDER_NAME);
			}
		});

		btnSync = (Button) findViewById(R.id.button_sync);
		btnSync.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(MyApp.TAG, "MainActivity->Sync!");

				// Get Backup in the Cloud account
				AccountManager am = AccountManager.get(MainActivity.this);
				Account[] accounts = am
						.getAccountsByType(Constants.ACCOUNT_TYPE);
				if (accounts.length > 0) {
					Account account = accounts[0];

					Bundle extras = new Bundle();
					extras.putBoolean("worked", true);

					// Ask for sync
					ContentResolver.requestSync(account, Constants.AUTHORITY,
							extras);
				}

			}
		});

		txtSelectedAccount
				.setText(getString(R.string.txt_selected_account, "-"));

		credential = GoogleAccountCredential.usingOAuth2(this,
				DriveScopes.DRIVE);

		startActivityForResult(credential.newChooseAccountIntent(),
				REQUEST_CODE_ACCOUNT_PICKER);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(MyApp.TAG, "onResume()");
		Log.i(MyApp.TAG,
				"isGooglePlayServicesAvailable()=>"
						+ (GooglePlayServicesUtil
								.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (REQUEST_CODE_ACCOUNT_PICKER == requestCode
				&& RESULT_OK == resultCode) {
			String accountName = data
					.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

			if (accountName != null) {

				credential.setSelectedAccountName(accountName);
				service = getDriveService(credential);

				txtSelectedAccount.setText(getString(
						R.string.txt_selected_account, accountName));
				btnListFiles.setEnabled(true);
				btnManualSync.setEnabled(true);
			}

			Log.i("MainActivity", "AccountName=" + accountName);
			Toast.makeText(this, "AccountName=" + accountName,
					Toast.LENGTH_LONG).show();
		} else if (REQUEST_CODE_ID_IO_USER_AUTH == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				listFiles();
			} else {
				startActivityForResult(credential.newChooseAccountIntent(),
						REQUEST_CODE_ACCOUNT_PICKER);
			}
		}
	}

	private Drive getDriveService(GoogleAccountCredential credential) {
		return new Drive.Builder(AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), credential).build();
	}

	private void listFiles() {
		ListFiles listFiles = new ListFiles();
		listFiles.execute();
	}

	/**
	 * List available files on Drive from the selected google account
	 *
	 * @author XGPB43
	 *
	 */
	class ListFiles extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			try {
				List<File> result = DriveUtils.retrieveAllFiles(service);
				Log.i(MyApp.TAG, "Getting Drive Done ...");
				Iterator<File> iterator = result.iterator();
				while (iterator.hasNext()) {
					File current = iterator.next();
					Log.i(MyApp.TAG,
							"FILE=>" + current.getTitle() + " kind="
									+ current.getKind() + " mimeType="
									+ current.getMimeType());
				}
			} catch (UserRecoverableAuthIOException e) {
				startActivityForResult(e.getIntent(),
						REQUEST_CODE_ID_IO_USER_AUTH);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(MyApp.TAG, "File Retrieval failed " + e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Enable the ui buttons for Token retrieval
			btnListFiles.setEnabled(true);
		}

	}

	/**
	 * Sync with Google Drive for the selected files
	 *
	 * Algorithm --------- -) Check whether the "Backup in the Cloud" folder
	 * exists in local storage YES) Continue NO) Create it. Check whether the
	 * creation was successful YES) Continue NO) Stop Sync
	 *
	 * 1) Check whether the "Backup in the Cloud" folder exists in the cloud
	 * YES) 1.1) Get the changes from the last sync. (IDs will be used for the
	 * name of local files) 1.2) Check whether there are changes to download
	 * YES) Download changes & update file content. NO) Do nothing NO) Create
	 * "Backup in the Cloud" folder in the cloud
	 *
	 * 2) Check whether there is a file to upload YES) Upload it NO) Do nothing
	 *
	 * @author XGPB43
	 *
	 */
	class SyncFiles extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(final String... params) {

			Log.i(MyApp.TAG, "Sync Process Started!!!!");

			// Check whether the root folder for backup in the cloud storage
			// exists in SD Card
			String outputFolderName = params[0];
			boolean isLocalFolderReady = isLocalFolderReady(outputFolderName);
			if (isLocalFolderReady == false) {
				return null;
			}

			// 1) Check whether the specified older exists
			Log.i(MyApp.TAG, "Folder Name=>" + params[0]);
			Log.i(MyApp.TAG, "1978=>" + DriveUtils.getInitialTimeInMillis());

			// Get moment where the sync will occur

			SharedPreferences sp = MyApp.INSTANCE.getSharedPreferences();

			long syncTimeInMillis = System.currentTimeMillis();
			long lastSyncTimeInMillis = sp.getLong(
					SharedPreferencesProperties.LASTSYNC.name(),
					DriveUtils.getInitialTimeInMillis());
			//long lastSyncTimeInMillis = DriveUtils.getInitialTimeInMillis();
			Log.i(MyApp.TAG, "LAST SYNC=" + lastSyncTimeInMillis + " HUMAN="
					+ new Date(lastSyncTimeInMillis));

			// Create the folder only if does not exists
			String folderId = DriveUtils.folderExists(service, params[0]);
			Log.i(MyApp.TAG, "FOLDER ID =>" + folderId);
			if (folderId != null) {
				// 1.1 & 1.2
				retrieveRemoteChanges(folderId, outputFolderName,
						lastSyncTimeInMillis);
			} else {
				DriveUtils.createFolder(service, params[0]);
			}

			// 2) Check whether there is a file to upload
			uploadLocalChanges(folderId, outputFolderName, lastSyncTimeInMillis, syncTimeInMillis);

			// 3) Save the sync moment.
			SharedPreferences.Editor editor = sp.edit();
			editor.putLong(SharedPreferencesProperties.LASTSYNC.name(),
					System.currentTimeMillis());
			editor.commit();

			return null;
		}

		private void uploadLocalChanges(String folderId,
				String outputFolderName, long lastSyncTimeInMillis, long syncTimeInMillis) {

			Log.i(MyApp.TAG, "Local changes upload process started!!!!");

			// Search for files in local storage whose modification date is
			// after "lastSyncDate"
			java.io.File directoryPath = new java.io.File(
					getFullPath(outputFolderName));
			if (directoryPath.isDirectory()) {
				java.io.File[] contents = directoryPath.listFiles();
				for (int i = 0; i < contents.length; i++) {
					java.io.File currentFile = contents[i];
					Log.i(MyApp.TAG,
							"Name=" + currentFile.getName() + " LastModified=>"
									+ currentFile.lastModified()
									+ " LastModifiedDate="
									+ new Date(currentFile.lastModified()));


					if (currentFile.lastModified() > lastSyncTimeInMillis
							&& currentFile.lastModified() < syncTimeInMillis) {
						DriveUtils.insertFile(service, currentFile, folderId);
					}
				}
			}

		}

		private void retrieveRemoteChanges(String folderId,
				String outputFolderName, long currentTimeInMillis) {

			Log.i(MyApp.TAG, "Remote changes retrieval process started!!!!");

			// Get recent changes
			List<File> changes = DriveUtils.retrieveChangesFromFolder(service,
					folderId, initialDateTime);
			DriveUtils.printFileList(changes);

			CharSequence date = DateFormat.format("yyyy-MM-ddThh:mm:ssz",
					currentTimeInMillis);

			int changesNumber = changes.size();

			Log.i(MyApp.TAG, "TIME=>" + date + " NUMBER OF CHANGES="
					+ changesNumber);

			initialDateTime = date.toString();

			if (changesNumber > 0) {
				// Download all files
				// DriveUtils.downloadFiles(service, changes, outputFolderName);
			}

		}

		private String getFullPath(String targetFolder) {
			return Constants.BASE_BACKUP_FOLDER + java.io.File.separator
					+ targetFolder;
		}

		private boolean isLocalFolderReady(String targetFolder) {
			boolean isLocalFolderReady = false;

			// Check from the external storage availability and targetFolder
			// String folderPath = Environment.getExternalStorageDirectory().i +
			// java.io.File.separator + Constants.BASE_BACKUP_FOLDER;
			String folderPath = getFullPath(targetFolder);

			java.io.File directory = new java.io.File(folderPath);
			Log.i(MyApp.TAG, "FOLDER PATH=>" + directory.getAbsolutePath()
					+ " Exists?=>" + directory.exists() + " Can Write="
					+ directory.canWrite());
			if (directory.exists() == false) {
				boolean isDirCreated = directory.mkdirs();
				Log.i(MyApp.TAG, "EXISTS?=>FALSE / IS DIR CREATED?=>"
						+ isDirCreated);
				isLocalFolderReady = isDirCreated;
			} else {
				Log.i(MyApp.TAG, "EXISTS?=>TRUE YOU CAN NOW PROCEED");
				isLocalFolderReady = true;
			}

			return isLocalFolderReady;
		}
	}
}
