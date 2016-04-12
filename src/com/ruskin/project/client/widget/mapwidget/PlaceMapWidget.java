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
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeatureOptions;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureSelectedListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.ArcGIS93Rest;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.OSM;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.layer.WMSParams;
import org.gwtopenmaps.openlayers.client.layer.XYZ;
import org.gwtopenmaps.openlayers.client.layer.XYZOptions;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ruskin.project.client.MainWidget;
import com.ruskin.project.client.lists.MaryList;
import com.ruskin.project.shared.Const;
import com.ruskin.project.shared.GWTContact;
import com.ruskin.project.shared.ReducedContact;

public class PlaceMapWidget implements IsWidget {
	private final VerticalPanel decorator;
	
	private final Map map;
	
	private final MapOptions options;
	private final MapWidget mapWidget;
	
	private final LayerSwitcher switcher;
	
	private static Vector placesLayer;
	
	private final SelectFeatureOptions clickControlOptions;
	private final SelectFeature placesControl;
	
	private Bounds bounds = new Bounds(-6602637.2967569,2397352.6248374,9051666.0938681,11202898.282064);
	private final Bounds maxVisibleExtent;
	private static List<GWTContact> currentlyHighlighted = new ArrayList<>();
	
	private final java.util.Map<String, ArcGIS93Rest> wmsLayers = new HashMap<String, ArcGIS93Rest>();
	private final java.util.Map<String, Layer> layersHashMap= new HashMap<String, Layer>();
	
	private final Projection proj;
	
	LonLat center = new LonLat(8,48);
	
