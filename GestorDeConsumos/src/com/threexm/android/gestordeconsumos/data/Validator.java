package com.threexm.android.gestordeconsumos.data;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.threexm.android.gestordeconsumos.R;
import com.threexm.android.gestordeconsumos.data.OperationResult.Type;
import com.threexm.android.gestordeconsumos.log.Logger;

public class Validator extends AsyncTask<Void, Void, Void> {

	private final String TAG = "Validator";

	private final String VALIDATOR_SERVICE = "http://consumos.clinicareinafabiola.com.ar:8888/GestionDeConsumos/api/Consumos";
	//private final String VALIDATOR_SERVICE = "http://3xmgroup.us.to/GestionDeConsumos/api/Consumos";
	private final String HEADER_CONTENT_TYPE = "Content-Type";
	private final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private final String JSON_FIELD_IMEI = "IMEI";
	private final String JSON_FIELD_TARJETA = "Tarjeta";
	private final String JSON_FIELD_TIPO_CONSUMO = "TipoDeConsumo";
	private final String JSON_FIELD_IMPORTE_CUENTA_CORRIENTE = "ImporteCuentaCorriente";
	private final String JSON_FIELD_NOMBRE_USUARIO = "NombreUsuario";
	private final String JSON_FIELD_MENSAJE = "Mensaje";

	// Internal fields
	private Operation mOperation;
	private Context mContext;

	public Validator(Context context, Operation operation) {
		mOperation = operation;
		mContext = context;
	}

	@Override
	protected Void doInBackground(Void... params) {

//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		if (testService()) {
			sendExpense();
		} else {
			Logger.e(TAG, "Service NOT available!");
		}

		Logger.i(TAG, "Validation Finished!");

		return null;
	}

	private boolean testService() {

		boolean isAvailable = false;
		Logger.i(TAG, "Testing Service...");

		HttpGet request = new HttpGet(VALIDATOR_SERVICE);

		// Execute the request
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);
		DefaultHttpClient client = new DefaultHttpClient(httpParams);

		try {
			HttpResponse response = client.execute(request);
			Logger.i(TAG, "RESPONSE=>" + response.getStatusLine().getStatusCode());
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				isAvailable = true;
			} else {
				setErrorResult(mContext
						.getString(R.string.error_ping_missbehaving));
			}
		} catch (IOException e) {
			Logger.i(TAG, e.toString());
			setErrorResult(mContext.getString(R.string.error_cannot_connect));
		}

		return isAvailable;
	}

	private void sendExpense() {

		Logger.i(TAG, "Send Expense...");

		// Create the request
		HttpPost request = new HttpPost(VALIDATOR_SERVICE);

		request.addHeader(HEADER_CONTENT_TYPE, "application/json");
		request.addHeader(HEADER_ACCEPT_ENCODING, "gzip,deflate,sdch");

		// Create body
		String jsonRequest = createJson();
		if (jsonRequest == null) {
			return;
		}

		try {
			request.setEntity(new StringEntity(jsonRequest));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// Execute the request
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);
		DefaultHttpClient client = new DefaultHttpClient(httpParams);

		try {
			HttpResponse response = client.execute(request);
			Logger.i(TAG, "RESPONSE=>" + response.getStatusLine().getStatusCode());
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				// OK
				parseOK(response);
			} else {
				// Error
				parseError(response);
			}

		} catch (IOException e) {
			Logger.i(TAG, e.toString());
			setErrorResult(mContext.getString(R.string.error_cannot_connect));
		} catch (JSONException e) {
			Logger.i(TAG, e.toString());
			setErrorResult(mContext.getString(R.string.error_json_broken));
		}

	}

	@SuppressLint("DefaultLocale")
	private String createJson() {
		JSONObject body = new JSONObject();
		try {
			Bundle extras = mOperation.getExtras();

			body.put(JSON_FIELD_IMEI, extras.getString(Operation.KEY_IMEI)
					.toUpperCase());
			body.put(JSON_FIELD_TARJETA, extras.getString(Operation.KEY_TAG_ID)
					.toUpperCase());
			body.put(JSON_FIELD_TIPO_CONSUMO, mOperation.getType().getKey());

			// Add details only if that makes sense
			if (Operation.Type.CC == mOperation.getType()) {
				String money = extras.getString(Operation.KEY_MONEY_AMOUNT);

				money = money.replace(",", ".");
				
				body.put(JSON_FIELD_IMPORTE_CUENTA_CORRIENTE,
						Double.parseDouble(money));
			}

			Logger.i(TAG, "REQUEST BODY=" + body.toString());
			return body.toString();

		} catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;

		}
	}

	private void setErrorResult(String errorDescription) {
		setErrorResult(errorDescription, null);
	}

	private void setErrorResult(String errorDescription, String userName) {
		OperationResult operationResult = new OperationResult(Type.ERROR);
		operationResult.getExtras().putString(OperationResult.KEY_DESCRIPTION,
				errorDescription);
		if (userName != null) {
			operationResult.getExtras().putString(OperationResult.KEY_USUARIO,
					userName);
		}
		mOperation.setOperationResult(operationResult);
	}

	private void parseOK(HttpResponse response) throws JSONException {
		/*
		 * {"Mensaje":"Consumo registrado con éxito","NombreUsuario":"PEROTTI Pablo"
		 * }
		 */
		if (response != null) {

			HttpEntity bodyEntity = response.getEntity();
			InputStream is;
			try {
				is = bodyEntity.getContent();
				String body = readContent(is);
				Logger.i(TAG, "RESPONSE OK -> Content Length=" + bodyEntity.getContentLength()
						+ " Content=>" + body);

				// Read
				if (body != null) {
					OperationResult operationResult = new OperationResult(
							OperationResult.Type.OK);
					JSONObject bodyObject = new JSONObject(body);
					String userName = bodyObject
							.getString(JSON_FIELD_NOMBRE_USUARIO);
					operationResult.getExtras().putString(
							OperationResult.KEY_USUARIO, userName);

					String mensaje = bodyObject.getString(JSON_FIELD_MENSAJE);
					operationResult.getExtras().putString(
							OperationResult.KEY_DESCRIPTION, mensaje);

					Logger.i(TAG, "ResponseOK=>" + body);

					mOperation.setOperationResult(operationResult);
				}

			} catch (IllegalStateException e) {
				setErrorResult(mContext.getString(R.string.error_json_broken));
			} catch (IOException e) {
				setErrorResult(mContext
						.getString(R.string.error_cannot_connect));
			}
		}
	}

	private void parseError(HttpResponse response) throws JSONException {
		if (response != null) {
			HttpEntity bodyEntity = response.getEntity();
			StatusLine line = response.getStatusLine();
			InputStream is;
			try {
				is = bodyEntity.getContent();

				String body = readContent(is);

				Logger.i(TAG,
						"RESPONSE ERROR -> Content Length=" + bodyEntity.getContentLength()
								+ " Content=>" + body + " Description="
								+ line.getReasonPhrase());

				JSONObject bodyObject = new JSONObject(body);
				String userName = bodyObject
						.getString(JSON_FIELD_NOMBRE_USUARIO);
				setErrorResult(line.getReasonPhrase(), userName);
			} catch (IllegalStateException e) {
				setErrorResult(mContext.getString(R.string.error_json_broken));
			} catch (IOException e) {
				setErrorResult(mContext.getString(R.string.error_json_broken));
			}

		}
	}

	private String readContent(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedInputStream bis = new BufferedInputStream(is);

		byte[] buffer = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = bis.read(buffer)) > -1) {
			sb.append(new String(buffer, 0, bytesRead));
		}
		return sb.toString();
	}
}
