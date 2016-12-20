package com.ruskin.project.client.widget.mapwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.Size;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.control.Control;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeatureOptions;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureSelectedListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.Image;
import org.gwtopenmaps.openlayers.client.layer.ImageOptions;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.TransitionEffect;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.layer.WMSOptions;
import org.gwtopenmaps.openlayers.client.layer.WMSParams;
import org.gwtopenmaps.openlayers.client.layer.XYZ;
import org.gwtopenmaps.openlayers.client.layer.XYZOptions;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.ruskin.project.client.Main;
import com.ruskin.project.client.MainWidget;
import com.ruskin.project.client.lists.MaryList;
import com.ruskin.project.shared.Const;
import com.ruskin.project.shared.GWTLocation;
import com.ruskin.project.shared.ReducedLocation;

public class PlaceMapWidget implements IsWidget {
	private final MainWidget master;
	private final Map map;	
	private final MapWidget mapWidget;
	private final SelectFeature placesControl;
	private final Bounds maxVisibleExtent;
	
	private final SelectFeatureOptions clickControlOptions = new SelectFeatureOptions();
	private static Vector placesLayer = new Vector("Places");
	private final HTMLPanel decorator = new HTMLPanel("");
	private final MapOptions options = new MapOptions();
	private final Bounds bounds = new Bounds(-6602637.2967569,2397352.6248374,9051666.0938681,11202898.282064);
	private static List<GWTLocation> currentlyHighlighted = new ArrayList<>();
	private final Projection proj = new Projection("EPSG:4326");
	private final LonLat center = new LonLat(8,48);
	
	public PlaceMapWidget(final MainWidget master) {	
		this.master = master;
		
		options.setNumZoomLevels(20);
		mapWidget = new MapWidget("100%", "460px", options);
		map = mapWidget.getMap();
		map.setRestrictedExtent(bounds);
		map.setMinMaxZoomLevel(0, 20);
		//map.getExtent().transform(new Projection("EPSG:900913"), proj);
	
		master.getLayerSwitcher();
		
		{
			final String layers = Const.KEY_WMS_LAYER_NAMES;
			this.addXYZ(layers, Const.KEY_WMS_BASE_LAYER);
			final String swiss = "Switzerland";
			this.addXYZOverlay(swiss, "img/1832_Switzerland_0247067.png", new Bounds(668000,5701000,1174800,6125500), new Size(100, 100));
		}
		
		placesControl = new SelectFeature(placesLayer, clickControlOptions);
		
		map.setRestrictedExtent(bounds);
		map.zoomToExtent(bounds);
		this.zoomToBounds(bounds);
		
		center.transform(proj.getProjectionCode(), map.getProjection());
		map.setCenter(center, 6);
		maxVisibleExtent = map.getExtent();
		
		buildUI();
	}	
	
	private void buildUI() {
		decorator.getElement().addClassName("mapDecorator");
		decorator.add(mapWidget);
		
		placesControl.setAutoActivate(true);
		
		placesLayer.addVectorFeatureSelectedListener(new VectorFeatureSelectedListener() {
			@Override
			public void onFeatureSelected(FeatureSelectedEvent eventObject) {
				GWTLocation c = GWTLocation.createGWTContact(eventObject.getVectorFeature().getAttributes().getAttributeAsString(Const.FEATURE_ATTRIBUTE_CONTACT_ID));
				eventObject.getVectorFeature().getStyle().setExternalGraphic("img/push_pin_green.png");	
				master.getAllDialog().showFor(c);
				placesControl.unSelect(eventObject.getVectorFeature());
			}
		});		
		
		map.addControl(placesControl);
		map.addLayer(placesLayer);
	}
	
	public void updateVisibleLayers() {
		if(master.getLayerSwitcher().getSwitzerland()) {
			map.getLayerByName("Switzerland").setIsVisible(true);
		} else {
			map.getLayerByName("Switzerland").setIsVisible(false);
		}
	}

	public void NewLayer(String choice) {
		placesLayer.destroyFeatures();
		if(choice.matches("All Layers")) {
			PlotPointAll(true);
			PlotPointDiary(false);
			PlotPointsRuskin(false);
		} else if (choice.matches("Diary Layer")) {
			PlotPointAll(false);
			PlotPointDiary(true);
			PlotPointsRuskin(false);
		} else if (choice.matches("Ruskin Layer")) {
			PlotPointAll(false);
			PlotPointDiary(false);
			PlotPointsRuskin(true);
		} else if (choice.matches("None")) {
			PlotPointAll(false);
			PlotPointDiary(false);
			PlotPointsRuskin(false);
		}
	}
	
	public Vector getVectorLayer() {
		return placesLayer;
	}
	
	/** Sets the center of the map and zoom level.
	 * @param ll
	 * @param zoom
	 */	
	public void setCenter(LonLat ll, int zoom) {
		this.map.setCenter(ll,zoom);
	}	
	
	/** Adds a {@link WMS} with only one map layer to this ContactMapWidget.
	 * @param name
	 * @param url - the URL needed for the WMS layer
	 */
	public void addXYZ(String name, String url) {
		final XYZOptions options = new XYZOptions();
		options.setSphericalMercator(true);
		final XYZ xyzLayer = new XYZ(name, url, options);
		
		this.map.addLayer(xyzLayer);
		this.map.setBaseLayer(xyzLayer);
	}
	
