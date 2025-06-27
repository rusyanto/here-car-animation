package com.example.caranimation;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.here.sdk.core.Color;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.GeoPolyline;
import com.here.sdk.core.errors.InstantiationErrorException;
import com.here.sdk.mapview.MapImage;
import com.here.sdk.mapview.MapMarker;
import com.here.sdk.mapview.MapPolyline;
import com.here.sdk.mapview.LineCap;
import com.here.sdk.mapview.MapMeasure;
import com.here.sdk.mapview.MapMeasureDependentRenderSize;
import com.here.sdk.mapview.RenderSize;
import com.here.sdk.mapview.MapView;
import com.here.sdk.mapview.MapImageFactory;
import com.here.sdk.routing.CarOptions;
import com.here.sdk.routing.Route;
import com.here.sdk.routing.RoutingError;
import com.here.sdk.routing.RoutingEngine;
import com.here.sdk.routing.Waypoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarAnimationController {

    private final Context context;
    private final MapView mapView;
    private RoutingEngine routingEngine;
    private MapMarker carMarker;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public CarAnimationController(Context context, MapView mapView){
        this.context = context;
        this.mapView = mapView;

        try {
            this.routingEngine = new RoutingEngine();
        } catch (InstantiationErrorException e) {
            Log.e("HERE_SDK", "RoutingEngine initialization failed", e);
        }
    }

    public void startAnimation() {
        GeoCoordinates start = new GeoCoordinates(1.27626, 103.83899); // Example start
        GeoCoordinates end = new GeoCoordinates(1.2768, 103.84562);   // Example end

        List<Waypoint> waypoints = new ArrayList<>();
        waypoints.add(new Waypoint(start));
        waypoints.add(new Waypoint(end));

        routingEngine.calculateRoute(waypoints, new CarOptions(), (RoutingError routingError, List<Route> routes) -> {
            if (routingError == null && routes != null && !routes.isEmpty()) {
                Route route = routes.get(0);
                drawRoute(route);
                placeCarMarker(start);
                animateCarAlongRoute(route);
            }
        });
    }

    private void drawRoute(Route route) {
        GeoPolyline polyline = route.getGeometry();

        try {
            // Create the polyline with style
            MapPolyline routeMapPolyline = new MapPolyline(polyline, createDefaultRouteStyle());

            // Add to the map
            mapView.getMapScene().addMapPolyline(routeMapPolyline);

        } catch (MapPolyline.Representation.InstantiationException e) {
            Log.e("Polyline Error", "SolidRepresentation failed: " + e.error.name());
        } catch (MapMeasureDependentRenderSize.InstantiationException e) {
            Log.e("Polyline Error", "RenderSize failed: " + e.error.name());
        }
    }

    private Map<Double, Double> getDefaultLineWidthValues() {
        Map<Double, Double> zoomWidthMap = new HashMap<>();

        zoomWidthMap.put(6.0, 1.0);
        zoomWidthMap.put(7.0, 1.0);
        zoomWidthMap.put(8.0, 1.5);
        zoomWidthMap.put(9.0, 2.0);
        zoomWidthMap.put(10.0, 2.5);
        zoomWidthMap.put(11.0, 3.0);
        zoomWidthMap.put(12.0, 3.5);
        zoomWidthMap.put(13.0, 4.0);
        zoomWidthMap.put(14.0, 5.0);
        zoomWidthMap.put(15.0, 6.0);
        zoomWidthMap.put(16.0, 7.0);
        zoomWidthMap.put(17.0, 8.0);
        zoomWidthMap.put(18.0, 9.0);
        zoomWidthMap.put(19.0, 10.0);
        zoomWidthMap.put(20.0, 11.0);
        zoomWidthMap.put(21.0, 12.0);
        zoomWidthMap.put(22.0, 13.0);

        return zoomWidthMap;
    }

    private MapPolyline.SolidRepresentation createDefaultRouteStyle() throws MapMeasureDependentRenderSize.InstantiationException, MapPolyline.Representation.InstantiationException {
        MapMeasureDependentRenderSize lineWidth = new MapMeasureDependentRenderSize(
                MapMeasure.Kind.ZOOM_LEVEL,
                RenderSize.Unit.PIXELS,
                getDefaultLineWidthValues()
        );

        double outlineWidth = 1.23 * mapView.getPixelScale();
        MapMeasureDependentRenderSize outlineWidthSize = new MapMeasureDependentRenderSize(
                RenderSize.Unit.PIXELS,
                outlineWidth
        );

        Color lineColor = new Color(0.051f, 0.380f, 0.871f, 1.0f);
        Color outlineColor = new Color(0.043f, 0.325f, 0.749f, 1.0f);

        return new MapPolyline.SolidRepresentation(lineWidth, lineColor, outlineWidthSize, outlineColor, LineCap.ROUND);
    }

    private void placeCarMarker(GeoCoordinates position) {
        MapImage carImage = MapImageFactory.fromResource(context.getResources(), R.drawable.car_icon);
        carMarker = new MapMarker(position, carImage);
        mapView.getMapScene().addMapMarker(carMarker);
    }

    private void animateCarAlongRoute(Route route) {
        List<GeoCoordinates> routePoints = route.getGeometry().vertices;
        long intervalMs = 150;

        // Start from the first point
        final int[] currentIndex = {0};

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (currentIndex[0] < routePoints.size() - 1) {
                    GeoCoordinates to = routePoints.get(currentIndex[0] + 1);

                    // Move the marker to the next point (simple step-based movement)
                    carMarker.setCoordinates(to);
                    currentIndex[0]++;

                    handler.postDelayed(this, intervalMs);
                }
            }
        });
    }
}
