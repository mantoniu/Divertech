package Si3.divertech;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements ClickableActivity {

    private String pos;
    DisplayMetrics metrics = new DisplayMetrics();

    MapView mapFragment = null;

    /*class CustomMarkerPopUp implements GoogleMap.InfoWindowAdapter {

        View customPopUp = getLayoutInflater().inflate(R.layout.marker_popup_layout, null);
        //
        // View customPopUp = getLayoutInflater().inflate(, null);

        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {
            return null;
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {
            addInfoMarker(marker);
            return customPopUp.getRootView();
        }

        public void addInfoMarker(Marker marker) {
            if (marker.getTag() == null){
                TextView title = customPopUp.findViewById(R.id.title);
                title.setText(R.string.selfPosition);
                ImageView picture = customPopUp.findViewById(R.id.image);
                TextView description = customPopUp.findViewById(R.id.description);
                description.setText("...");
                return;
            }


            if (ListEvent.getInstance().getEvent(marker.getTag().toString()) != null) {
                TextView title = customPopUp.findViewById(R.id.title);
                title.setText(ListEvent.getInstance().getEvent(marker.getTag().toString()).getTitle());
                ImageView picture = customPopUp.findViewById(R.id.image);
                Picasso.get().load(ListEvent.getInstance().getEvent(marker.getTag().toString()).getPictureUrl()).into(picture);
                TextView description = customPopUp.findViewById(R.id.description);
                description.setText(ListEvent.getInstance().getEvent(marker.getTag().toString()).getShortDescription());
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    description.setMaxWidth((int) (metrics.heightPixels / 1.6));
                } else {
                    description.setMaxWidth((int) (metrics.widthPixels / 1.6));
                }
                //description.setLayoutParams(new ConstraintLayout.LayoutParams(metrics.widthPixels-150, ActionBar.LayoutParams.WRAP_CONTENT));
            }
            else {

            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setContentView(R.layout.activity_map);

        View headerView = findViewById(R.id.header_menu);
        ((TextView) headerView.findViewById(R.id.feed_title)).setText(R.string.ma_carte);

        pos = getIntent().getStringExtra("pos");

        Bundle b = new Bundle();
        b.putInt("page", 2);
        FootMenu f = new FootMenu();
        f.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.footMenu, f).commit();

        mapFragment = findViewById(R.id.map);
        mapFragment.onCreate(new Bundle());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        this.gps(fusedLocationClient);
    }


    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    private Address getAdress(Event event) {
        Geocoder geocoder = new Geocoder(getContext());
        Address address;
        // If we want t respect code specification for API >= 33 we must change way to produce map and include choice before crating map because GeocodeListener is async
        //if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        //    Geocoder.GeocodeListener geocodeListener = new Geocoder.GeocodeListener() {
        //        @Override
        //        public void onGeocode(@NonNull List<Address> addresses) {
        //            address = addresses.get(0);
        //        }
        //        @Override
        //        public void onError(String errorMessage){
        //            throw new RuntimeException(errorMessage);
        //        }
        //    };
        //    geocoder.getFromLocationName(event.getPlace(), 1,42,-6,52,9,geocodeListener);
        //} else {
        try {
            address = Objects.requireNonNull(geocoder.getFromLocationName(event.getPosition(), 1)).get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //}
        return address;
    }

    //  ---- GPS --- ---- GPS --- ---- GPS --- ---- GPS --- ---- GPS --- ---- GPS --- ---- GPS --- //
    private FusedLocationProviderClient fusedLocationClient;

    public void markersAndActualPosition(Location location2) {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                LatLng positionActuelle = null;
                if (location2 != null) {
                    positionActuelle = new LatLng(location2.getLatitude(), location2.getLongitude());
                } else {
                    positionActuelle = new LatLng(46.52863469527167, 2.43896484375);
                }

                Address address;
                googleMap.setInfoWindowAdapter(new CustomMarkerPopUp());
                List<Marker> markers = new ArrayList<>();
                for (Event event : ListEvent.getInstance().getEvents()) {
                    address = getAdress(event);
                    LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(location));
                    if (marker != null) {
                        marker.setTag(event.getId());
                        markers.add(marker);
                    }
                }

                Log.d("GPS", "position : " + positionActuelle);
                if(!(positionActuelle.latitude == 46.52863469527167 && positionActuelle.longitude == 2.43896484375)){
                    Marker markerVotrePosition = googleMap.addMarker(new MarkerOptions().position(positionActuelle));
                    markers.add(markerVotrePosition);
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionActuelle, 4f));

                if (pos == null)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionActuelle, 15f));

                else {
                    address = getAdress(Objects.requireNonNull(ListEvent.getInstance().getEvent(pos)));

                    LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
                    markers.stream().filter(marker -> marker.getTag().equals(pos)).findFirst().ifPresent(Marker::showInfoWindow);
                }

                googleMap.setOnInfoWindowClickListener(marker -> {
                    Intent intent = new Intent(getContext(), EventActivity.class);
                    intent.putExtra("event", ListEvent.getInstance().getEvent(marker.getTag().toString()));
                    startActivity(intent);
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            markersAndActualPosition(null);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        markersAndActualPosition(location);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        markersAndActualPosition(null);
                    }
                });
    };

    private void gps(FusedLocationProviderClient fusedLocationProviderClient) {
        LatLng latlng = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            Log.d("GPS", "demande de permission GPS");
        }
        else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            markersAndActualPosition(location);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            markersAndActualPosition(null);
                        }
                    });
        }
    }
}





