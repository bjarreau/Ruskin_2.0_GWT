package com.ruskin.project.server;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ruskin.project.client.Service;
import com.ruskin.project.shared.Const;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ServiceImpl extends RemoteServiceServlet implements Service {
	
	private static final String DEFAULT_WMS_BASELAYER = "http://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile/${z}/${y}/${x}";
	private static final String DEFAULT_WMS_LAYER_NAMES = "ArcGIS";
	private static final String DEFAULT_FILE_NAME = "Hello";
	
	private final Map<String, String> clientConfig;
	private ServletConfig servletConfig;
	
	public ServiceImpl() {
		super();	
		clientConfig = new HashMap<>();
	}
	
	@Override
	public void init(final ServletConfig config) throws ServletException {
		super.init(config);
		servletConfig = config;

		for (final Enumeration<String> e = servletConfig.getInitParameterNames(); e.hasMoreElements();) {
		
			final String key = e.nextElement();
			final String val = servletConfig.getInitParameter(key);
				
			if (key.equals(Const.KEY_WMS_BASE_LAYER)) {
				
				if (val == null || val.trim().isEmpty()) {
					clientConfig.put(key, DEFAULT_WMS_BASELAYER);
				} else {
					clientConfig.put(key, val.trim());
				}

			} else if (key.equals(Const.KEY_WMS_LAYER_NAMES)) {
				if (val == null || val.trim().isEmpty()) {
					clientConfig.put(Const.KEY_WMS_LAYER_NAMES, DEFAULT_WMS_LAYER_NAMES);
				} else {
					clientConfig.put(Const.KEY_WMS_LAYER_NAMES, val);
				}
				
			} else if (key.equals(Const.KEY_FILE_NAME)) {
				if (val == null || val.trim().isEmpty()) {
					clientConfig.put(Const.KEY_FILE_NAME, DEFAULT_FILE_NAME);
				} else {
					clientConfig.put(Const.KEY_FILE_NAME, val);
				}
				
			}
		}
	}
	
	@Override
	public Map<String, String> getConfig() {
		return clientConfig;
	}
}
