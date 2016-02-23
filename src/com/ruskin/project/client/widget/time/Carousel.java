package com.ruskin.project.client.widget.time;

import java.util.List;
import java.util.ArrayList;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ruskin.project.client.MainWidget;
import com.ruskin.project.client.lists.AllList;
import com.ruskin.project.shared.GWTContact;

/** A timeline tracker that allows for a user to select a desired
 * date and view the places that were visited up until that date.
 *Authors: Holden Pitre & Brittney Jarreau
 */
public class Carousel implements IsWidget {
	final VerticalPanel carousel;
	
	final HorizontalPanel time;
	final HorizontalPanel btnContainer;
	
    final Button left;
    final Button right;
    
	final List<GWTContact> list;
	
	public Carousel(MainWidget master) {
		carousel = new VerticalPanel();
		time = new HorizontalPanel();
		btnContainer = new HorizontalPanel();
		
		left = new Button();
		right = new Button();
    	
    	list = new ArrayList<>();
   
    	buildUI();
	}
    
	public void buildUI() {
		Image leftArrow = new Image("img/white-left-arrow.png");
		Image rightArrow = new Image("img/white-right-arrow.png");
		
		btnContainer.setWidth("1250px");
		
		left.getElement().appendChild(leftArrow.getElement());
		left.setHeight("200px");
		left.setWidth("30px");
		left.getElement().getStyle().setFloat(Style.Float.LEFT);
		
		right.getElement().appendChild(rightArrow.getElement());
		right.setHeight("200px");
		right.setWidth("30px");
		right.getElement().getStyle().setFloat(Style.Float.RIGHT);

//    // ClickHandler for september listbox
//    september.addClickHandler(new ClickHandler() {
//    	public void onClick(ClickEvent Event) {
//    		allInfo.clear();
//    		String day = september.getItemText(september.getSelectedIndex());
//    		list.clear();
//    		int ref = AllList.getRef(day);
//    		for (GWTContact c : AllList.getAllContacts()) {
//    			if (ref >= c.getDateRef()) {
//    				HTML Summary = new HTML("<b>Arrival Date: </b> <br>" + c.getArrivalDate()+ "<br> <b>Departure Date: </b> <br>" + c.getDepartDate() + 
//    						"<br> <b>Country: </b> <br>" + c.getCountry() + "<br> <b>Location: </b> <br>" + c.getLocation()); 
//    
//    	    		Summary.addStyleName("summary");
//    				allInfo.add(Summary);
//        			allInfo.setVisible(true);
//        			list.add(c);
//    			}
//    		}
//    	}
//    });
		time.add(left);
		time.add(btnContainer);
		time.add(right);
		time.setWidth("1340px");
	
		carousel.add(time);
		carousel.setStyleName("carousel");
	}

	@Override
	public Widget asWidget() {
		return carousel;
	}
}
