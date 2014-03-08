package it.mmo.aqrestlib.rest;

import it.mmo.aqrestlib.rest.customize.UrlManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class GlobalState implements UrlManager {
	
	private static UrlManager _instance;
	private Map<String, Object> _storage;
	private Map<String, Class<?>> _cstorage;
	
	private GlobalState (){
		_storage = new HashMap<String, Object>();
		_cstorage = new HashMap<String, Class<?>>();
	}
	
	public void clear(){
		_storage = new HashMap<String, Object>();
		_cstorage = new HashMap<String, Class<?>>();
	}
	
	public static UrlManager get(){
		if (_instance == null){
			_instance = new GlobalState();
		}
		return _instance;
	}
	
	@Override
	public void setString(String name, String value){
		_storage.put(name, value);
		_cstorage.put(name, String.class);
	}
	public void setJSON(String name, JSONObject value){
		_storage.put(name, value);
		_cstorage.put(name, JSONObject.class);
	}
	
	public void addElement(String list, String value){
		if (_storage.containsKey(list)){
		} else {
			_storage.put(list, new ArrayList<String>());
		}
		_storage.get(list);
	}
	
	public void removeVar(String name){
		_storage.remove(name);
		_cstorage.remove(name);
	}
	
	@Override
	public boolean hasVar(String name){
		return _storage.containsKey(name);
	}
	
	@Override
	public String getString(String name){
		if(_storage.containsKey(name))
			if(_cstorage.get(name).equals(String.class))
				return String.class.cast(_storage.get(name));
		return null;
	}
	
	public JSONObject getJSONObject(String name){
		if(_storage.containsKey(name))
			if(_cstorage.get(name).equals(JSONObject.class))
				return JSONObject.class.cast(_storage.get(name));
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getVar(String name){
		if(_storage.containsKey(name)){
			Class<?> t = _cstorage.get(name);
			T cast = (T) t.cast(_storage.get(name));
			return cast;
			}
		return null;
	}
	
	
	public String toString(){
		return _storage.toString();
	}
}


