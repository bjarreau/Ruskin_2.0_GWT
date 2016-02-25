package com.ruskin.project.client.widget.mapwidget;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ruskin.project.client.MainWidget;

public class LayerSwitcher extends PopupPanel {
	MainWidget master;
	VerticalPanel holder = new VerticalPanel();
	CheckBox diary = new CheckBox("Diary");
	CheckBox ruskin = new CheckBox("Ruskin");
	
	public LayerSwitcher (MainWidget master) {
		this.master = master;
		
		buildUI();
	}

	private void buildUI() {
		holder.setSize("200px", "100px");
		Label lbl = new Label("Perspective");
		lbl.setWidth(holder.getElement().getStyle().getWidth());
		lbl.setStyleName("perspectiveLbl");
		
		holder.setStyleName("perspective");
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

	public void setHolder(VerticalPanel holder) {
		this.holder = holder;
	}

	public CheckBox getDiary() {
		return diary;
	}

	public CheckBox getRuskin() {
		return ruskin;
	}
	
}
