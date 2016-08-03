package com.ruskin.project.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The async counterpart of {@link Service}.
 */
public interface ServiceAsync {
	void getConfig(AsyncCallback<Map<String, String>> callback);
}
