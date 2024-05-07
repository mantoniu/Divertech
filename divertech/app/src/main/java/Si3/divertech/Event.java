package Si3.divertech;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Event implements Parcelable {
    protected String id;
    private String title;
    private String pictureUrl;
    private String shortDescription;
    private String position;
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
                    in.readString()
            );
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Event(Parcel in){

    }

    public Event() {
    }

    public Event(String id, String title, String pictureUrl, String shortDescription, String position, String description, String date) {
        this(id, title, pictureUrl, shortDescription, position, description, ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }

    public Event(String id, String title, String pictureUrl, String shortDescription, String position, String description, ZonedDateTime date) {
        this.id = id;
        this.title = title;
        this.pictureUrl = pictureUrl;
        this.shortDescription = shortDescription;
        this.position = position;
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

    public String getPosition() {
        return position;
    }

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
        dest.writeString(position);
        dest.writeString(description);
        dest.writeString(date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }

    @NonNull
    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", position='" + position + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void setDate(String date) {
        this.date = ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy à HH:mm", Locale.getDefault()));
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getZonedDate() {
        return date;
    }
}
