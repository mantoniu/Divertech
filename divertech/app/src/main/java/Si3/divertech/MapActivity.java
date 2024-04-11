package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
        listEvent.mock();
        for(Event event : listEvent.values()) {
            // Add a marker in Sydney and move the camera
            LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(event.getTitle()));
        }
        if(pos == -1)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.52863469527167,2.43896484375),5.3f));
        else
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(listEvent.get(pos).getLatitude(), listEvent.get(pos).getLongitude()),13));
    }
}

//TODO check geocoder



