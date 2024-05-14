package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements ClickableActivity, OnMapReadyCallback{
    private String pos;
    DisplayMetrics metrics = new DisplayMetrics();



    class CustomMarkerPopUp implements GoogleMap.InfoWindowAdapter {

        View customPopUp = getLayoutInflater().inflate(R.layout.marker_popup_layout, null);

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
            if (marker.getTag() == null)
                return;

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
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setContentView(R.layout.activity_map);

        pos = getIntent().getStringExtra("pos");

        Bundle b = new Bundle();
        b.putInt("page", 2);
        FootMenu f = new FootMenu();
        f.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.footMenu, f).commit();


        MapView mapFragment = findViewById(R.id.map);
        mapFragment.onCreate(new Bundle());
        mapFragment.getMapAsync(this);
    }


    @Override
    public Context getContext(){
        return getApplicationContext();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Address address;
        googleMap.setInfoWindowAdapter(new CustomMarkerPopUp());
        List<Marker> markers = new ArrayList<>();
        for (Event event : ListEvent.getInstance().getEvents()) {
            address = getAdress(event);
            LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(location));
            if(marker != null) {
                marker.setTag(event.getId());
                markers.add(marker);
            }
        }
        if(pos == null)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.52863469527167,2.43896484375),5.3f));
        else {
            address = getAdress(Objects.requireNonNull(ListEvent.getInstance().getEvent(pos)));
            LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
            markers.stream().filter(marker -> Objects.equals(marker.getTag(), pos)).findFirst().ifPresent(Marker::showInfoWindow);
        }

        googleMap.setOnInfoWindowClickListener(marker -> {
            if (marker.getTag() == null)
                return;

            Intent intent = new Intent(getContext(),EventActivity.class);
            intent.putExtra("event", ListEvent.getInstance().getEvent(marker.getTag().toString()));
            startActivity(intent);
        });
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
           address = Objects.requireNonNull(geocoder.getFromLocationName(event.getPosition(), 1)).get(0);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
        //}
        return address;
    }
}





