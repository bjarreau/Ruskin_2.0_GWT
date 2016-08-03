package com.ruskin.project.client.widget.time;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.ruskin.project.client.MainWidget;
import com.ruskin.project.client.lists.AllList;
import com.ruskin.project.shared.GWTLocation;

/** A timeline tracker that allows for a user to select a desired
 * date and view the places that were visited up until that date.
 *Authors: Holden Pitre & Brittney Jarreau
 */
public class Carousel implements IsWidget {
	private final HTMLPanel carousel= new HTMLPanel("");
	private final MainWidget master;
	private final HorizontalPanel time = new HorizontalPanel();
	private final HorizontalPanel btnContainer = new HorizontalPanel();
    private final Button left = new Button();
    private final Button right = new Button();
    
	private final List<GWTLocation> list = new ArrayList<GWTLocation>();
	private final List<Button> buttons = new ArrayList<Button>();
	
	private int index = 4;
	
	public Carousel(MainWidget master) {
		this.master = master;

    	genButtons();
    	buildUI();
	}
	
	private void updateMap() {
		final Boolean diary = master.getLayerSwitcher().getDiary().getValue();
		final Boolean ruskin = master.getLayerSwitcher().getRuskin().getValue();
		
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
		final String id = buttons.get(index).getTitle();

		for(GWTLocation c : AllList.getAllContacts()) {
			if (c.getDateRef() <= AllList.getContact(id).getDateRef()) {
				list.add(c);
			}
		}

		master.getMap().printContacts(list);
		master.getMap().getVectorLayer().redraw();
	}
    
	private void genButtons() {
		for(GWTLocation c : AllList.getAllContacts()) {
			final GWTLocation cf = c;
			Button b = new Button();
			b.setTitle(c.getId());
			b.setHTML(c.getArrivalDate()+ "-" + c.getDepartDate() + "<br>" + c.getLocation() + ", " + c.getCountry() + "<br>");
			buttons.add(b);	
			b.setStyleName("box");
			b.setWidth(((Window.getClientWidth() - 100) / 5) - 50 + "px");
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
		final Image leftArrow = new Image("img/white-left-arrow.png");
		final Image rightArrow = new Image("img/white-right-arrow.png");
		left.getElement().appendChild(leftArrow.getElement());
		right.getElement().appendChild(rightArrow.getElement());
		
		btnContainer.getElement().addClassName("timeBackground");
		btnContainer.getElement().addClassName("carousel");
		time.getElement().addClassName("timeBackground");
		time.getElement().addClassName("carousel");
		carousel.getElement().addClassName("carousel");
		
		left.getElement().getStyle().setFloat(Style.Float.LEFT);
		right.getElement().getStyle().setFloat(Style.Float.RIGHT);
		left.setStyleName("timeBtn");
		right.setStyleName("timeBtn");
		
		left.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent Event) {
				if(index!=4) {
					index--;
					updateButtons();
					updateMap();
				}
			}
		});
		
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
		
		time.add(left);
		time.add(btnContainer);
		time.add(right);
	
		carousel.add(time);
	}

	@Override
	public Widget asWidget() {
		return carousel;
	}
}
