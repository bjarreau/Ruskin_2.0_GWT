package com.ruskin.project.client.dialog.contact;


import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ruskin.project.client.Main;
import com.ruskin.project.client.SimplifiedCallback;
import com.ruskin.project.shared.GWTContact;

public class ContactDialog {

	private static final NumberFormat nf = NumberFormat.getFormat("0.0####");
	private final DialogBox dialog;
	private final SimplePanel prettyView;
	private final Label lblId;
	private final Label lblLatitude;
	private final Label lblLongitude;
	private GWTContact showingFor;

	public ContactDialog() {
		dialog = new DialogBox(false, true);
		prettyView = new SimplePanel();
		lblId = new Label();
		lblLatitude = new Label();
		lblLongitude = new Label();
		buildUI();
	}

	private void buildUI() {
		dialog.getElement().getStyle().setZIndex(2000);
		dialog.setGlassEnabled(true);
		dialog.setWidth("500px");
		dialog.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(final CloseEvent<PopupPanel> event) {
				showingFor = null;
			}
		});

		buildPrettyView();

		final FlowPanel btnPanel = new FlowPanel();

		final Button btnClose = new Button("Close");
		btnClose.getElement().getStyle().setPadding(5, Unit.PX);
		btnClose.getElement().getStyle().setFloat(Style.Float.RIGHT);
		btnClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				dialog.hide();
			}
		});
		btnPanel.add(btnClose);

		final VerticalPanel mainContents = new VerticalPanel();
		mainContents.getElement().getStyle().setWidth(100, Unit.PCT);

		mainContents.add(prettyView);
		mainContents.add(new HTML(SafeHtmlUtils.fromSafeConstant("<hr />")));
		mainContents.add(btnPanel);

		dialog.setWidget(mainContents);
	}

	private void buildPrettyView() {
		final FlexTable table = new FlexTable();
		table.setWidth("500px");

		int i = 0;

		table.setWidget(i, 0, new Label("Contact ID:"));
		table.setWidget(i, 1, lblId);
		i += 1;


		table.setWidget(i, 0, new Label("Latitude:"));
		table.setWidget(i, 1, lblLatitude);
		i += 1;

		table.setWidget(i, 0, new Label("Longitude:"));
		table.setWidget(i, 1, lblLongitude);
		i += 1;
		
		prettyView.setWidget(table);
	}

	private void updateUI() {
		dialog.setText("Metadata for Point " + showingFor.getId());
		final StringBuilder txt = new StringBuilder();

		{
			final String val = showingFor.getId();
			lblId.setText(val);
			txt.append("ID: ").append(val).append("<br />");
		}
		{
			final String val = nf.format(showingFor.getLatitude());
			lblLatitude.setText(val);
			txt.append("Latitude: ").append(val).append("<br />");
		}
		{
			final String val = nf.format(showingFor.getLongitude());
			lblLongitude.setText(val);
			txt.append("Longitude: ").append(val).append("<br />");
		}
	}

	public void showFor(final String id) {
		
		Main.getContactServices().getContact(id, new SimplifiedCallback<GWTContact>("get contact with id " + id, true) {
			@Override
			public void onSuccess(GWTContact result) {	
				showFor(result);
			}
		});
	}

	public void showFor(final GWTContact c) {
		showingFor = c;
		updateUI();
		dialog.center();
	}
	
}
