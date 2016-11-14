package com.threexm.android.gestordeconsumos;

import android.app.Application;
import android.content.res.Resources;

import com.threexm.android.gestordeconsumos.data.OperationController;
import com.threexm.android.gestordeconsumos.log.Logger;

public class MyApp extends Application {

	public static final String TAG = "MyApp";
	
	public static final OperationController OPERATIONCONTROLLER = new OperationController();
	
	public MyApp() {
		
	}
	
	public void onCreate() {
		
		Resources resources = getResources();
		
		Logger.initialize(
				getApplicationContext(),
				resources.getBoolean(R.bool.debug_enabled), 
				resources.getBoolean(R.bool.info_enabled), 
				resources.getBoolean(R.bool.error_enabled));
		
		Logger.d(TAG, "onCreate");
	}
	
	public void onTerminate() {
		Logger.d(TAG, "onTerminate");

		Logger.close();
	}
	
}
