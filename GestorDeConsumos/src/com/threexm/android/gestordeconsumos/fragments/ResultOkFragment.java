package com.threexm.android.gestordeconsumos.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.threexm.android.gestordeconsumos.MyApp;
import com.threexm.android.gestordeconsumos.R;
import com.threexm.android.gestordeconsumos.data.Operation;
import com.threexm.android.gestordeconsumos.data.OperationResult;
import com.threexm.android.gestordeconsumos.log.Logger;

/**
 * Muestra el resultado exitoso junto con el nombre y detalle de que operacion
 * se realizo.
 * 
 * @author pablo
 * 
 */
public class ResultOkFragment extends Fragment implements IBackKeyHandler {

	public static final String TAG = "ResultOkFragment";

	Button mBtnVolver;
	TextView mDetailName, mDetailConsumption, mDescription;

	public ResultOkFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(
						com.threexm.android.gestordeconsumos.R.layout.fragment_operation_ok,
						container, false);

		mBtnVolver = (Button) rootView.findViewById(R.id.btnVolver);
		mDescription = (TextView) rootView.findViewById(R.id.txtDescription);
		mDetailName = (TextView) rootView.findViewById(R.id.tvDetailName);
		mDetailConsumption = (TextView) rootView
				.findViewById(R.id.tvDetailConsumption);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Logger.i(TAG, "onActivityCreated");

		mBtnVolver.setOnClickListener(new OnClickListener() {
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

		// Update the data according the operation data
		Operation currentOperation = MyApp.OPERATIONCONTROLLER.getOperation();
		mDetailConsumption.setCompoundDrawablesWithIntrinsicBounds(currentOperation.getType().getIconSmall(), 0, 0, 0); 
		mDetailConsumption.setText(currentOperation.getType().getLabel());
		mDetailName.setText(currentOperation.getOperationResult().getExtras()
				.getString(OperationResult.KEY_USUARIO));
		mDescription.setText(currentOperation.getOperationResult().getExtras()
				.getString(OperationResult.KEY_DESCRIPTION));
	}

	@Override
	public void onBackKeyPressed() {
		mBtnVolver.callOnClick();
	}
}
