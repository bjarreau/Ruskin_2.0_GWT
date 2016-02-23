package com.ruskin.project.client.widget.time;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ruskin.project.client.MainWidget;

public class TimeWidget implements IsWidget { 
	private final VerticalPanel mainPanel;
	private final Carousel carousel;

	public TimeWidget(MainWidget master) {
		this.mainPanel = new VerticalPanel();
		this.mainPanel.setStyleName("queryPanel");
		carousel = new Carousel(master);

		buildUI();
	}

	public void buildUI() {
		mainPanel.add(carousel);
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}
}