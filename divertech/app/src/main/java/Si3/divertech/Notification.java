package Si3.divertech;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Notification implements Adaptable {
    private String id;
    private String eventId;
    private String pictureUrl;
    private String title;
    private String description;
    private final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {

        @Override
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[0];
        }
    };

    public Notification() {
    }

    public Notification(Parcel in) {
        id = in.readString();
        eventId = in.readString();
        pictureUrl = in.readString();
        title = in.readString();
        description = in.readString();
    }

    public Notification(String id, String eventId, String pictureUrl, String title, String description) {
        this.id = id;
        this.eventId = eventId;
        this.pictureUrl = pictureUrl;
        this.title = title;
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(eventId);
        dest.writeString(pictureUrl);
        dest.writeString(title);
        dest.writeString(description);
    }

    public String getDescription() {
        return description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getEventId() {
        return eventId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getShortDescription() {
        return description;
    }
}
