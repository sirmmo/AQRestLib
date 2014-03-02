package it.mmo.aqrestlib.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class GlobalState {

	public static final String global_name = "field_nome";
	public static final String active_child = "nid";
	public static final String active_child_name = "nid_name";
	
	private static GlobalState _instance;
	private Map<String,String> _storage;
	private Map<String,JSONObject> _jstorage;
	private Map<String, List<String>> _astorage;
	
	private GlobalState (){
		_storage = new HashMap<String, String>();
		_jstorage = new HashMap<String, JSONObject>();
		_astorage = new HashMap<String, List<String>>();
	}
	
	public void clear(){
		_storage = new HashMap<String, String>();
		_jstorage = new HashMap<String, JSONObject>();
	}
	
	public static GlobalState get(){
		if (_instance == null){
			_instance = new GlobalState();
		}
		return _instance;
	}
	
	public void setVar(String name, String value){
		_storage.put(name, value);
	}
	public void setJSON(String name, JSONObject value){
		_jstorage.put(name, value);
	}
	
	public void addElement(String list, String value){
		if (_astorage.containsKey(list)){
		} else {
			_astorage.put(list, new ArrayList<String>());
		}
		_astorage.get(list).add(value);
	}
	
	public void removeElement(String list, String value){
		if (_astorage.containsKey(list))
			if (_astorage.get(list).contains(value))
				_astorage.get(list).remove(value);
	}
	
	public String[] getElements(String list){
		if (_astorage.containsKey(list)){
			return _astorage.get(list).toArray(new String[1]);
		} else
			return new String [0];
	}
	
	public void clearElements(String list){
		if(_astorage.containsKey(list))
			_astorage.get(list).clear();
	}

	public void removeVar(String name){
		_storage.remove(name);
	}
	
	public boolean hasVar(String name){
		return _storage.containsKey(name);
	}
	
	public String getVar(String name){
		try{
			return _storage.get(name);
		} catch(Exception e){
			return "";
		}
	}
	public JSONObject getJSON(String name){
		try{
			return _jstorage.get(name);
		} catch(Exception e){
			return new JSONObject();
		}
	}
	
	public String toString(){
		return _storage.toString();
	}
}


