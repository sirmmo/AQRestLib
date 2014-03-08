package it.mmo.aqrestlib.rest;

import it.mmo.aqrestlib.rest.callbacks.DialogCallback;
import it.mmo.aqrestlib.rest.callbacks.JSONError;
import it.mmo.aqrestlib.rest.callbacks.RestCallback;
import it.mmo.aqrestlib.rest.callbacks.RestError;
import it.mmo.aqrestlib.rest.customize.UrlManager;
import it.mmo.aqrestlib.utils.MapFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;

public class RestClient {
	protected AQuery aq = null;
	protected String active_domain;
	private Context context;
	private String[] urlPartsTemplate;
	private String[] urlParamsTemplate;
	private String url = "";

	private boolean no_check = false;

	private Map<String, String> urlParts = new HashMap<String, String>();
	private Map<String, String> urlParams = new HashMap<String, String>();
	private Map<String, String> headers = new HashMap<String, String>();
	private RestMethod restMethod = RestMethod.GET;
	private DialogCallback dialogCallback;

	/**
	 * 
	 * @param context
	 * @param active_domain
	 * @param url
	 * @param params
	 */
	public RestClient(Context context, String active_domain, String url,
			String params[]) {
		aq = new AQuery(context);
		this.context = context;
		this.active_domain = active_domain;
		this.url = url;
		ArrayList<String> strings = new ArrayList<String>();
		String[] up = url.split("{");
		for (String up1 : up) {
			strings.add(up1.split("}")[0]);
		}
		this.urlPartsTemplate = strings.toArray(new String[] {});
		this.urlParamsTemplate = params;
	}

	/***
	 * Sets if a given set of parameters should be tested or not
	 * 
	 * @param do_no_check
	 */
	protected void setCheck(boolean do_no_check) {
		no_check = !do_no_check;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return this for Fluent pattern
	 */
	public RestClient setParam(String key, String value) {
		Log.d(key, value);
		if (!no_check || Arrays.asList(urlParamsTemplate).contains(key))
			Log.d("SET", value);
		this.urlParams.put(key, value);
		return this;
	}
	/**
	 * 
	 * @param key
	 * @param value
	 * @return this for Fluent pattern
	 */
	public RestClient setHeader(String key, String value) {
		Log.d(key, value);
		this.headers.put(key, value);
		return this;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return this for Fluent pattern
	 */
	public RestClient setUrlPart(String key, String value) {
		Log.d(key, value);
		if (Arrays.asList(urlPartsTemplate).contains(key))
			this.urlParts.put(key, value);
		return this;
	}

	/**
	 * Compiles the url and params from the global state object
	 * @return this for Fluent pattern
	 */
	public RestClient compileFromGlobalState() {
		UrlManager state = GlobalState.get();
		this.compileFromGlobalState(state);
		return this;
	}

	/**
	 * Compiles the url and params from a custom global state object
	 * @param state the custom global state object
	 * @return this for Fluent pattern
	 */
	public RestClient compileFromGlobalState(UrlManager state) {
		Log.d("STATE", state.toString());
		for (String i : urlPartsTemplate) {
			if (state.hasVar(i)) {
				this.setUrlPart(i, state.getString(i));
			}
		}
		for (String i : urlParamsTemplate) {
			if (state.hasVar(i)) {
				this.setParam(i, state.getString(i));
			}
		}
		return this;
	}

	/**
	 * Gets the value from the configuration
	 * @param key
	 * @return the value for the key
	 */
	protected String getPart(String key) {
		if (urlParts.containsKey(key))
			return urlParts.get(key);
		else
			return "";
	}


	/**
	 * Gets the value from the configuration
	 * @param key
	 * @return the value for the key
	 */
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

	/**
	 * checks the parameters
	 * @throws RestUrlParameterException
	 */
	protected void checkParams() throws RestUrlParameterException {
		if (!no_check)
			for (String p : urlParamsTemplate) {
				if (!urlParams.containsKey(p))
					throw new RestUrlParameterException(p);
			}
	}

	/**
	 * Sets the method
	 * @param restMethod can be POST, GET, PUT, DELETE.
	 * @return this for Fluent pattern
	 */
	public RestClient setMethod(RestMethod restMethod){
		this.restMethod = restMethod;
		return this;
	}
	/**
	 * Sets the Dialog Callback function
	 * @param dialogCallback
	 * @return this for Fluent pattern
	 */
	public RestClient setDialogCallback(DialogCallback dialogCallback){
		this.dialogCallback = dialogCallback;
		return this;
	}
	
	
	public RestClient call(final RestMethod restMethod, final RestCallback restCallback) {
		this.call(restMethod, restCallback, null, null, dialogCallback);
		return this;
	}

	public RestClient call(final RestMethod restMethod, final RestCallback restCallback, final RestError restError) {
		this.call(restMethod, restCallback, restError, null, dialogCallback);
		return this;
	}

	public RestClient call(final RestMethod restMethod, final RestCallback restCallback, final RestError restError, final JSONError jsonError) {
		this.call(restMethod, restCallback, restError, jsonError, dialogCallback);
		return this;
	}
	
	public RestClient call(final RestCallback restCallback) {
		this.call(restMethod, restCallback, null, null, dialogCallback);
		return this;
	}
	
	public RestClient call(final RestCallback restCallback, final RestError restError) {
		this.call(restMethod, restCallback, restError, null, dialogCallback);
		return this;
	}
	
	public RestClient call(final RestCallback restCallback, final RestError restError, final JSONError jsonError) {
		this.call(restMethod, restCallback, restError, jsonError, dialogCallback);
		return this;
	}
	
	private void call(final RestMethod restMethod, final RestCallback restCallback,
			final RestError restError, final JSONError jsonError,
			final DialogCallback dialogCallback) {
		try {
			if (dialogCallback != null)
				dialogCallback.openLoadingDialog();
			this.checkParams();
			String iurl = active_domain + MapFormat.format(url, urlParts);
			Log.d("URL", iurl);
			Log.d("URL-PARAMS", urlParams.toString());
			
			switch(restMethod){
			case GET:
				iurl += urlSafeParams();
				aq.ajax(iurl, String.class, new RestAjaxCallback(context, restCallback, restError, jsonError, dialogCallback) );
				break;
			case PUT:
				aq.put(iurl, new JSONObject(urlParams), String.class, new RestAjaxCallback(context, restCallback, restError, jsonError, dialogCallback) );
				break;
			case POST:
				aq.ajax(iurl, urlParams, String.class, new RestAjaxCallback(context, restCallback, restError, jsonError, dialogCallback) );
				break;
			case DELETE:
				iurl += urlSafeParams();
				aq.delete(iurl, String.class, new RestAjaxCallback(context, restCallback, restError, jsonError, dialogCallback) );
				break;
			}
			
		} catch (RestUrlParameterException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private String urlSafeParams(){
		StringBuilder sb = new StringBuilder();
		boolean f = true;
		for(Map.Entry<String, String> kvp: urlParams.entrySet()){
			String separator = "&";
			if(f){
				f= false;
				separator = "?";
			}
			sb.append(separator+kvp.getKey()+"="+kvp.toString());
		}
		return sb.toString();
	}
	
}
