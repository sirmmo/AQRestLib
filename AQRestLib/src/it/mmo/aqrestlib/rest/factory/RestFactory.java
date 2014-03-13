package it.mmo.aqrestlib.rest.factory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONException;
import org.json.JSONObject;

import it.mmo.aqrestlib.rest.RestClient;
import android.content.Context;

public abstract class RestFactory {

	protected Context context;
	protected String configuration;
	protected String base_url;

	JSONObject conf;

	public RestFactory(Context context, String configuration, String base_url) {
		this.context = context;
		this.configuration = configuration;
		this.base_url = base_url;
	}

	public RestFactory getRemoteConfiguration() {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		Callable<JSONObject> c = new InnerRunnable();
		Future<JSONObject> j = executor.submit(c);
		while (!j.isDone())
			;
		try {
			conf = j.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	public RestFactory getJSONConfiguration() throws JSONException {
		conf = new JSONObject(configuration);
		return this;
	}

	public abstract RestClient getClient(String name);

	public class InnerRunnable implements Callable<JSONObject> {
		@Override
		public JSONObject call() throws Exception {
			return new JSONObject();
		}

	}
}
