package com.ruskin.project.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;
import com.ruskin.project.client.dialog.AllDialog;
import com.ruskin.project.client.lists.AllList;
import com.ruskin.project.client.lists.MaryList;
import com.ruskin.project.client.lists.PassThrough;
import com.ruskin.project.client.widget.mapwidget.LayerSwitcher;
import com.ruskin.project.client.widget.mapwidget.PlaceMapWidget;
import com.ruskin.project.client.widget.time.Carousel;
import com.ruskin.project.shared.Const;

/**
 * The main widget, which is added to the RootPanel.
 * It contains the Map, Search, Time, and Forum widgets.
 * 
 * Author: Brittney Jarreau
 */
public class MainWidget implements IsWidget {
	private final Label titleLabel = new Label();
	private final Image logo = new Image("img/ruskin_logo_2.png");
	private final HTMLPanel mainPanel = new HTMLPanel("");	
	private final HTMLPanel titlePanel = new HTMLPanel("");
	private final HTMLPanel chronologyPanel = new HTMLPanel("");
	private final String dataFileName = Main.getConfig().get(Const.KEY_FILE_NAME);
	
	private String xmlData;
	private final Carousel timePanel;
	private final PlaceMapWidget placesMap;
	private final AllDialog allDialog;
	private final LayerSwitcher switcher;
	
	public MainWidget() {
		new AllList();
		new MaryList();
		new PassThrough();
		
		placesMap = new PlaceMapWidget(this);
		switcher = new LayerSwitcher(this);
		timePanel = new Carousel(this);
		allDialog = new AllDialog(this);
	
		
		this.buildUI();		
	}
	
	private void buildUI() {
		mainPanel.setWidth("100%");

		switcher.setPopupPosition(Window.getClientWidth()-250, 60);
		switcher.show();
	
		logo.setHeight("50px");
	
		titlePanel.getElement().addClassName("titleContainer");
		titleLabel.getElement().addClassName("titleLabel");
		titleLabel.getElement().appendChild(logo.getElement());
		titleLabel.addClickHandler(new ClickHandler () {
			 public void onClick(ClickEvent event) {  
		          Window.Location.assign("http://english.selu.edu/humanitiesonline/ruskin/apparatuses/account_of_a_tour_on_the_continent_apparatus.php");
			 }
		});
		
		httpGetFile(dataFileName, new AsyncCallback<String>() {
	        public void onFailure(Throwable caught) {
	            xmlData = "Error";
	        }

	        public void onSuccess(String xmlText) {
	            xmlData = xmlText;
	            parseMessage(xmlData);
	        }
	    });
		
		titlePanel.add(titleLabel);
		chronologyPanel.add(timePanel);
		mainPanel.add(titlePanel);		
		mainPanel.add(placesMap);
		mainPanel.add(chronologyPanel);		
	}
	
	public final PlaceMapWidget getMap() {
		return placesMap;
	}
	
	public Carousel getTimeWidget() {
		return timePanel;
	}
	
	public AllDialog getAllDialog() {
		return allDialog;
	}
	
	public LayerSwitcher getLayerSwitcher() {
		return switcher;
	}
	
	public static void httpGetFile(final String url, final AsyncCallback<String> callback) {
	    final RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, url);
	    rb.setCallback(new RequestCallback() {
	        public void onResponseReceived(Request request, Response response) {
	            try {
	                final int responseCode = response.getStatusCode() / 100;
	                if (url.startsWith("file:/") || (responseCode == 2)) {
	                    callback.onSuccess(response.getText());
	                } else {
	                    callback.onFailure(new IllegalStateException("HttpError#" + response.getStatusCode() + " - " + response.getStatusText()));
	                }
	            } catch (Throwable e) {
	                callback.onFailure(e);
	            }
	        }

	        public void onError(Request request, Throwable exception) {
	            callback.onFailure(exception);
	        }
	    });
	    try {
	        rb.send();
	    } catch (RequestException e) {
	        callback.onFailure(e);
	    }
	}
	
	private void parseMessage(String messageXml) {
		  try {
		    // parse the XML document into a DOM
		    final Document messageDom = XMLParser.parse(messageXml);
		    final Element element = messageDom.getDocumentElement();
		    final NodeList nodelist = element.getElementsByTagName("place");
		    final int count = nodelist.getLength();
		    
		    System.out.println("Found " + count + " Items");
		    
		    for(int i = 0; i < count; i++) {
		        final Element list = (Element) nodelist.item(i);
		        String terrain = ((Text)list.getElementsByTagName("terrain")
		             .item(0).getFirstChild()).getData();
			    
		        System.out.println("terrain: " + terrain);
		    }
		    
		  } catch (DOMException e) {
		    Window.alert("Could not parse XML document. Exception Caught" );
		  }
		}
	
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
}