	public PlaceMapWidget(final MainWidget master) {	
		options = new MapOptions();
		options.setNumZoomLevels(20);
		mapWidget = new MapWidget("100%", "360px", options);
		decorator = new VerticalPanel();
		decorator.setStyleName("mapDecorator");
		decorator.add(mapWidget);
		
		proj = new Projection("EPSG:4326");
		
		map = mapWidget.getMap();
		map.setRestrictedExtent(bounds);
		map.setMinMaxZoomLevel(0, 20);
		
		switcher = master.getLayerSwitcher();
		
		OSM tempLayer = OSM.Mapnik("TempLayer");
		
		placesLayer = new Vector("Places");
		
		map.addLayer(tempLayer);
		
		XYZOptions options = new XYZOptions();
		options.setSphericalMercator(true);
		XYZ layer = new XYZ("ArcGIS", "http://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile/${z}/${y}/${x}", options);
//		XYZ layer = new XYZ("ArcGIS", "http://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/${z}/${y}/${x}", options);
		
		map.addLayer(layer);
		map.setBaseLayer(layer);
		
		clickControlOptions = new SelectFeatureOptions();
		placesControl = new SelectFeature(placesLayer, clickControlOptions);
		
		placesControl.setAutoActivate(true);
		
		map.addControl(placesControl);
		map.addLayer(placesLayer);
		
		placesLayer.addVectorFeatureSelectedListener(new VectorFeatureSelectedListener() {
			@Override
			public void onFeatureSelected(FeatureSelectedEvent eventObject) {
				GWTContact c = GWTContact.createGWTContact(eventObject.getVectorFeature().getAttributes().getAttributeAsString(Const.FEATURE_ATTRIBUTE_CONTACT_ID));
				eventObject.getVectorFeature().getStyle().setExternalGraphic("img/push_pin_green.png");	
				master.getAllDialog().showFor(c);
				placesControl.unSelect(eventObject.getVectorFeature());
			}
		});		
		
		map.setRestrictedExtent(bounds);
		map.zoomToExtent(bounds);
		this.zoomToBounds(bounds);
		
		center.transform(proj.getProjectionCode(), map.getProjection());
		map.setCenter(center, 5);
		maxVisibleExtent = map.getExtent();
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
	
	/** Sets the base layer for this ContactMapWidget.
	 * 
	 * @param name
	 */
	public void setBaseLayer(String name) {		
		this.map.setBaseLayer(layersHashMap.get(name));
	}
	
	/** Sets the center of the map and zoom level.
	 * 
	 * @param ll
	 * @param zoom
	 */	
	public void setCenter(LonLat ll, int zoom) {
		this.map.setCenter(ll,zoom);
	}	
	
	/** Adds a {@link WMS} with only one map layer to this ContactMapWidget.
	 * 
	 * @param name
	 * @param url - the URL needed for the WMS layer
	 */
	public void addSingleLayerXYZ(String name, String url) {
		WMSParams wmsParams = new WMSParams();
		wmsParams.setFormat("image/png");
		ArcGIS93Rest layer = new ArcGIS93Rest(name, url, wmsParams);
		wmsLayers.put(name,layer);
		layersHashMap.put(name, layer);			
		this.map.addLayer(layer);
	}
	
	/** Returns the current base layer of this MapWidget.
	 * 
	 * @return
	 */	
	public Layer getBaseLayer(){		
		return this.map.getBaseLayer();
	}

	/** Zooms to a specified bounding box.
	 * 
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
	 * 
	 * @return
	 */
	public Bounds getBounds() {
		return this.map.getExtent().transform(new Projection("EPSG: 900913"), new Projection("EPSG: 4326"));
	}	
	
	/** Returns an array of all layers that have been added to this map.
	 * 
	 * @return
	 */
	public Layer[] getLayers() {
		return map.getLayers();
	}
	
	/**
	 * Prints the given {@link ReducedContact}s on this MapWidget.
	 * 
	 * @param contacts
	 *            - a list of {@link ReducedContact} objects resulting from search
	 */		
	public void printContacts(List<? extends ReducedContact> contacts) {
		Style pointStyle = new Style();		
		for (ReducedContact c : contacts) {
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

		Bounds dataExtent = getVectorLayer().getDataExtent();
		boolean outOfBounds = !maxVisibleExtent.containsBounds(dataExtent, false, true);
		if(!outOfBounds){		
			zoomToBounds(getVectorLayer().getDataExtent());			
		}else{
			this.setCenter(center, 5);
		}
	}
	
	public void PlotPointAll(Boolean plot) {
		Style pointStyle = new Style();		
		Style pointStyle2 = new Style();		
		
		if (plot == true) {
			for (int i=0; i<MaryList.getSize(); i++) {
				ReducedContact c = MaryList.getReducedContact(i);
			
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
			
//			ReducedContact c2 = new ReducedContact("John Was Here", 60, 40);
//			
//			LonLat ll2 = c2.getCoordinate();
//			Point point2 = new Point(ll2.lon(), ll2.lat());
//			point2.transform(proj, new Projection(map.getProjection()));	
//			pointStyle2.setExternalGraphic("img/map_marker_blue.png");
//			pointStyle2.setGraphicSize(10, 17);
//			pointStyle2.setFillOpacity(1.0);
//
//			VectorFeature pointFeature2 = new VectorFeature(point2, pointStyle2);
//			pointFeature2.getAttributes().setAttribute(Const.FEATURE_ATTRIBUTE_CONTACT_ID, c2.getId());
//			pointFeature2.setFeatureId(c2.getId());
//			placesLayer.addFeature(pointFeature2);
//			
//			placesControl.activate();
		}
	}
	
	public void PlotPointDiary(Boolean plot) {
		Style pointStyle = new Style();	
		
		if (plot == true) {
			System.out.println(MaryList.getSize());
			for (int i=0; i<MaryList.getSize(); i++) {
				ReducedContact c = MaryList.getReducedContact(i);
			
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
//			placesControl.activate();
		}
	}
	public void PlotPointsRuskin (Boolean plot) {
		Style pointStyle = new Style();	
		if (plot == true) {
			ReducedContact c = new ReducedContact("John Was Here", 60, 40);
			
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
			
//			placesControl.activate();
		}
	}
	
	/**This method unhighlights all of the currently highlighted contacts.
	 * 
	 */
	public static void clearHighlighted() {	
		for(GWTContact c:currentlyHighlighted){
			placesLayer.getFeatureById(c.getId()).getStyle().setExternalGraphic("img/push_pin_red.png");
		}
	}
	
//	/**This method unhighlights all of the currently highlighted contacts.
//	 * 
//	 */
//	public static void clearRuskinLayerHighlighted() {	
//		for(GWTContact c:currentlyHighlighted){
//			ruskinVectorLayer.getFeatureById(c.getId()).getStyle().setExternalGraphic("img/push_pin_red.png");
//		}
//	}
//	
//	/**This method unhighlights all of the currently highlighted contacts.
//	 * 
//	 */
//	public static void clearAllLayerHighlighted() {	
//		for(GWTContact c:currentlyHighlighted){
//			if(c.getAuthor().matches("John Ruskin")) {
//				allVectorLayer.getFeatureById(c.getId()).getStyle().setExternalGraphic("img/push_pin_red.png");
//			}
//			else {
//				allVectorLayer.getFeatureById(c.getId()).getStyle().setExternalGraphic("img/push_pin_red.png");
//			}
//		}
//	}
	
	public List<GWTContact> getCurrentlyHighlighted() {
		return currentlyHighlighted;
	}

	@Override
	public Widget asWidget() {
		return decorator;
	}
}


