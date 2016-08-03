package com.ruskin.project.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.ruskin.project.client.dialog.AllDialog;
import com.ruskin.project.client.lists.AllList;
import com.ruskin.project.client.lists.MaryList;
import com.ruskin.project.client.lists.PassThrough;
import com.ruskin.project.client.widget.mapwidget.LayerSwitcher;
import com.ruskin.project.client.widget.mapwidget.PlaceMapWidget;
import com.ruskin.project.client.widget.time.Carousel;

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
	private final AllList All = new AllList();
	private final MaryList Mary = new MaryList();
	private final PassThrough Pass = new PassThrough();
	
	private final Carousel timePanel;
	private final PlaceMapWidget placesMap;
	private final AllDialog allDialog;
	private final LayerSwitcher switcher;
	
	public MainWidget() {
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
	
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
}
