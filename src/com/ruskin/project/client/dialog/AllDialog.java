package com.ruskin.project.client.dialog;

import java.util.List;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.ruskin.project.client.MainWidget;
import com.ruskin.project.client.lists.MaryList;
import com.ruskin.project.client.lists.PassThrough;
import com.ruskin.project.shared.GWTLocation;
import com.ruskin.project.shared.GWTPassThrough;

/** This Widget is intended to display metadata describing clicked points
 *Author: Brittney Jarreau
 */
public class AllDialog {
	private final MainWidget master;
	
	private final NumberFormat nf = NumberFormat.getFormat("0.0####");
	private final DialogBox dialog = new DialogBox(false, true);
	
	protected final CellTable<GWTPassThrough> table = new CellTable<GWTPassThrough>();
	private final ListDataProvider<GWTPassThrough> dataProvider = new ListDataProvider<GWTPassThrough>();		
	private List<GWTPassThrough> list = dataProvider.getList();	
	
	private final ScrollPanel passPnl = new ScrollPanel();
	private final VerticalPanel passStuff = new VerticalPanel();
	
	private final SimplePanel maryView = new SimplePanel();
	private final Label MarylblId = new Label();
	private final Label MarylblArrivalDate= new Label();
	private final Label MarylblDepartDate= new Label();
	private final Label MarylblLocation= new Label();
	private final Label MarylblLink= new Label();
	private final Label MarylblSights= new Label();
	private final Label MarylblLatitude = new Label();
	private final Label MarylblLongitude = new Label();
	private final Label passThrus = new Label("Pass-Throughs");
	
	private GWTLocation showingFor;
	private GWTLocation showingForMary;
	
	private List<GWTPassThrough> newList;
	
	public AllDialog(MainWidget master) {
		this.master = master;
		
		buildUI();
	}

	private void buildUI() {
		passPnl.setWidth("450px");
		passPnl.setHeight("100px");
		
		passThrus.getElement().addClassName("passLbl");
		
		dialog.getElement().getStyle().setZIndex(2000);
		dialog.setGlassEnabled(true);
		dialog.setWidth("500px");
		dialog.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(final CloseEvent<PopupPanel> event) {
				
				master.getMap().getVectorLayer().getFeatureById(showingFor.getId()).getStyle().setExternalGraphic("img/red_push_pin.png");
				
				showingFor = null;
				showingForMary = null;
				list.clear();
				master.getMap().getVectorLayer().redraw();
			}
		});

		buildMaryView();
		
		maryView.getElement().getStyle().setWidth(100, Unit.PCT);
