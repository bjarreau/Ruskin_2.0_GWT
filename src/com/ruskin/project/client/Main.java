package com.ruskin.project.client;

import java.util.Collections;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * Author: Brittney Jarreau
 */

public class Main implements EntryPoint {
	private static Main INSTANCE;
	private final ServiceAsync manager;
	private Map<String, String> config;
	
	static final String xmlurl = "data/places_visited_by_mary.xml";
	private String xmlText = null;

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
//		httpGetFile(xmlurl);
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
	
	public static void httpGetFile(final String url) {
		try {
			// parse the XML document into a DOM
			Document messageDom = XMLParser.parse(url);
			
			messageDom.getDocumentElement().normalize();
			System.out.println("Root element :" + messageDom.getDocumentElement().getNodeName());
			
			NodeList nList = messageDom.getElementsByTagName("place");
			
			System.out.println("----------------------------");
	
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					System.out.println("Place type: " + eElement.getAttribute("type"));
				}
			} 
		} catch (Exception e) {
				e.printStackTrace();
		}
	}
}