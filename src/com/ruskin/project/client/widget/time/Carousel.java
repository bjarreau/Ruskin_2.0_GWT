package com.ruskin.project.client.widget.time;

import java.util.List;
import java.util.ArrayList;

import org.gwtopenmaps.openlayers.client.event.VectorFeatureSelectedListener;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureSelectedListener.FeatureSelectedEvent;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
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
import com.ruskin.project.shared.Const;
import com.ruskin.project.shared.GWTContact;

/** A timeline tracker that allows for a user to select a desired
 * date and view the places that were visited up until that date.
 *Authors: Holden Pitre & Brittney Jarreau
 */
public class Carousel implements IsWidget {
	final VerticalPanel carousel;
	final MainWidget master;
	
	final HorizontalPanel time;
	final HorizontalPanel btnContainer;
	
    final Button left;
    final Button right;
    
	final List<GWTContact> list = new ArrayList<GWTContact>();
	final List<Button> buttons;
	
	public int index = 4;
	
	public Carousel(MainWidget master) {
		this.master = master;
		
		carousel = new VerticalPanel();
		time = new HorizontalPanel();
		btnContainer = new HorizontalPanel();
		
		left = new Button();
		right = new Button();
    	
    	
    	buttons = new ArrayList<Button>();
   
    	genButtons();
    	buildUI();
	}
	
	private void updateMap() {
		Boolean diary = master.getLayerSwitcher().getDiary().getValue();
		Boolean ruskin = master.getLayerSwitcher().getRuskin().getValue();
		
		if(diary && ruskin) {
			master.getMap().NewLayer("All Layers");
		} else if(ruskin) {
			master.getMap().NewLayer("Ruskin Layer");
		} else if (diary){
			master.getMap().NewLayer("Diary Layer");
		} else {
			master.getMap().NewLayer("None");
		}
		
		list.clear();
		String id = buttons.get(index).getTitle();

		for(GWTContact c : AllList.getAllContacts()) {
			if (c.getDateRef() <= AllList.getContact(id).getDateRef()) {
				list.add(c);
			}
		}

		master.getMap().printContacts(list);
		master.getMap().getVectorLayer().redraw();
		System.out.println("Current layer: " + master.getMap().getVectorLayer().getName());
	}
    
	private void genButtons() {
		for(GWTContact c : AllList.getAllContacts()) {
			final GWTContact cf = c;
			Button b = new Button();
			b.setTitle(c.getId());
			b.setHTML(c.getArrivalDate()+ "-" + c.getDepartDate() + "<br>" + c.getLocation() + "," + c.getCountry() + "<br>");
			buttons.add(b);	
			b.setStyleName("box");
			b.setWidth(((Window.getClientWidth() - 110) / 5) - 40 + "px");
			b.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent Event) {
					master.getAllDialog().showFor(cf);
				}
			});
		}
	}
	
	private void updateButtons() {
		btnContainer.clear();
		
		btnContainer.add(buttons.get(index-4));
		btnContainer.add(buttons.get(index-3));
		btnContainer.add(buttons.get(index-2));
		btnContainer.add(buttons.get(index-1));
		btnContainer.add(buttons.get(index));
	}

	public void buildUI() {
		Image leftArrow = new Image("img/white-left-arrow.png");
		Image rightArrow = new Image("img/white-right-arrow.png");
		
		btnContainer.setWidth("1250px");
		
		left.getElement().appendChild(leftArrow.getElement());
		left.setHeight("130px");
		left.setWidth("50px");
		left.getElement().getStyle().setFloat(Style.Float.LEFT);
		left.setStyleName("timeBtn");
		left.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent Event) {
				if(index!=4) {
					index--;
					updateButtons();
					updateMap();
				}
			}
		});
		
		right.getElement().appendChild(rightArrow.getElement());
		right.setHeight("130px");
		right.setWidth("50px");
		right.getElement().getStyle().setFloat(Style.Float.RIGHT);
		right.setStyleName("timeBtn");
		right.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent Event) {
				if(index != AllList.getSize()-1) {
					index++;
					updateButtons();
					updateMap();
				}
			}
		});
		
		updateButtons();
		btnContainer.setStyleName("timeBackground");
		
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
