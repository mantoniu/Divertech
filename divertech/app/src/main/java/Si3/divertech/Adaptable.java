package Si3.divertech;

import android.os.Parcelable;

public interface Adaptable extends Parcelable {
    String getId();

    String getPictureUrl();

    String getTitle();

    String getShortDescription();
}
