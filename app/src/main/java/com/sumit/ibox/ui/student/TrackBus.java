package com.sumit.ibox.ui.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sumit.ibox.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class TrackBus extends AppCompatActivity {

    Context context;
    MapView mapView;
    WebView wv;
    MyLocationNewOverlay locationNewOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_bus);
        init();
        setUpToolbar();
        setUpMap();
        setUpWebView();
    }

    private void init() {
        context = this;
        mapView = findViewById(R.id.map_view);
        wv = findViewById(R.id.web_view);
    }

    private void setUpToolbar() {
        Toolbar syllabusToolbar;
        syllabusToolbar = findViewById(R.id.trackBusToolbar);
        syllabusToolbar.setTitle(R.string.track_bus);
        syllabusToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        syllabusToolbar.setNavigationIcon(R.drawable.arrow_left);
        syllabusToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpMap() {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        mapView.setUseDataConnection(true);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        final IMapController mapController = mapView.getController();
        mapController.setZoom(16);
        GpsMyLocationProvider gpsMyLocationProvider = new GpsMyLocationProvider(context);
        locationNewOverlay = new MyLocationNewOverlay(gpsMyLocationProvider, mapView);
        locationNewOverlay.enableMyLocation();
        locationNewOverlay.enableFollowLocation();
//        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_bus);
//        locationNewOverlay.setPersonIcon(icon);
        mapView.getOverlays().add(locationNewOverlay);
        locationNewOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                GeoPoint geoPoint = locationNewOverlay.getMyLocation();
                Log.println(Log.ASSERT,"getLatitude", String.valueOf(geoPoint.getLatitude()));
                Log.println(Log.ASSERT,"getLongitude", String.valueOf(geoPoint.getLongitude()));
                mapView.getOverlays().clear();
                mapView.getOverlays().add(locationNewOverlay);
                mapController.animateTo(geoPoint);
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebView() {
//        String link = "<iframe scrolling=\"no\" frameborder=\"0\" allowfullscreen webkitallowfullscreen mozallowfullscreen allow=\"autoplay; fullscreen\" src=\"https://w3.cdn.anvato.net/player/prod/v3/anvload.html?key=eyJtIjoiTElOIiwidiI6ImFkc3RNT2s2ZFpCdmRLNHgiLCJhbnZhY2siOiJxTjF6ZHkxdzBnZ042VW1uN3Nna3VWZDdxMkFXa0pvOSIsInNoYXJlTGluayI6Imh0dHBzOi8vd3d3Lndvb2R0di5jb20vbGl2ZS1zdHJlYW1pbmctdmlkZW8vIiwicGx1Z2lucyI6eyJjb21zY29yZSI6eyJzY3JpcHQiOiJodHRwOi8vdzMuY2RuLmFudmF0by5uZXQvcGxheWVyL3Byb2QvdjMvcGx1Z2lucy9jb21zY29yZS9jb21zY29yZXBsdWdpbi5taW4uanMiLCJjbGllbnRJZCI6IjYwMzY0MzkiLCJ1c2VEZXJpdmVkTWV0YWRhdGEiOnRydWUsIm1hcHBpbmciOnsidmlkZW8iOnsiYzMiOiJodHRwczovL3d3dy53b29kdHYuY29tLyIsImM0IjoiKm51bGwiLCJjNiI6IipudWxsIiwibnNfc3RfY2wiOiIwIiwibnNfc3RfcHIiOiJ7e1RJVExFfX0iLCJuc19zdF9lcCI6Int7RVBJU09ERX19IiwibnNfc3RfZ2UiOiJOZXdzIiwibnNfc3Rfc3QiOiJ3b29kIiwibnNfc3RfcHUiOiJOZXhzdGFyIiwibnNfc3RfZW4iOiIqbnVsbCIsIm5zX3N0X3NuIjoiKm51bGwiLCJuc19zdF9pYSI6IipudWxsIiwibnNfc3RfY2UiOiIqbnVsbCIsIm5zX3N0X2RkdCI6IipudWxsIiwibnNfc3RfdGR0IjoiKm51bGwifSwiYWQiOnsibnNfc3RfY2wiOiIwIn19fSwiZ29vZ2xlQW5hbHl0aWNzIjp7InRyYWNraW5nSWQiOiJVQS0zMjUwNzM2OC0xOSIsImV2ZW50cyI6eyJWSURFT19TVEFSVEVEIjp7ImFsaWFzIjoiTGl2ZSBTdHJlYW0gLSB2aWRlb0NvbnRlbnRQbGF5IiwiY2F0ZWdvcnkiOiJMaXZlIFN0cmVhbSJ9LCJWSURFT19DT01QTEVURUQiOnsiYWxpYXMiOiJMaXZlIFN0cmVhbSAtIHZpZGVvQ29tcGxldGUiLCJjYXRlZ29yeSI6IkxpdmUgU3RyZWFtIn0sIlVTRVJfUEFVU0UiOnsiYWxpYXMiOiJMaXZlIFN0cmVhbSAtIHZpZGVvUGF1c2UiLCJjYXRlZ29yeSI6IkxpdmUgU3RyZWFtIn0sIlZJREVPX1ZJRVdFRCI6eyJhbGlhcyI6IkxpdmUgU3RyZWFtIC0gdmlkZW9WaWV3Q2hlY2twb2ludCIsImNhdGVnb3J5IjoiTGl2ZSBTdHJlYW0ifX19LCJkZnAiOnsiY2xpZW50U2lkZSI6eyJhZFRhZ1VybCI6Imh0dHBzOi8vcHViYWRzLmcuZG91YmxlY2xpY2submV0L2dhbXBhZC9saXZlL2Fkcz9zej05eDEwMDAmaXU9LzU2NzgvbGluLndvb2QvbGl2ZXN0cmVhbSZjaXVfc3pzPTMwMHgyNTAmaW1wbD1zJmdkZnBfcmVxPTEmZW52PXZwJm91dHB1dD12YXN0JnVudmlld2VkX3Bvc2l0aW9uX3N0YXJ0PTEmY29ycmVsYXRvcj03MTA1JnBtbmQ9MCZwbXhkPTM2MDAwMCZwbWFkPTEmdXJsPVtyZWZlcnJlcl91cmxdJmRlc2NyaXB0aW9uX3VybD1bZGVzY3JpcHRpb25fdXJsXSZjb3JyZWxhdG9yPVt0aW1lc3RhbXBdIn0sImxpYnJhcnlSZXF1ZXN0ZWQiOnRydWV9LCJoZWFsdGhBbmFseXRpY3MiOnt9fSwiaHRtbDUiOnRydWUsInRva2VuIjoiVk5KMFJfbEtmdk5GcDN3dFhxSXd0eXFVZ0dwSGtoVDRDQkVfdVVLUTVNVX5NbjR3ZmcifQ%3D%3D\"  width =\"640\" height=\"360\"></iframe>";
//        wv.loadUrl(link);
//        WebSettings settings = wv.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        settings.setAppCacheEnabled(true);
//        settings.setDomStorageEnabled(true);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        settings.setUseWideViewPort(true);
//        settings.setSavePassword(true);
//        settings.setSaveFormData(true);
//        wv.setWebViewClient(new MyWebViewClient());
        String video2 = "<iframe scrolling=\"no\" frameborder=\"0\" allowfullscreen webkitallowfullscreen mozallowfullscreen allow=\"autoplay; fullscreen\" src=\"https://w3.cdn.anvato.net/player/prod/v3/anvload.html?key=eyJtIjoiTElOIiwidiI6ImFkc3RNT2s2ZFpCdmRLNHgiLCJhbnZhY2siOiJxTjF6ZHkxdzBnZ042VW1uN3Nna3VWZDdxMkFXa0pvOSIsInNoYXJlTGluayI6Imh0dHBzOi8vd3d3Lndvb2R0di5jb20vbGl2ZS1zdHJlYW1pbmctdmlkZW8vIiwicGx1Z2lucyI6eyJjb21zY29yZSI6eyJzY3JpcHQiOiJodHRwOi8vdzMuY2RuLmFudmF0by5uZXQvcGxheWVyL3Byb2QvdjMvcGx1Z2lucy9jb21zY29yZS9jb21zY29yZXBsdWdpbi5taW4uanMiLCJjbGllbnRJZCI6IjYwMzY0MzkiLCJ1c2VEZXJpdmVkTWV0YWRhdGEiOnRydWUsIm1hcHBpbmciOnsidmlkZW8iOnsiYzMiOiJodHRwczovL3d3dy53b29kdHYuY29tLyIsImM0IjoiKm51bGwiLCJjNiI6IipudWxsIiwibnNfc3RfY2wiOiIwIiwibnNfc3RfcHIiOiJ7e1RJVExFfX0iLCJuc19zdF9lcCI6Int7RVBJU09ERX19IiwibnNfc3RfZ2UiOiJOZXdzIiwibnNfc3Rfc3QiOiJ3b29kIiwibnNfc3RfcHUiOiJOZXhzdGFyIiwibnNfc3RfZW4iOiIqbnVsbCIsIm5zX3N0X3NuIjoiKm51bGwiLCJuc19zdF9pYSI6IipudWxsIiwibnNfc3RfY2UiOiIqbnVsbCIsIm5zX3N0X2RkdCI6IipudWxsIiwibnNfc3RfdGR0IjoiKm51bGwifSwiYWQiOnsibnNfc3RfY2wiOiIwIn19fSwiZ29vZ2xlQW5hbHl0aWNzIjp7InRyYWNraW5nSWQiOiJVQS0zMjUwNzM2OC0xOSIsImV2ZW50cyI6eyJWSURFT19TVEFSVEVEIjp7ImFsaWFzIjoiTGl2ZSBTdHJlYW0gLSB2aWRlb0NvbnRlbnRQbGF5IiwiY2F0ZWdvcnkiOiJMaXZlIFN0cmVhbSJ9LCJWSURFT19DT01QTEVURUQiOnsiYWxpYXMiOiJMaXZlIFN0cmVhbSAtIHZpZGVvQ29tcGxldGUiLCJjYXRlZ29yeSI6IkxpdmUgU3RyZWFtIn0sIlVTRVJfUEFVU0UiOnsiYWxpYXMiOiJMaXZlIFN0cmVhbSAtIHZpZGVvUGF1c2UiLCJjYXRlZ29yeSI6IkxpdmUgU3RyZWFtIn0sIlZJREVPX1ZJRVdFRCI6eyJhbGlhcyI6IkxpdmUgU3RyZWFtIC0gdmlkZW9WaWV3Q2hlY2twb2ludCIsImNhdGVnb3J5IjoiTGl2ZSBTdHJlYW0ifX19LCJkZnAiOnsiY2xpZW50U2lkZSI6eyJhZFRhZ1VybCI6Imh0dHBzOi8vcHViYWRzLmcuZG91YmxlY2xpY2submV0L2dhbXBhZC9saXZlL2Fkcz9zej05eDEwMDAmaXU9LzU2NzgvbGluLndvb2QvbGl2ZXN0cmVhbSZjaXVfc3pzPTMwMHgyNTAmaW1wbD1zJmdkZnBfcmVxPTEmZW52PXZwJm91dHB1dD12YXN0JnVudmlld2VkX3Bvc2l0aW9uX3N0YXJ0PTEmY29ycmVsYXRvcj03MTA1JnBtbmQ9MCZwbXhkPTM2MDAwMCZwbWFkPTEmdXJsPVtyZWZlcnJlcl91cmxdJmRlc2NyaXB0aW9uX3VybD1bZGVzY3JpcHRpb25fdXJsXSZjb3JyZWxhdG9yPVt0aW1lc3RhbXBdIn0sImxpYnJhcnlSZXF1ZXN0ZWQiOnRydWV9LCJoZWFsdGhBbmFseXRpY3MiOnt9fSwiaHRtbDUiOnRydWUsInRva2VuIjoiVk5KMFJfbEtmdk5GcDN3dFhxSXd0eXFVZ0dwSGtoVDRDQkVfdVVLUTVNVX5NbjR3ZmcifQ%3D%3D\"  width =\"100%\" height=\"100%\"></iframe>";
        wv.getSettings().setPluginState(WebSettings.PluginState.ON);
        wv.setWebChromeClient(new WebChromeClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setHorizontalScrollBarEnabled(false);
        wv.setVerticalScrollBarEnabled(false);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setBuiltInZoomControls(false);
        wv.getSettings().setAppCacheEnabled(true);
        wv.setInitialScale(0);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.loadData(video2,"text/html","UTF-8");
    }

}
