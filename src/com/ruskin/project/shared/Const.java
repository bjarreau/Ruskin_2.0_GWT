package com.ruskin.project.shared;

public class Const {
	public static final String KEY_WMS_LAYER_NAMES = "ArcGIS";
	
	public static final String KEY_WMS_BASE_LAYER = "http://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile/${z}/${y}/${x}";
	
	public static final String KEY_FILE_NAME = "data/places_visited_by_mary.xml";
	
	public static final String FEATURE_ATTRIBUTE_CONTACT_ID = "contact_id";

	/** prevent instantiation */
	private Const() {}

}
