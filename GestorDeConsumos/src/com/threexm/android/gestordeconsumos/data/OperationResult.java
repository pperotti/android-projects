package com.threexm.android.gestordeconsumos.data;

import android.os.Bundle;

/**
 * Representa un resultado de diferente tipo. La data recibida es populada
 * en un Bundle
 * 
 * @author pperotti
 */
public class OperationResult {
	
	static enum Type {
		OK, ERROR
	};

	public static final String KEY_MONEY_AMOUNT = "key_money_amount";
	public static final String KEY_USUARIO = "key_usuario";
	public static final String KEY_DESCRIPTION = "key_description";
	
	private Type mType;
	private Bundle mData = new Bundle();
	
	public OperationResult(OperationResult.Type type) {
		mType = type;
	}
		
	public Bundle getExtras() {
		return mData;
	}
	
	public Type getType() {
		return mType;
	}
}
