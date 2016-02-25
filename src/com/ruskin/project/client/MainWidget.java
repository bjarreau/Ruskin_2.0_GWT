package com.ruskin.project.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ruskin.project.client.dialog.contact.AllDialog;
import com.ruskin.project.client.dialog.contact.DiaryLayerDialog;
import com.ruskin.project.client.dialog.contact.RuskinDialog;
import com.ruskin.project.client.lists.AllList;
import com.ruskin.project.client.lists.JJList;
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
	
	private final Carousel timePanel;
	private VerticalPanel mainPanel = new VerticalPanel();	
	private final PlaceMapWidget placesMap;
	private final DiaryLayerDialog diaryDialog;
	private final RuskinDialog ruskinDialog;
	private final AllDialog allDialog;
	
	private final AllList All = new AllList();
	private final MaryList Mary = new MaryList();
	private final JJList JohnJames = new JJList();
	private final PassThrough Pass = new PassThrough();
	
	private final LayerSwitcher switcher;
	
	public MainWidget() {
		this.mainPanel = new VerticalPanel();
		placesMap = new PlaceMapWidget(this);
		timePanel = new Carousel(this);
		diaryDialog = new DiaryLayerDialog(this);
		ruskinDialog = new RuskinDialog(this);
		allDialog = new AllDialog(this);
		
		switcher = new LayerSwitcher();
		this.buildUI();		
	}
	
	private void buildUI() {
		mainPanel.setWidth("100%");
		
		switcher.setPopupPosition(Window.getClientWidth()-270, 60);
		switcher.show();
		
		FlowPanel titleContainer = new FlowPanel();
		titleContainer.setStyleName("titleContainer");
		
		Label titleLabel = new Label();
		Image logo = new Image("img/ruskin_logo_2.png");
		logo.setHeight("40px");
	
		titleLabel.setStyleName("titleLabel");
		titleLabel.getElement().appendChild(logo.getElement());
		titleLabel.addClickHandler(new ClickHandler () {
			 public void onClick(ClickEvent event) {  
		          Window.Location.assign("http://english.selu.edu/humanitiesonline/ruskin/apparatuses/account_of_a_tour_on_the_continent_apparatus.php");
			 }
		});
		
		titleContainer.add(titleLabel);
		mainPanel.add(titleContainer);		
		mainPanel.add(placesMap);
		mainPanel.add(timePanel);		
	}
	
	public final PlaceMapWidget getMap() {
		return placesMap;
	}
	
	public Carousel getTimeWidget() {
		return timePanel;
	}
	
	public DiaryLayerDialog getDiaryDialog() {
		return diaryDialog;
	}
	
	public RuskinDialog getRuskinDialog() {
		return ruskinDialog;
	}
	
	public AllDialog getAllDialog() {
		return allDialog;
	}
	
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
}
