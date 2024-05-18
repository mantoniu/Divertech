package Si3.divertech;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Si3.divertech.events.Event;
import Si3.divertech.events.EventActivitiesFactory;
import Si3.divertech.events.EventList;
import Si3.divertech.map.PopupMarker;
import Si3.divertech.map.PopupMarkerFactory;
import Si3.divertech.users.UserData;

public class MapActivity extends AppCompatActivity implements ClickableActivity {

    private String eventId;

    MapView mapFragment = null;

    Map<Marker, Integer> markers = new HashMap<>();

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        eventId = getIntent().getStringExtra(getString(R.string.event_id));

        Bundle b = new Bundle();
        b.putInt("page", 2);
        FootMenu f = new FootMenu();
        f.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.footMenu, f).commit();

        mapFragment = findViewById(R.id.map);
        mapFragment.onCreate(new Bundle());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        this.gps();
    }


    @Override
    public Context getContext() {
        return getApplicationContext();
    }


    private LatLng getAddress(String eventId) {
        Geocoder geocoder = new Geocoder(getContext());
        try {
            Address address = Objects.requireNonNull(geocoder.getFromLocationName(EventList.getInstance().getEvent(eventId).getFullAddress(), 1)).get(0);
            return new LatLng(address.getLatitude(), address.getLongitude());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void markersAndActualPosition(Location location) {
        mapFragment.getMapAsync(googleMap -> {
            if (location != null) {
                Marker markerSelfPosition = googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                markers.put(markerSelfPosition, PopupMarkerFactory.SELF);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 7f));
            } else
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.52863469527167, 2.43896484375), 4f));

            googleMap.setOnMarkerClickListener(marker -> {
                PopupMarker popup;
                try {
                    popup = PopupMarkerFactory.build(markers.get(marker), getLayoutInflater());
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
                googleMap.setInfoWindowAdapter(popup);
                return false;
            });

            for (Event event : EventList.getInstance().getEvents()) {
                Log.d("antoniu => EVENTID", event.getId());
                LatLng location1 = getAddress(event.getId());
                Marker marker = googleMap.addMarker(new MarkerOptions().position(location1));
                if (marker != null) {
                    marker.setTag(event.getId());
                    markers.put(marker, PopupMarkerFactory.EVENT);
                    if (event.getId().equals(eventId)) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 10f));
                    }
                } else {
                    Toast.makeText(getContext(), "Problème lors de la création d'un markeur", Toast.LENGTH_LONG).show();
                }
            }
            googleMap.setOnInfoWindowClickListener(marker -> {
                if (marker.getTag() != null) {
                    Intent intent = new Intent(getContext(), EventActivitiesFactory.getEventActivityClass(UserData.getInstance().getConnectedUser().getUserType()));                    intent.putExtra(getString(R.string.event_id), marker.getTag().toString());
                    startActivity(intent);
                }
            });
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            markersAndActualPosition(null);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this::markersAndActualPosition)
                .addOnFailureListener(e -> markersAndActualPosition(null));
    }

    private void gps() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this::markersAndActualPosition)
                    .addOnFailureListener(e -> markersAndActualPosition(null));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(0, 0);
    }
}





