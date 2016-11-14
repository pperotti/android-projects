package com.threexm.android.gestordeconsumos.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Window;

import com.threexm.android.gestordeconsumos.MyApp;
import com.threexm.android.gestordeconsumos.R;
import com.threexm.android.gestordeconsumos.data.Operation;
import com.threexm.android.gestordeconsumos.fragments.CCFragment;
import com.threexm.android.gestordeconsumos.fragments.IBackKeyHandler;
import com.threexm.android.gestordeconsumos.fragments.INFCHandler;
import com.threexm.android.gestordeconsumos.fragments.OptionsFragment;
import com.threexm.android.gestordeconsumos.fragments.ReadingFragment;
import com.threexm.android.gestordeconsumos.fragments.ResultErrorFragment;
import com.threexm.android.gestordeconsumos.fragments.ResultOkFragment;
import com.threexm.android.gestordeconsumos.log.Logger;

public class MainActivity extends Activity {

	public static final String TAG = "MainActivity";

	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private Operation.State mOperationStateWhenPause = Operation.State.NONE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);

		Logger.d(TAG, "onCreate");
		
		mAdapter = NfcAdapter.getDefaultAdapter(this);

		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		//Get the metrics
		Logger.i(TAG, "WidthPixels=" + getResources().getDisplayMetrics().widthPixels);
		Logger.i(TAG, "HeightPixels=" + getResources().getDisplayMetrics().heightPixels);
		Logger.i(TAG, "DensityDpi=" + getResources().getDisplayMetrics().densityDpi);
		Logger.i(TAG, "XDPI=" + getResources().getDisplayMetrics().xdpi);
		Logger.i(TAG, "YDPI=" + getResources().getDisplayMetrics().ydpi);
		
		
	}

	@Override
	public void onResume() {
		super.onResume();

		Logger.d(TAG, "onResume()");

		/**
		 * Load the appropriate fragment according to the state
		 */
		Operation currentOperation = MyApp.OPERATIONCONTROLLER.getOperation();
		Fragment fragment = null;
		if (Operation.Type.NONE.equals(currentOperation.getType())) {
			fragment = new OptionsFragment();
		} else {
			// Validate the STATE to analyze what to display. Each fragment
			// should check the internal state according to the state
			Operation.State currentState = currentOperation.getState();
			Logger.i(TAG, "mOperationStateWhenPause=" + mOperationStateWhenPause.name() + " current=" + currentState.name());
			if (Operation.State.NONE.equals(currentState)) {
				// Do nothing
			} else if (Operation.State.NFC_READ.equals(currentState) && Operation.State.NFC_READ.equals(mOperationStateWhenPause) == false ) {
				fragment = new ReadingFragment();
			} else if (Operation.State.VALIDATION_IN_PROGRESS.equals(currentState) && Operation.State.VALIDATION_IN_PROGRESS.equals(mOperationStateWhenPause) == false ) {
				fragment = new ReadingFragment();
			} else if (Operation.State.MONEY_ENTRY.equals(currentState) && Operation.State.MONEY_ENTRY.equals(mOperationStateWhenPause) == false ) {
				fragment = new CCFragment();
			} else if (Operation.State.RESULT_OK.equals(currentState) && Operation.State.RESULT_OK.equals(mOperationStateWhenPause) == false ) {
				fragment = new ResultOkFragment();
			} else if (Operation.State.RESULT_ERROR.equals(currentState) && Operation.State.RESULT_OK.equals(mOperationStateWhenPause) == false ) {
				fragment = new ResultErrorFragment();
			}
		}

		if ( fragment != null ) { 
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.fragment_container, fragment);
			ft.commit();
		}
		
		mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
	}

	@Override
	public void onPause() {
		super.onPause();

		Logger.d(TAG, "onPause");
		
		mOperationStateWhenPause = MyApp.OPERATIONCONTROLLER.getOperation().getState();

		mAdapter.disableForegroundDispatch(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		Logger.d(TAG, "onConfigurationChanged!");
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		Logger.i(TAG, "onNewIntent");
		Fragment currentFragment = getFragmentManager().findFragmentById(
				R.id.fragment_container);
		if (currentFragment instanceof INFCHandler ) {
			((INFCHandler) currentFragment).handleNfcIntent(intent);
		}

	}

	@Override
	public void onBackPressed() {
		Fragment currentFragment = getFragmentManager().findFragmentById(
				R.id.fragment_container);
		if (currentFragment instanceof IBackKeyHandler) {
			((IBackKeyHandler) currentFragment).onBackKeyPressed();
		}
	}
}
