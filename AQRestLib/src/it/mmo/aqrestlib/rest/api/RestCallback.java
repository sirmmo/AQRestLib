package it.mmo.aqrestlib.rest.api;

import org.json.JSONObject;

public interface RestCallback {
	public void do_callback(JSONObject json, String url);
}
