package com.threexm.android.gestordeconsumos.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.threexm.android.gestordeconsumos.MyApp;
import com.threexm.android.gestordeconsumos.R;
import com.threexm.android.gestordeconsumos.data.Operation;
import com.threexm.android.gestordeconsumos.log.Logger;

/**
 * Maneja las operaciones NFC y gestiona la UI mostrada durante la comunicacion
 * con el server. Dependiendo si la comunicacion fue existosa o no
 * 
 * @author pablo
 * 
 */
public class ReadingFragment extends Fragment implements IBackKeyHandler,
		INFCHandler, Operation.CompletionListener {

	public static final String TAG = "ReadingFragment";

	Button mBtnCancel;
	ImageView mLogo, mAnimatedLogo;
	TextView mDetailAmount, mDetailConsumption;
	LinearLayout mDetailMoney;
	RelativeLayout mProgress;
	boolean mCanProcessReading = true;

	public ReadingFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				com.threexm.android.gestordeconsumos.R.layout.fragment_reader,
				container, false);

		mBtnCancel = (Button) rootView.findViewById(R.id.btnCancelReaderFR);
		mDetailConsumption = (TextView) rootView
				.findViewById(R.id.tvDetailConsumptionFR);
		mDetailAmount = (TextView) rootView.findViewById(R.id.tvDetailAmountFR);
		mDetailMoney = (LinearLayout) rootView.findViewById(R.id.llDetailMoneyFR);
		mProgress = (RelativeLayout) rootView.findViewById(R.id.rlProgress);
		mProgress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Logger.i(TAG, "Click Captured!");
			}
		});

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Logger.i(TAG, "onActivityCreated");

		mBtnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				MyApp.OPERATIONCONTROLLER.resetOperation();

				OptionsFragment fragment = new OptionsFragment();
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.fragment_container, fragment);
				ft.commit();
			}
		});

//		//This is for test-only purposes
//		mLogo.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				startValidation("ABC");
//			}
//		});

		Operation currentOperation = MyApp.OPERATIONCONTROLLER.getOperation();
		if (Operation.Type.CC.equals(currentOperation.getType())) {
			mDetailMoney.setVisibility(View.VISIBLE);
			mDetailAmount.setText(currentOperation.getExtras().getString(
					Operation.KEY_MONEY_AMOUNT));
		} else if (Operation.Type.DESAYUNO.equals(currentOperation.getType())) {
			mDetailMoney.setVisibility(View.INVISIBLE);
		} else if (Operation.Type.ALMUERZO.equals(currentOperation.getType())) {
			mDetailMoney.setVisibility(View.INVISIBLE);
		}
		mDetailConsumption.setCompoundDrawablesWithIntrinsicBounds(currentOperation.getType().getIconSmall(), 0, 0, 0);
		
		mDetailConsumption.setText(getString(currentOperation.getType().getLabel()));
		
		if ( Operation.State.VALIDATION_IN_PROGRESS.equals(currentOperation.getState()) ) {
			mProgress.setVisibility(View.VISIBLE);
			mCanProcessReading = false;
		}
	}

	@Override
	public void onBackKeyPressed() {
		if ( mProgress.getVisibility() != View.VISIBLE) { 
			mBtnCancel.callOnClick();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		MyApp.OPERATIONCONTROLLER.getOperation().addCompletionListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		MyApp.OPERATIONCONTROLLER.getOperation().removeCompletionListener(this);
	}

	@Override
	public void handleNfcIntent(Intent intent) {

		//Make sure that it is safe to continue
		if ( mCanProcessReading == false ) {
			Logger.i(TAG, "No more lectures available for this fragment!");
			return;
		}
		
		String action = intent.getAction();
		Logger.i(TAG, "Action=" + action);
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

			Parcelable parcelableExtra = intent
					.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			if (parcelableExtra != null) {
				Tag tag = (Tag) parcelableExtra;
				byte[] id = tag.getId();
				String tagID = getHex(id);
				Logger.i(TAG, "Tag=>" + tagID);
//				Toast.makeText(getActivity(), "ID: " + tagID, Toast.LENGTH_LONG)
//						.show();
				
				//Disable other reads
				mCanProcessReading = false;
				
				//Start the actual validation with the backend				
				startValidation(tagID);
			} else {
				Logger.i(TAG, "No data available!");
			}
		}
	}

	private String getHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = bytes.length - 1; i >= 0; --i) {
			int b = bytes[i] & 0xff;
			if (b < 0x10)
				sb.append('0');
			sb.append(Integer.toHexString(b));
			/*
			 * if (i > 0) { sb.append("."); }
			 */
		}
		return sb.toString();
	}

	@Override
	public void onSuccess() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mProgress.setVisibility(View.GONE);
				ResultOkFragment fragment = new ResultOkFragment();
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.fragment_container, fragment);
				ft.commit();
			}
		});

	}

	@Override
	public void onError() {
		
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Logger.e(TAG, "onError!");
				mProgress.setVisibility(View.GONE);
				ResultErrorFragment fragment = new ResultErrorFragment();
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.fragment_container, fragment);
				ft.commit();
			}
		});
		
	}

	private void startValidation(String tagID) {
		mBtnCancel.setEnabled(false);
		mProgress.setVisibility(View.VISIBLE);
		MyApp.OPERATIONCONTROLLER.validate(getActivity(), tagID, getIMEI());
	}

	private String getIMEI() {
		TelephonyManager tm = (TelephonyManager) getActivity()
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
}