//		tabPanel.add(maryView, "Mary");
//		tabPanel.selectTab(0);

		final FlowPanel btnPanel = new FlowPanel();
		final Button btnClose = new Button();
		
		btnClose.getElement().getStyle().setPadding(5, Unit.PX);
		btnClose.getElement().getStyle().setFloat(Style.Float.RIGHT);
		btnClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				dialog.hide();
			}
		});
		btnClose.setStyleName("closeBtn");

		final VerticalPanel mainContents = new VerticalPanel();
		mainContents.getElement().getStyle().setWidth(100, Unit.PCT);
		
		// Create the Results table
				TextColumn<GWTPassThrough> countryColumn = new TextColumn<GWTPassThrough>() {
					@Override
					public String getValue(GWTPassThrough contact) {
						return contact.getCountry();
					}
				};
				TextColumn<GWTPassThrough> locationColumn = new TextColumn<GWTPassThrough>() {
					@Override
					public String getValue(GWTPassThrough contact) {
						return contact.getLocation();
					}
				};
				TextColumn<GWTPassThrough> sightColumn = new TextColumn<GWTPassThrough>() {
					@Override
					public String getValue(GWTPassThrough contact) {
						return contact.getSights();
					}
				};
		table.setColumnWidth(0, "25%");
		table.setColumnWidth(1, "25%");
		table.setColumnWidth(2, "50%");		
		table.addColumn(countryColumn, "COUNTRY");
		table.addColumn(locationColumn, "LOCATION");
		table.addColumn(sightColumn, "SIGHTS");
		table.setStyleName("dialogTable");
		table.getHeader(0).setHeaderStyleNames("dialogTableHeader");
		table.getHeader(1).setHeaderStyleNames("dialogTableHeader");
		table.getHeader(2).setHeaderStyleNames("dialogTableHeader");
		table.setWidth("100%", true);
				
		dataProvider.addDataDisplay(table);	
		
		passPnl.add(table);
		
		passStuff.add(passThrus);
		passStuff.add(passPnl);
		passStuff.setStyleName("dialogTableContainer");
		
		mainContents.add(btnClose);
		mainContents.add(maryView);
		mainContents.add(passStuff);
	
		mainContents.setStyleName("dialogPanel");

		dialog.setWidget(mainContents);
		dialog.setStyleName("gwt-DialogBox");
	}

	private void buildMaryView() {
		final FlexTable table = new FlexTable();
		table.setWidth("500px");

		int i = 0;

		table.setWidget(i, 0, new Label("Country:"));
		table.setWidget(i, 1, MarylblId);
		table.getCellFormatter().setStyleName(i, 0, "dialogLbl");
		i += 1;
		
		table.setWidget(i, 0, new Label("Longitude"));
		table.setWidget(i, 1, MarylblLongitude);
		table.getCellFormatter().setStyleName(i, 0, "dialogLbl");
		i += 1;
		
		table.setWidget(i, 0, new Label("Latitude:"));
		table.setWidget(i, 1, MarylblLatitude);
		table.getCellFormatter().setStyleName(i, 0, "dialogLbl");
		i += 1;
		
		table.setWidget(i, 0, new Label("Arrival Date:"));
		table.setWidget(i, 1, MarylblArrivalDate);
		table.getCellFormatter().setStyleName(i, 0, "dialogLbl");
		i += 1;
		
		table.setWidget(i, 0, new Label("Departure Date:"));
		table.setWidget(i, 1, MarylblDepartDate);
		table.getCellFormatter().setStyleName(i, 0, "dialogLbl");
		i += 1;
		
		table.setWidget(i, 0, new Label("Location:"));
		table.setWidget(i, 1, MarylblLocation);
		table.getCellFormatter().setStyleName(i, 0, "dialogLbl");
		i += 1;
		
		table.setWidget(i, 0, new Label("Sights:"));
		table.setWidget(i, 1, MarylblSights);
		table.getCellFormatter().setStyleName(i, 0, "dialogLbl");
		i += 1;
		
		table.setWidget(i, 0, new Label("Link:"));
		table.setWidget(i, 1, MarylblLink);
		table.getCellFormatter().setStyleName(i, 0, "dialogLbl");
		i += 1;
		
		maryView.setWidget(table);
	}

	
	private void updateUI() {
		dialog.setText(showingFor.getId());
		{
			final String Mval = showingForMary.getCountry();
			MarylblId.setText(Mval);
			MarylblId.setStyleName("dialogLbl");
		}
		{
			final String Mval = nf.format(showingForMary.getLongitude());
			MarylblLongitude.setText(Mval);
			MarylblLongitude.setStyleName("dialogLbl");
		}
		{
			final String Mval = nf.format(showingForMary.getLatitude());
			MarylblLatitude.setText(Mval);
			MarylblLatitude.setStyleName("dialogLbl");
		}
		{
			final String Mval = showingForMary.getArrivalDate();
			MarylblArrivalDate.setText(Mval);
			MarylblArrivalDate.setStyleName("dialogLbl");
		}
		{
			final String Mval = showingForMary.getDepartDate();
			MarylblDepartDate.setText(Mval);
			MarylblDepartDate.setStyleName("dialogLbl");
		}
		{
			final String Mval = showingForMary.getLocation();
			MarylblLocation.setText(Mval);
			MarylblLocation.setStyleName("dialogLbl");
		}
		{
			final String Mval = showingForMary.getLink();
			MarylblLink.setText(Mval);
			MarylblLink.setStyleName("dialogLbl");
		}
		{
			final String Mval = showingForMary.getSights();
			MarylblSights.setText(Mval);
			MarylblSights.setStyleName("dialogLbl");
		}
	}

	public void showFor(final GWTLocation c) {
		showingFor = c;
		showingForMary = MaryList.getContact(c.getId());
		for(int i=0; i<PassThrough.getPass(c.getId()).size(); i++) {
			list.add(PassThrough.getPass(c.getId()).get(i));
		}
		updateUI();
		dialog.center();
	}
	
	public CellTable<GWTPassThrough> getTable() {
		return table;
	}
	
	public List<GWTPassThrough> getList() {
		return list;
	}

	public void setList(List<GWTPassThrough> newList){
		list = newList;
	}

	public ListDataProvider<GWTPassThrough> getDataProvider() {
		return dataProvider;
	}
}
