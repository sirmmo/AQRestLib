package it.mmo.aqrestlib.rest;

import org.json.JSONObject;

public interface RestError {
	public void do_error(JSONObject json, String url);

}
