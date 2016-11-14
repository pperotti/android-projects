package com.threexm.android.gestordeconsumos.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.content.Context;
import android.util.Log;

class SDCardWritter {
	
	private static final String TAG = "SDCardWritter";
	
	private static File mOutputFile = null;
	private static FileOutputStream mFOS = null;
	private static final String LOG_DIR = "/mnt/extSdCard/gdc-logs";
	
	/**
	 * Initialize the sdcard logger 
	 * 
	 * @param context
	 */
	public static void initialize(Context context) {
		
		//Create the directory if it doesn't exists
		File mOutputDir = new File(LOG_DIR);
		if ( mOutputDir.exists() == false ) {
			boolean result = mOutputDir.mkdir();
			if ( result ) {
				Log.i(TAG, "Logs Folder Succesfully Created");
			} else {
				Log.i(TAG, "Logs Folder not created");
				return;
			}
		} else { 
			Log.e(TAG, "Logs Folder exists!");
		}
		
		try {
			mOutputFile = new File(LOG_DIR + File.separatorChar + "logs"+ System.currentTimeMillis() + ".txt");
			if ( mOutputFile.exists() == false ) {
				boolean fileCreated = mOutputFile.createNewFile();
				if ( fileCreated ) { 
					Log.i(TAG, "Log file created!");
				} else {
					Log.i(TAG, "Log file NOT created!");
				}
			} else { 
				Log.i(TAG, "Log file exists!");
			}
			mOutputFile.setWritable(true);
			mFOS = new FileOutputStream(mOutputFile, true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write to file the log message using  
	 */
	public static void write(String tag, String message) {
		Calendar calendar = Calendar.getInstance();
		String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR); 
		String hour = ((calendar.get(Calendar.HOUR_OF_DAY) < 10) ? "0" : "") + calendar.get(Calendar.HOUR_OF_DAY);
		String minutes = ((calendar.get(Calendar.MINUTE) < 10) ? "0" : "") + calendar.get(Calendar.MINUTE);
		String seconds = ((calendar.get(Calendar.SECOND) < 10) ? "0" : "") + calendar.get(Calendar.SECOND);
		String time =  hour + ":" + minutes + ":" + seconds + "." + calendar.get(Calendar.MILLISECOND);
		if ( mOutputFile != null ) {
			try {
				mFOS.write((date + " " + time + " " + tag + " " + message + "\r\n").getBytes());
			} catch (IOException e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Close the logger 
	 */
	public static void close() {
		if ( mOutputFile != null ) { 
			try {
				mFOS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
