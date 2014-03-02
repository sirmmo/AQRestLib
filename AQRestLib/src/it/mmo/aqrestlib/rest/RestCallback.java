package it.mmo.aqrestlib.rest;

import org.json.JSONObject;

public interface RestCallback {
	public void do_callback(JSONObject json, String url);
}
