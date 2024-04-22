package Si3.divertech;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Event implements Parcelable {
    protected String id;
    private String title;
    private String pictureUrl;
    private String shortDescription;
    private String place;
    private String description;
    private ZonedDateTime date;

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    ZonedDateTime.parse(in.readString(), DateTimeFormatter.ISO_ZONED_DATE_TIME)
            );
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Event(Parcel in){

    }

    public Event(String id, String title, String pictureUrl, String shortDescription, String position, String description, ZonedDateTime date) {
        this.id = id;
        this.title = title;
        this.pictureUrl = pictureUrl;
        this.shortDescription = shortDescription;
        this.place = position;
        this.description = description;
        this.date = date;
    }

    public String getTitle(){
        return title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getId(){ return id;}
    public String getPlace(){ return place;}

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(pictureUrl);
        dest.writeString(shortDescription);
        dest.writeString(place);
        dest.writeString(description);
        dest.writeString(date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }

    @NonNull
    @Override
    public String toString(){
        return getId()+"test"+ getTitle();
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public ZonedDateTime getZonedDate() {
        return date;
    }
}
