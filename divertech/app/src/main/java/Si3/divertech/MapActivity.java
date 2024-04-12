package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements ClickableActivity, OnMapReadyCallback {

    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle b = new Bundle();
        b.putInt("page",2);
        FootMenu f = new FootMenu();
        f.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.footMenu,f).commit();


        MapView mapFragment = findViewById(R.id.map);
        mapFragment.onCreate(new Bundle());
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos",-1);

    }


    @Override
    public Context getContext(){
        return getApplicationContext();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        ListEvent listEvent = new ListEvent();
        Address address;
        listEvent.mock();
        for(Event event : listEvent.values()) {
            address = getAdress(event);
            LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(event.getTitle()+event.getShortDesciption()));
        }
        if(pos == -1)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.52863469527167,2.43896484375),5.3f));
        else {
            address = getAdress(Objects.requireNonNull(listEvent.get(pos)));
            LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
        }
    }

    private Address getAdress(Event event){
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
           address = Objects.requireNonNull(geocoder.getFromLocationName(event.getPlace(), 1)).get(0);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
        //}
        return address;
    }


}



