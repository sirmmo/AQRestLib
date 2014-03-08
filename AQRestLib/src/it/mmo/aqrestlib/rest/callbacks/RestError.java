package it.mmo.aqrestlib.rest.callbacks;

import org.json.JSONObject;

public interface RestError {
	public void do_error(int status, JSONObject json, String url);

}
