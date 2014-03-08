package it.mmo.aqrestlib.rest.callbacks;

import org.json.JSONObject;

public interface RestCallback {
	public void do_callback(int status, JSONObject json, String url);
}
