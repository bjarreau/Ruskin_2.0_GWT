package com.ruskin.project.client;

import java.util.Collections;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
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
//		httpGetFile(this.getConfig().get(Const.KEY_FILE_NAME));
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
	
//	public static void httpGetFile(final String url) {
//		   String record = "";
//	        try{
//	            BufferedReader reader = new BufferedReader(new FileReader(url));
//	            String line;
//	            while ((line = reader.readLine()) != null){
//	                record += line;
//	            }
//	             reader.close();
//	            StringBuilder writer = new StringBuilder();
//	            writer.append(record);
//	        }catch (Exception e){
//	            System.out.println("Exception occurred trying to read" + url);
//	            e.printStackTrace();
//	        }
//	}
}