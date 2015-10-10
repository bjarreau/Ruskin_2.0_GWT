package com.ruskin.project.client.searchWidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ruskin.project.client.MainWidget;


/** A search criteria widget that allows for a user to input desired
 * search criteria and fetch similar results
 *Author: Holden Pitre
 */
public class SearchWidget implements IsWidget {
	private final MainWidget master;
	VerticalPanel panel;
	TextBox tb;
	Button b;
	Label lbl;
	
	public SearchWidget(MainWidget master) {
		this.master = master;
		panel = new VerticalPanel();
		tb = new TextBox();
		b = new Button("Search");
		tb.setWidth("370px");
		lbl = new Label("Enter Search Criteria");
		
		
		BuildUI();
	}
	
	private void BuildUI() {
		b.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.alert("Will search for content matching: " + " '' " + tb.getText() + " '' ");
			}
		});
		
		lbl.setStyleName("flexTableCellHead");
		panel.setWidth("400px");
		panel.setHeight("100px");
		panel.add(lbl);
		panel.add(tb);
		panel.add(b);
		panel.setStyleName("flexTableCell");
	}
	
	@Override
	public Widget asWidget() {
		return panel;
	}

}