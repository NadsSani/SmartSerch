package com.starwings.app.smartserchapp.fragments;

import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.starwings.app.smartserchapp.R;
import com.starwings.app.smartserchapp.Utils.GeocodingLocation;

import java.io.IOException;

/**
 * Created by user on 29-11-2017.
 */

public class MapFragment extends SupportMapFragment implements GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private String location,name;
    private final int[] MAP_TYPES = { GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE };
    private int curMapTypeIndex = 0;

    private LatLng locationValue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        location=getArguments().getString("Location");
        name=getArguments().getString("Name");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        GeocodingLocation locationAddress = new GeocodingLocation();
        locationAddress.getAddressFromLocation(location,
                getContext(), new GeocoderHandler());

    }
    private void initListeners() {
        getMap().setOnMarkerClickListener(this);
        getMap().setOnMapLongClickListener(this);
        getMap().setOnInfoWindowClickListener( this );
        getMap().setOnMapClickListener(this);
        initCamera( );
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
           callInitiateMap(locationAddress);
        }
    }

    private void callInitiateMap(String latlng) {
        if(latlng.trim().toUpperCase().equals("NA"))
        {
            return;
        }
        String[] latlngstring=latlng.split(",");
        locationValue=new LatLng(Double.parseDouble(latlngstring[0].trim()),Double.parseDouble(latlngstring[1].trim()));

        mGoogleApiClient = new GoogleApiClient.Builder( getActivity() )
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener( this )
                .addApi( LocationServices.API )
                .build();
        mGoogleApiClient.connect();
        initListeners();
    }


    @Override
    public void onConnected(Bundle bundle) {
//        mCurrentLocation = LocationServices
//                .FusedLocationApi
//                .getLastLocation( mGoogleApiClient );


    }
    private void initCamera() {
        CameraPosition position = CameraPosition.builder()
                .target( new LatLng( locationValue.latitude,
                        locationValue.longitude ) )
                .zoom( 16f )
                .bearing( 0.0f )
                .tilt( 0.0f )
                .build();

        getMap().animateCamera( CameraUpdateFactory
                .newCameraPosition( position ), null );

        getMap().setMapType( GoogleMap.MAP_TYPE_NORMAL );
        getMap().setTrafficEnabled( true );
        getMap().setMyLocationEnabled( true );
        getMap().getUiSettings().setZoomControlsEnabled( true );
        MarkerOptions options = new MarkerOptions().position( new LatLng( locationValue.latitude,
                locationValue.longitude ) );
        options.title( getAddressFromLatLng( new LatLng( locationValue.latitude,
                locationValue.longitude ) ) );

        options.icon( BitmapDescriptorFactory.defaultMarker() );
        getMap().addMarker( options );
    }
    @Override
    public void onMapClick(LatLng latLng) {

//        MarkerOptions options = new MarkerOptions().position( latLng );
//        options.title( getAddressFromLatLng( latLng ) );
//
//        options.icon( BitmapDescriptorFactory.defaultMarker() );
//        getMap().addMarker( options );
    }
    @Override
    public void onMapLongClick(LatLng latLng) {
        MarkerOptions options = new MarkerOptions().position( latLng );
        options.title( getAddressFromLatLng( latLng ) );

        options.icon( BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource( getResources(),
                        R.mipmap.ic_launcher ) ) );

        getMap().addMarker( options );
    }
    private String getAddressFromLatLng( LatLng latLng ) {
        Geocoder geocoder = new Geocoder( getActivity() );

        String address = "";
        try {
            address = geocoder
                    .getFromLocation( latLng.latitude, latLng.longitude, 1 )
                    .get( 0 ).getAddressLine( 0 );
        } catch (IOException e ) {
        }

        return address;
    }
    private void drawCircle( LatLng location ) {
        CircleOptions options = new CircleOptions();
        options.center( location );
        //Radius in meters
        options.radius( 10 );
        options.fillColor( getResources()
                .getColor( R.color.colorPrimaryAlpha ) );
        options.strokeColor( getResources()
                .getColor( R.color.colorPrimary ) );
        options.strokeWidth( 10 );
        getMap().addCircle(options);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    private void drawPolygon( LatLng startingLocation ) {
        LatLng point2 = new LatLng( startingLocation.latitude + .001,
                startingLocation.longitude );
        LatLng point3 = new LatLng( startingLocation.latitude,
                startingLocation.longitude + .001 );

        PolygonOptions options = new PolygonOptions();
        options.add( startingLocation, point2, point3 );

        options.fillColor( getResources()
                .getColor( R.color.colorPrimaryAlpha ) );
        options.strokeColor( getResources()
                .getColor( R.color.colorPrimary ) );
        options.strokeWidth( 10 );

        getMap().addPolygon( options );
    }
    private void drawOverlay( LatLng location, int width, int height ) {
        GroundOverlayOptions options = new GroundOverlayOptions();
        options.position( location, width, height );

        options.image( BitmapDescriptorFactory
                .fromBitmap( BitmapFactory
                        .decodeResource( getResources(),
                                R.mipmap.ic_launcher ) ) );
        getMap().addGroundOverlay( options );
    }
    @Override
    public void onConnectionSuspended(int i) {

    }
}
