package it.mmo.aqrestlib.rest.objectmodel;

public interface UrlManager {

	public abstract void setString(String name, String value);

	public abstract boolean hasVar(String name);

	public abstract String getString(String name);

}