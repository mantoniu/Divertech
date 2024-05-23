package Si3.divertech.events;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import Si3.divertech.utils.DateUtils;

public class Event implements Parcelable {
    protected String id;
    private String title;
    private String pictureUrl;
    private String shortDescription;
    private String address;
    private String postalCode;
    private String city;
    private String description;
    private ZonedDateTime date;

    private String organizer;

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
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

    public Event(Parcel in) {

    }

    public Event() {
    }

    public Event(String id, String title, String pictureUrl, String shortDescription, String address, String postalCode, String city, String description, String date, String organizer) {
        this(id, title, pictureUrl, shortDescription, address, postalCode, city, description, ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME), organizer);
    }

    public Event(String id, String title, String pictureUrl, String shortDescription, String address, String postalCode, String city, String description, ZonedDateTime date, String organizer) {
        this.id = id;
        this.title = title;
        this.pictureUrl = pictureUrl;
        this.shortDescription = shortDescription;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.description = description;
        this.date = date;
        this.organizer = organizer;
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

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getOrganizer() {
        return organizer;
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
        dest.writeString(address);
        dest.writeString(postalCode);
        dest.writeString(city);
        dest.writeString(description);
        dest.writeString(date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        dest.writeString(organizer);
    }

    @NonNull
    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", address='" + getFullAddress() + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", organizer id=" + organizer +
                '}';
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void setDate(String date) {
        this.date = DateUtils.parseString(date);
    }
    public String getFormattedDate() {
        return DateUtils.formatZonedDate(date);
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getZonedDate() {
        return date;
    }

    public String getFullAddress() {
        return address + ", " + city + ", " + postalCode;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }
}
