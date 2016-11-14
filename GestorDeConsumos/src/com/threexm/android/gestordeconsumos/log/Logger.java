package com.threexm.android.gestordeconsumos.log;

import android.content.Context;
import android.util.Log;

public class Logger {

	// By default, only info and error will be enabled
	private static ILogger mDebug = new EmptyLogger();
	private static ILogger mInfo = new EmptyLogger();
	private static ILogger mError = new EmptyLogger();

	// Initialization
	public static void initialize(Context context, boolean debugEnabled,
			boolean infoEnabled, boolean errorEnabled) {
		if (debugEnabled) {
			mDebug = new DebugLogger();
		}

		if (infoEnabled) {
			mInfo = new InfoLogger();
		}

		if (errorEnabled) {
			mError = new ErrorLogger();
		}
		SDCardWritter.initialize(context);
	}

	public static void d(String tag, String message) {
		mDebug.execute(tag, message);
	}

	public static void i(String tag, String message) {
		mInfo.execute(tag, message);
	}

	public static void e(String tag, String message) {
		mError.execute(tag, message);
	}

	public static void close() {
		SDCardWritter.close();
	}
}

interface ILogger {
	void execute(String TAG, String message);
}

class DebugLogger implements ILogger {
	public void execute(String TAG, String message) {
		Log.d(TAG, message);
		SDCardWritter.write(TAG, message);
	}
}

class InfoLogger implements ILogger {
	public void execute(String TAG, String message) {
		Log.i(TAG, message);
		SDCardWritter.write(TAG, message);
	}
}

class ErrorLogger implements ILogger {
	public void execute(String TAG, String message) {
		Log.e(TAG, message);
		SDCardWritter.write(TAG, message);
	}
}

class EmptyLogger implements ILogger {
	public void execute(String TAG, String message) {
	}
}