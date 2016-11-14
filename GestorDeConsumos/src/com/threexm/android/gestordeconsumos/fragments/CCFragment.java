package com.threexm.android.gestordeconsumos.fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.threexm.android.gestordeconsumos.MyApp;
import com.threexm.android.gestordeconsumos.R;
import com.threexm.android.gestordeconsumos.data.Operation;
import com.threexm.android.gestordeconsumos.log.Logger;

/**
 * Maneja el ingreso del importe en cuenta corriente
 * 
 * @author pablo
 * 
 */
public class CCFragment extends Fragment implements IBackKeyHandler {

	public static final String TAG = "CCFragment";

	Button mBtnCancel, mBtnConfirm;
	EditText mAmount;
	InputMethodManager mInputMethodManager;

	public CCFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				com.threexm.android.gestordeconsumos.R.layout.fragment_cc,
				container, false);

		mBtnCancel = (Button) rootView.findViewById(R.id.btnCancelCC);
		mBtnConfirm = (Button) rootView.findViewById(R.id.btnConfirmCC);
		mAmount = (EditText) rootView.findViewById(R.id.etAmount);

		InputFilter filter = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {

				String toEvaluate = dest.toString() + source;

				Pattern pattern = Pattern.compile("\\d{1,3}(\\.\\d{0,2}){0,1}");
				Matcher matcher = pattern.matcher(toEvaluate);
				boolean valid = matcher.matches();
				Logger.i(TAG, "-" + toEvaluate);
				if (!valid) {
					return "";
				}
				return null;
			}
		};
		mAmount.setFilters(new InputFilter[] { filter });

		mAmount.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() == 0) {
					mBtnConfirm.setEnabled(false);
					return;
				}
				
				try {
					String toEvaluate = s.toString();
					float number = Float.valueOf(toEvaluate);
					if (number > 0) {
						mBtnConfirm.setEnabled(true);
					} else { 
						mBtnConfirm.setEnabled(false);
					}
				} catch (NumberFormatException nfe) {
					mBtnConfirm.setEnabled(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

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

				// Reset the operation
				MyApp.OPERATIONCONTROLLER.resetOperation();

				// Load the options fragment
				OptionsFragment fragment = new OptionsFragment();
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.fragment_container, fragment);
				ft.commit();
			}
		});

		mBtnConfirm.setOnClickListener(new OnClickListener() {
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View v) {

				String currentMoney = mAmount.getText().toString();
//				if (currentMoney.trim().length() == 0) {
//					Toast.makeText(getActivity(),
//							getString(R.string.error_no_money_entered),
//							Toast.LENGTH_LONG).show();
//					return;
//				}

				if (Float.valueOf(currentMoney) <= 0) {
					Toast.makeText(getActivity(),
							getString(R.string.error_no_valid_money_amount),
							Toast.LENGTH_LONG).show();
					return;
				}

				String newMoney = String.format("%.2f",
						Float.parseFloat(currentMoney));
				mAmount.setText(newMoney);

				// Create the operation and set the next state
				Operation operation = MyApp.OPERATIONCONTROLLER.getOperation();
				operation.setState(Operation.State.NFC_READ);
				operation.getExtras().putString(Operation.KEY_MONEY_AMOUNT,
						newMoney);

				ReadingFragment fragment = new ReadingFragment();
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.fragment_container, fragment);
				ft.commit();
			}
		});

		mInputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (mInputMethodManager != null) {
			mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
					0);
		}
		mAmount.requestFocus();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mInputMethodManager != null) {
			mInputMethodManager.hideSoftInputFromWindow(
					mAmount.getWindowToken(), 0);
		}
	}

	@Override
	public void onBackKeyPressed() {
		mBtnCancel.callOnClick();
	}
}
