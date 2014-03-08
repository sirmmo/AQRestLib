package it.mmo.aqrestlib.rest.factory;

import org.json.JSONException;

import it.mmo.aqrestlib.rest.RestClient;
import android.content.Context;

public class TastypieRestFactory extends RestFactory {

	public TastypieRestFactory(Context context, String configuration, String base_url) {
		super(context, configuration, base_url);
		
	}

	@Override
	public RestClient getClient(String name) {
		try {
			return new RestClient(context, base_url, conf.getJSONObject(name).getString("url"), conf.getJSONObject(name).getString("params").split("|") );
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
