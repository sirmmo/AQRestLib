package it.mmo.aqrestlib.rest;

import it.mmo.aqrestlib.R;
import it.mmo.aqrestlib.R.string;
import it.mmo.aqrestlib.framework.DefaultActivity;
import it.mmo.aqrestlib.rest.callbacks.DialogCallback;
import it.mmo.aqrestlib.rest.callbacks.JSONError;
import it.mmo.aqrestlib.rest.callbacks.RestCallback;
import it.mmo.aqrestlib.rest.callbacks.RestError;
import it.mmo.aqrestlib.utils.MapFormat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public abstract class RestClient {
	protected AQuery aq = null;
	protected String active_domain;
	private Context context;
	private String[] urlPartsTemplate;
	private String[] urlParamsTemplate;
	private String url = "";
	private boolean show_load = true;
	
	private boolean no_check = false;

	private Map<String, String> urlParts = new HashMap<String, String>();
	private Map<String, String> urlParams = new HashMap<String, String>();
	private Map<String, String> headers = new HashMap<String, String>();

	/***
	 * super(context, "", new String[]{""}, new String[]{""});
	 * @param context the app context
	 * @param url is the url for the call
	 * @param urlParts array of part name
	 * @param params array of parameters
	 */
	public RestClient(Context context, String url, String urlParts[],
			String params[]) {
		aq = new AQuery(context);
		this.context = context;
		active_domain = context.getString(R.string.server_url);
		this.url = url;
		this.urlPartsTemplate = urlParts;
		this.urlParamsTemplate = params;
	}
/***
 * Sets if a given set of parameters should be tested or not
 * @param do_no_check
 */
	protected void setCheck(boolean do_no_check){
		no_check = !do_no_check;
	}
	
	public RestClient setParam(String key, String value) {
		Log.d(key, value);
		if (!no_check || Arrays.asList(urlParamsTemplate).contains(key))
			Log.d("SET", value);
			this.urlParams.put(key, value);
		return this;
	}
	
	public RestClient setHeader(String key, String value){
		Log.d(key, value);
		this.headers.put(key, value);
		return this;
	}

	public RestClient setUrlPart(String key, String value) {
		Log.d(key, value);
		if (Arrays.asList(urlPartsTemplate).contains(key))
			this.urlParts.put(key, value);
		return this;
	}

	public RestClient compileFromGlobalState() {
		GlobalState state = GlobalState.get();
		Log.d("STATE", GlobalState.get().toString());
		for (String i : urlPartsTemplate) {
			if (state.hasVar(i)) {
				this.setUrlPart(i, state.getVar(i));
			}
		}
		for (String i : urlParamsTemplate) {
			if (state.hasVar(i)) {
				this.setParam(i, state.getVar(i));
			}
		}
		return this;
	}

	protected String getPart(String key) {
		if (urlParts.containsKey(key))
			return urlParts.get(key);
		else
			return "";
	}

	protected String getParameter(String key) {
		if (urlParams.containsKey(key))
			return urlParams.get(key);
		else
			return "";
	}

	public Map<String, String> getUrlParts() {
		Map<String, String> ret = new HashMap<String, String>();
		for (String i : urlPartsTemplate) {
			ret.put(i, null);
		}
		return ret;
	}

	public Map<String, String> getParams() {
		Map<String, String> ret = new HashMap<String, String>();
		for (String i : urlParamsTemplate) {
			ret.put(i, null);
		}
		return ret;
	}

	protected void checkParams() throws RestUrlParameterException {
		if(!no_check)
			for (String p : urlParamsTemplate) {
				if (!urlParams.containsKey(p))
					throw new RestUrlParameterException("Il campo " + p
							+ " è obbligatorio");
			}
	}

	public void call(final RestCallback restCallback) {
		this.call(restCallback, null, null, null);
	}

	public void call(final RestCallback restCallback, final RestError restError, final JSONError jsonError, final DialogCallback dialog) {
		try {
			try {
				if(dialog != null)
					dialog.openLoadingDialog();
			} catch (Exception e) {
			}
			checkParams();
			String iurl = active_domain + MapFormat.format(url, urlParts);
			Log.d("URL - REQ", iurl);
			Log.d("URL - POST", urlParams.toString());
			
			aq.ajax(iurl, urlParams, String.class, new AjaxCallback<String>() {
				public void callback(String url, String json, AjaxStatus status) {
					try {
						DefaultActivity act = (DefaultActivity) context;
						if(dialog != null)
							dialog.closeLoadingDialog();
					} catch (Exception e) {
					}
					Log.d("URL", url);
					try {
						Log.d(url, json);
					} catch (Exception e) {
						Log.d(url,status.getCode()+"");
					}
					if (status.getCode() != 200) {
						Toast.makeText(context, "ERRORE DI RETE", Toast.LENGTH_SHORT).show();
					} else {
						JSONObject response;
						try {
							response = new JSONObject(json);
							if (response.has("status") && response.getString("status").equals("error")) {
								String message = "";
								try {
									StringBuilder messageBuilder = new StringBuilder();
									JSONObject msg = response.optJSONObject("message");
									JSONArray msga = response.optJSONArray("message");
									if (msg != null) {
										Iterator<?> k = msg.keys();
										while (k.hasNext()) {
											messageBuilder.append(msg.getString((String) k.next()));
										}
										message = messageBuilder.toString();
									} else if (msga != null) {
										for (int i = 0; i < msga.length(); i++) {
											messageBuilder.append(msga.getString(i));
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
									restError.do_error(response, url);
							} else {
								try{
									restCallback.do_callback(response, url);
								} catch (Exception e){
									e.printStackTrace();
								}
							}
						} catch (JSONException e) {
							if (jsonError != null)
								jsonError.evaluate_json(json, url);
							Toast.makeText(context, "ERRORE DI COMUNICAZIONE",
									Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}
				}
			});
		} catch (RestUrlParameterException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	public void showLoad(boolean show_load) {
		this.show_load = show_load;
	}
}
