package com.ruskin.project.client.widget.mapwidget;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LayerSwitcher extends PopupPanel {
	
	VerticalPanel holder = new VerticalPanel();
	CheckBox diary = new CheckBox("Diary");
	CheckBox ruskin = new CheckBox("Ruskin");
	
	public LayerSwitcher () {
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

	public void setDiary(CheckBox diary) {
		this.diary = diary;
	}
	
}
