package com.ruskin.project.client;

import java.util.Collections;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;
import com.ruskin.project.server.ServiceImpl;
import com.ruskin.project.shared.Const;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * Author: Brittney Jarreau
 */

public class Main implements EntryPoint {
	private static Main INSTANCE;
	private final ServiceAsync manager;
	private Map<String, String> config;
	

	public static ServiceAsync getServices() {
		return INSTANCE.manager;
	}
	
	public static Map<String, String> getConfig() {
		return INSTANCE.config;
	}
	
	private Main() {
		INSTANCE = this;
		manager = GWT.create(Service.class);
	}
	
	/**
	 * This is the entry point method; it is called by GWT after creating this object.
	 */
	public void onModuleLoad() {
		// Trigger download of config.  When that is done, trigger download of featureToggleMap.
		// When that is done, kick off buildUI.
			manager.getConfig(new SimplifiedCallback<Map<String, String>>("get client-side config", true) {
				@Override
				public void onSuccess(Map<String, String> config) {
					Main.this.config = Collections.unmodifiableMap(config);
					buildUI();
					}
			});
	}

	/**
	 * Create the main user interface and then attach it to the {@link RootPanel}.
	 */
	private void buildUI() {
		RootPanel.getBodyElement().getStyle().setBackgroundColor("#172133");
		final RootPanel root = RootPanel.get();
		final MainWidget main = new MainWidget();		
		root.add(main);
	}
	
}