	public void addXYZOverlay(String name, String url, Bounds bounds, Size size) {
		final ImageOptions opts = new ImageOptions();
		opts.setNumZoomLevels(20);
		opts.setIsBaseLayer(false);
		final Image img= new Image(name, url, bounds, size, opts);
		this.map.addLayer(img);
		System.out.println(this.map.getLayerByName(name).getId());
		img.setIsVisible(false);
	}
	
	/** Zooms to a specified bounding box.
	 * @param bounds
	 */
	public void zoomToBounds(Bounds bounds) {	
		if(bounds == null){
			return;
		}
		bounds = bounds.transform( new Projection("EPSG: 900913"), new Projection("EPSG: 4326"));
		this.map.zoomToExtent(bounds);
	}
	
	/** Gets the bounding box currently being displayed by this MapWidget.
	 * @return
	 */
	public Bounds getBounds() {
		return this.map.getExtent().transform(new Projection("EPSG: 900913"), new Projection("EPSG: 4326"));
	}	
	
	/** Returns an array of all layers that have been added to this map.
	 * @return
	 */
	public Layer[] getLayers() {
		return map.getLayers();
	}
	
	/**
	 * Prints the given {@link ReducedLocation}s on this MapWidget.
	 * @param contacts
	 *            - a list of {@link ReducedLocation} objects resulting from search
	 */		
	public void printContacts(List<? extends ReducedLocation> contacts) {
		final Style pointStyle = new Style();		
		for (ReducedLocation c : contacts) {
			Point point = new Point(c.getLongitude(), c.getLatitude());
			point.transform(proj, new Projection(map.getProjection()));
				
			pointStyle.setExternalGraphic("img/push_pin_green.png");
			pointStyle.setGraphicSize(10, 17);
			pointStyle.setFillOpacity(1.0);

			VectorFeature pointFeature = new VectorFeature(point, pointStyle);
			pointFeature.getAttributes().setAttribute(Const.FEATURE_ATTRIBUTE_CONTACT_ID, c.getId());
			pointFeature.setFeatureId(c.getId());
			placesLayer.addFeature(pointFeature);
			placesControl.activate();
		}

		final Bounds dataExtent = getVectorLayer().getDataExtent();
		boolean outOfBounds = !maxVisibleExtent.containsBounds(dataExtent, false, true);
		if(!outOfBounds){		
			zoomToBounds(getVectorLayer().getDataExtent());			
		}else{
			this.setCenter(center, 5);
		}
	}
	
	public void PlotPointAll(Boolean plot) {
		final Style pointStyle = new Style();		
		
		if (plot == true) {
			for (int i=0; i<MaryList.getSize(); i++) {
				ReducedLocation c = MaryList.getReducedContact(i);
			
				Point point = new Point(c.getLongitude(), c.getLatitude());
				point.transform(proj, new Projection(map.getProjection()));
					
				pointStyle.setExternalGraphic("img/red_push_pin.png");
				pointStyle.setGraphicSize(10, 17);
				pointStyle.setFillOpacity(1.0);

				VectorFeature pointFeature = new VectorFeature(point, pointStyle);
				pointFeature.getAttributes().setAttribute(Const.FEATURE_ATTRIBUTE_CONTACT_ID, c.getId());
				pointFeature.setFeatureId(c.getId());
				placesLayer.addFeature(pointFeature);
			}
		}
	}
	
	public void PlotPointDiary(Boolean plot) {
		final Style pointStyle = new Style();	
		
		if (plot == true) {
			System.out.println(MaryList.getSize());
			for (int i=0; i<MaryList.getSize(); i++) {
				ReducedLocation c = MaryList.getReducedContact(i);
			
				Point point = new Point(c.getLongitude(), c.getLatitude());
				point.transform(proj, new Projection(map.getProjection()));
					
				pointStyle.setExternalGraphic("img/red_push_pin.png");
				pointStyle.setGraphicSize(10, 17);
				pointStyle.setFillOpacity(1.0);

				VectorFeature pointFeature = new VectorFeature(point, pointStyle);
				pointFeature.getAttributes().setAttribute(Const.FEATURE_ATTRIBUTE_CONTACT_ID, c.getId());
				pointFeature.setFeatureId(c.getId());
				placesLayer.addFeature(pointFeature);
			}
		}
	}
	public void PlotPointsRuskin (Boolean plot) {
		final Style pointStyle = new Style();	
		if (plot == true) {
			ReducedLocation c = new ReducedLocation("John Was Here", 60, 40);
			
			LonLat ll = c.getCoordinate();
			Point point = new Point(ll.lon(), ll.lat());
			point.transform(proj, new Projection(map.getProjection()));	
			pointStyle.setExternalGraphic("img/red_push_pin.png");
			pointStyle.setGraphicSize(10, 17);
			pointStyle.setFillOpacity(1.0);

			VectorFeature pointFeature = new VectorFeature(point, pointStyle);
			pointFeature.getAttributes().setAttribute(Const.FEATURE_ATTRIBUTE_CONTACT_ID, c.getId());
			pointFeature.setFeatureId(c.getId());
			placesLayer.addFeature(pointFeature);
		}
	}
	
	/**This method unhighlights all of the currently highlighted contacts.
	 * 
	 */
	public static void clearHighlighted() {	
		for(GWTLocation c:currentlyHighlighted){
			placesLayer.getFeatureById(c.getId()).getStyle().setExternalGraphic("img/push_pin_red.png");
		}
	}
	
	public List<GWTLocation> getCurrentlyHighlighted() {
		return currentlyHighlighted;
	}

	@Override
	public Widget asWidget() {
		return decorator;
	}
}