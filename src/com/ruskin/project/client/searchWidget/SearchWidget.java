package com.ruskin.project.client.searchWidget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.view.client.ListDataProvider;

import com.ruskin.project.client.MainWidget;
import com.ruskin.project.client.lists.AllList;
import com.ruskin.project.shared.GWTContact;

/** A search criteria widget that allows for a user to input desired
 * search criteria and fetch similar results
 *Author: Holden Pitre
 */
public class SearchWidget implements IsWidget {
	private final MainWidget master;
	
	Label blankSpace;
	
	VerticalPanel holder;
	HorizontalPanel searchbar;
	ScrollPanel scrollable;
	Button b;
	MultiWordSuggestOracle oracle;   
	SuggestBox box; 
	
	private final ListDataProvider<GWTContact> dataProvider = new ListDataProvider<GWTContact>();		
	private List<GWTContact> list = dataProvider.getList();	
	private final List<GWTContact> searchList = new ArrayList<>();
	
	public SearchWidget(MainWidget master) {
		this.master = master;
		
		blankSpace = new Label(" ");
		
		holder = new VerticalPanel();
		searchbar = new HorizontalPanel();
		
		searchbar.setWidth("1000px");
		searchbar.setHeight("60px");
		oracle = new MultiWordSuggestOracle();
		
		box = new SuggestBox(oracle);
		b = new Button("Search");
		
		BuildUI();
	}
	
	private void BuildUI() {
		blankSpace.setHeight("20px");
		
		oracle.add("Germany"); 
		oracle.add("France"); 
		oracle.add("Britain"); 
		oracle.add("Belgium"); 
		oracle.add("Prussia"); 
		oracle.add("Germany"); 
		oracle.add("Italy"); 
		oracle.add("Switzerland");
		
		b.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getList().clear();
				String toSearch = box.getText();
				for(int i=0; i<AllList.getSize(); i++) {
					GWTContact c = AllList.getAllContacts().get(i);
					if (toSearch.isEmpty()) {
						getList().add(c);
					}
					else if(c.getLocation().contains(toSearch) || c.getCountry().contains(toSearch)) {
						getList().add(c);
					}
				}		
				master.getMap().printContacts(list);
				master.getMap().getVectorLayer().redraw();
				System.out.println(getList().size());
			}
		});
		
		box.setWidth("940px");
		searchbar.add(box);
		searchbar.add(b);
		
		holder.add(blankSpace);
		holder.add(searchbar);
		
		holder.add(searchbar);
	}
	
	public List<GWTContact> getList() {
		return list;
	}

	public void setList(List<GWTContact> newList){
		list = searchList;
	}

	public ListDataProvider<GWTContact> getDataProvider() {
		return dataProvider;
	}
	
	@Override
	public Widget asWidget() {
		return holder;
	}

}
