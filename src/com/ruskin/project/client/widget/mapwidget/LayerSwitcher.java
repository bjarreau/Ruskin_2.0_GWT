package com.ruskin.project.client.widget.mapwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ruskin.project.client.MainWidget;

public class LayerSwitcher extends PopupPanel {
	private final Label lbl = new Label("Perspective");
	private final VerticalPanel holder = new VerticalPanel();
	private final CheckBox diary = new CheckBox(" Diary");
	private final CheckBox ruskin = new CheckBox(" Ruskin");
	
	private final MainWidget master;
	
	public LayerSwitcher (MainWidget master) {
		this.master = master;
		
		buildUI();
	}

	private void buildUI() {
		holder.setSize("200px", "100px");
		
		lbl.getElement().addClassName("perspectiveLbl");
		diary.getElement().addClassName("perspective");
		ruskin.getElement().addClassName("perspective");
		holder.getElement().addClassName("perspective");
		
		holder.add(lbl);
		holder.add(diary);
		holder.add(ruskin);
		
		setWidget(holder);

		this.getElement().getStyle().setZIndex(1500);
		
		diary.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent Event) {
				if(getDiary().getValue() && getRuskin().getValue()) {
					master.getMap().NewLayer("All Layers");
				} else if(getDiary().getValue()) {
					master.getMap().NewLayer("Diary Layer");
				} else if (getRuskin().getValue()){
					master.getMap().NewLayer("Ruskin Layer");
				} else {
					master.getMap().NewLayer("None");
				}
			}
		});
		
		ruskin.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent Event) {
				if(getDiary().getValue() && getRuskin().getValue()) {
					master.getMap().NewLayer("All Layers");
				} else if(getRuskin().getValue()) {
					master.getMap().NewLayer("Ruskin Layer");
				} else if (getDiary().getValue()){
					master.getMap().NewLayer("Diary Layer");
				} else {
					master.getMap().NewLayer("None");
				}
			}
		});
	}

	public VerticalPanel getHolder() {
		return holder;
	}

	public CheckBox getDiary() {
		return diary;
	}

	public CheckBox getRuskin() {
		return ruskin;
	}
	
}
