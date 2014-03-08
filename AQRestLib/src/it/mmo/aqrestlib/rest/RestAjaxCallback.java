package it.mmo.aqrestlib.rest;

import it.mmo.aqrestlib.rest.callbacks.DialogCallback;
import it.mmo.aqrestlib.rest.callbacks.JSONError;
import it.mmo.aqrestlib.rest.callbacks.RestCallback;
import it.mmo.aqrestlib.rest.callbacks.RestError;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class RestAjaxCallback extends  AjaxCallback<String>{
	
	private RestCallback restCallback = null;
	private RestError restError = null;
	private JSONError jsonError = null;
	private DialogCallback dialogCallback = null;
	private Context context = null;
	
	public RestAjaxCallback(Context context, final RestCallback restCallback,
			final RestError restError, final JSONError jsonError,
			final DialogCallback dialogCallback){
		this.context = context;
		this.restCallback = restCallback;
		this.restError = restError;
		this.dialogCallback = dialogCallback;
		this.jsonError = jsonError;
	}
	
	
	public void callback(String url, String json, AjaxStatus status) {
		try {
			if (dialogCallback != null)
				dialogCallback.closeLoadingDialog();
		} catch (Exception e) {
		}
		Log.d("URL", url);
		try {
			Log.d(url, json);
		} catch (Exception e) {
			Log.d(url, status.getCode() + "");
		}
		if (status.getCode() != 200) {
			Toast.makeText(context, "NETWORK ERROR",
					Toast.LENGTH_SHORT).show();
		} else {
			JSONObject response;
			try {
				response = new JSONObject(json);
				if (response.has("status")
						&& response.getString("status").equals(
								"error")) {
					String message = "";
					try {
						StringBuilder messageBuilder = new StringBuilder();
						JSONObject msg = response
								.optJSONObject("message");
						JSONArray msga = response
								.optJSONArray("message");
						if (msg != null) {
							Iterator<?> k = msg.keys();
							while (k.hasNext()) {
								messageBuilder.append(msg
										.getString((String) k
												.next()));
							}
							message = messageBuilder.toString();
						} else if (msga != null) {
							for (int i = 0; i < msga.length(); i++) {
								messageBuilder.append(msga
										.getString(i));
							}
							message = messageBuilder.toString();
						} else {
							message = response.getString("message");
						}
					} catch (JSONException ex) {
					}
					Toast.makeText(context, message,
							Toast.LENGTH_SHORT).show();
					if (restError != null)
						restError.do_error(status.getCode(), response, url);
				} else {
					try {
						restCallback.do_callback(status.getCode(), response, url);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				Toast.makeText(context, "COMMUNICATION ERROR",
						Toast.LENGTH_SHORT).show();
				if (jsonError != null)
					jsonError.evaluate_json(json, url);
				e.printStackTrace();
			}
		}
	}
}
