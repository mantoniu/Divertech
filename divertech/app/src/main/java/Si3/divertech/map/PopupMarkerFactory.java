package Si3.divertech.map;

import android.view.LayoutInflater;

public abstract class PopupMarkerFactory {
    public static final int EVENT = 1;
    public static final int SELF = 2;

    public static PopupMarker build(int type, LayoutInflater layoutInflater) throws Throwable {
        switch (type) {
            case EVENT:
                return new EventPopupMarker(layoutInflater);
            case SELF:
                return new SelfPopupMarker(layoutInflater);
            default:
                throw new Throwable("Type Error");
        }
    }
}
