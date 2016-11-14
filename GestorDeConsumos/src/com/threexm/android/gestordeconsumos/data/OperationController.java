package com.threexm.android.gestordeconsumos.data;

import java.util.ArrayList;

import com.threexm.android.gestordeconsumos.log.Logger;

import android.content.Context;

/**
 * Gestiona las operaciones en curso. Actualmente, la UI permite 1 sola a la
 * vez.
 * 
 * @author pperotti
 * 
 */
public class OperationController {

	private static String TAG = "OperationController";
	
	private Operation mCurrentOperation = new Operation(Operation.Type.NONE);
	
	//Holds records for all the operations handled during this session
	private ArrayList<Long> mRecentOperations = new ArrayList<Long>();

	public Operation newOperation(Operation.Type type) {
		mCurrentOperation = new Operation(type);
		Logger.i(TAG, "New Operation: Token=" + mCurrentOperation.getUniqueToken());
		return mCurrentOperation;
	}

	public Operation getOperation() {
		return mCurrentOperation;
	}

	public void resetOperation() {
		mCurrentOperation = new Operation(Operation.Type.NONE);
	}
	
	public void validate(Context context, String tagID, String imei) {
		
		Logger.i(TAG, "Validation started! Token=" + mCurrentOperation.getUniqueToken() 
				+ " Type="+ mCurrentOperation.getType().name() 
				+ " " + mCurrentOperation.getExtras() 
				);
		
		//If current operation is in the recent operation list, then do not continue 
		if ( mRecentOperations.contains(mCurrentOperation.getUniqueToken()) ) { 
			//Ignore it, just log the data
			Logger.i(TAG, "Token already in recent operations");
		} else {
			Logger.i(TAG, "Token clear! We are go to proceed!");
			mRecentOperations.add(mCurrentOperation.getUniqueToken());
		}
		
		mCurrentOperation.getExtras().putString(Operation.KEY_IMEI, imei);
		mCurrentOperation.getExtras().putString(Operation.KEY_TAG_ID, tagID);
		mCurrentOperation.setState(Operation.State.VALIDATION_IN_PROGRESS);
		new Validator(context, mCurrentOperation).execute();
	}
}
