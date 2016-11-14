package com.threexm.android.gestordeconsumos.data;

import java.util.Random;

import android.os.Bundle;

import com.threexm.android.gestordeconsumos.MyApp;
import com.threexm.android.gestordeconsumos.R;
import com.threexm.android.gestordeconsumos.log.Logger;

public class Operation {

	public interface CompletionListener {
		void onSuccess();

		void onError();
	}

	public enum Type {
		NONE("none", 0, 0, 0), DESAYUNO("DS", R.string.operation_ds,
				R.drawable.ic_menu_ds, R.drawable.ic_ds_small), ALMUERZO("AL",
				R.string.operation_al, R.drawable.ic_menu_al,
				R.drawable.ic_al_small), CC("CC", R.string.operation_cc,
				R.drawable.ic_menu_cc, R.drawable.ic_cc_small);

		private String mKey = null;
		private int mLabel = 0;
		private int mIcon = 0;
		private int mIconSmall = 0;

		Type(String key, int label, int icon, int iconSmall) {
			mKey = key;
			mLabel = label;
			mIcon = icon;
			mIconSmall = iconSmall;
		}

		public String getKey() {
			return mKey;
		}

		public int getIcon() {
			return mIcon;
		}

		public int getIconSmall() {
			return mIconSmall;
		}

		public int getLabel() {
			return mLabel;
		}
	};

	public enum State {
		NONE, NFC_READ, RESULT_OK, VALIDATION_IN_PROGRESS, RESULT_ERROR, MONEY_ENTRY;
	};

	public static final String KEY_MONEY_AMOUNT = "key_money_amount";
	public static final String KEY_IMEI = "key_imei";
	public static final String KEY_TAG_ID = "key_tag_id";

	private Type mCurrentType = Type.NONE;
	private State mCurrentState = State.NONE;
	private Bundle mExtras = new Bundle();
	private OperationResult mOperationResult;
	private CompletionListener mCompletionListener;

	/*
	 * This will identify the operation as unique. It will be used to avoid
	 * creating 2 validation activities that matches the same unique token. 
	 */
	private Long mUniqueToken = (new Random(System.currentTimeMillis()))
			.nextLong();

	public Operation(Type newType) {
		mCurrentType = newType;
	}

	public Type getType() {
		return mCurrentType;
	}

	public State getState() {
		return mCurrentState;
	}

	public void setState(State newState) {
		mCurrentState = newState;
	}

	public Bundle getExtras() {
		return mExtras;
	}

	public Long getUniqueToken() {
		return mUniqueToken;
	}
	
	public CompletionListener getCompletionListener() {
		return mCompletionListener;
	}

	public OperationResult getOperationResult() {
		return mOperationResult;
	}

	public void setOperationResult(OperationResult operationResult) {
		mOperationResult = operationResult;

		if (OperationResult.Type.OK == operationResult.getType()) {
			mCurrentState = State.RESULT_OK;
			if (mCompletionListener != null) {
				mCompletionListener.onSuccess();
			}
		} else if (OperationResult.Type.ERROR == operationResult.getType()) {
			mCurrentState = State.RESULT_ERROR;
			if (mCompletionListener != null) {
				mCompletionListener.onError();
			}
		}

		Logger.i("SET OPERATION RESULT",
				"RESULT TYPE="
						+ operationResult.getType()
						+ " currentState="
						+ mCurrentState.name()
						+ " currentOperationState="
						+ MyApp.OPERATIONCONTROLLER.getOperation().getState()
								.name());
	}

	public void addCompletionListener(CompletionListener completionListener) {
		mCompletionListener = completionListener;
	}

	public void removeCompletionListener(CompletionListener completionListener) {
		mCompletionListener = null;
	}

}
