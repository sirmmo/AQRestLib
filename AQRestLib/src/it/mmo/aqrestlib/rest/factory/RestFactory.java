package it.mmo.aqrestlib.rest.factory;

import org.json.JSONException;
import org.json.JSONObject;

import it.mmo.aqrestlib.rest.RestClient;
import android.content.Context;

public abstract class RestFactory {
	
	protected Context context;
	protected String configuration;
	protected String base_url;
	
	JSONObject conf;
	
	public RestFactory(Context context, String configuration, String base_url){
		this.context = context;
		this.configuration = configuration;
		this.base_url = base_url;
	}
	
	public RestFactory getRemoteConfiguration(){
		return this;
	}
	
	public RestFactory getJSONConfiguration() throws JSONException{
		conf = new JSONObject(configuration);
		return this;
	}
	
	public abstract RestClient getClient(String name);
}
