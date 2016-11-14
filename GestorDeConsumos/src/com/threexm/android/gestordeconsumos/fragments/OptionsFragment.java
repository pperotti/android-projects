package com.threexm.android.gestordeconsumos.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.threexm.android.gestordeconsumos.MyApp;
import com.threexm.android.gestordeconsumos.R;
import com.threexm.android.gestordeconsumos.data.Operation;
import com.threexm.android.gestordeconsumos.log.Logger;

/**
 * Presenta las opciones de menu
 * 
 * @author pablo
 * 
 */
public class OptionsFragment extends Fragment implements IBackKeyHandler {

	public static final String TAG = "OptionsFragment";

	private Button mBtnDesayuno, mBtnAlmuerzo, mBtnCC;

	public OptionsFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				com.threexm.android.gestordeconsumos.R.layout.fragment_options,
				container, false);

		mBtnDesayuno = (Button) rootView.findViewById(R.id.btnDesayuno);
		mBtnAlmuerzo = (Button) rootView.findViewById(R.id.btnAlmuerzo);
		mBtnCC = (Button) rootView.findViewById(R.id.btnCC);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Logger.i(TAG, "onActivityCreated");

		mBtnDesayuno.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				
				//Create the operation and set the next state
				MyApp.OPERATIONCONTROLLER.newOperation(Operation.Type.DESAYUNO)
						.setState(Operation.State.NFC_READ);

				//Load the proper fragment
				ReadingFragment fragment = new ReadingFragment();
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.fragment_container, fragment);
				ft.addToBackStack(null);
				ft.commit();
			}
		});
		mBtnAlmuerzo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//Create the operation and set the next state
				MyApp.OPERATIONCONTROLLER.newOperation(Operation.Type.ALMUERZO)
						.setState(Operation.State.NFC_READ);
				
				ReadingFragment fragment = new ReadingFragment();
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.fragment_container, fragment);
				ft.addToBackStack(null);
				ft.commit();
			}
		});
		mBtnCC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//Create the operation and set the next state
				MyApp.OPERATIONCONTROLLER.newOperation(Operation.Type.CC)
						.setState(Operation.State.MONEY_ENTRY);
				
				//load the proper fragment
				CCFragment fragment = new CCFragment();
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.fragment_container, fragment);
				ft.commit();
			}
		});
	}

	@Override
	public void onBackKeyPressed() {
		getActivity().finish();
	}
}
