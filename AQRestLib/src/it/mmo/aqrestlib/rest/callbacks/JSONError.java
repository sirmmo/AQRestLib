package it.mmo.aqrestlib.rest.callbacks;

import org.json.JSONObject;

public interface JSONError {

	public void evaluate_json(String json, String url);
}
