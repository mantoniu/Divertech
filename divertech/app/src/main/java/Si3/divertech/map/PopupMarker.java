package Si3.divertech.map;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import Si3.divertech.R;

public abstract class PopupMarker implements GoogleMap.InfoWindowAdapter {

    View customPopUp;

    public PopupMarker(LayoutInflater layoutInflater, int view) {
        super();
        this.customPopUp = layoutInflater.inflate(view, null);;
    }
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

    public abstract void addInfoMarker(Marker marker);
}
