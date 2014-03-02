package it.mmo.aqrestlib.rest.api;

import org.json.JSONObject;

public interface RestError {
	public void do_error(JSONObject json, String url);

}
