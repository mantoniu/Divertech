package Si3.divertech;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Event implements Adaptable {
    protected String id;
    private String title;
    private String pictureUrl;
    private String shortDesciption;
    private String place;
    private String description;

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString());
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Event(Parcel in){

    }

    public Event(String id, String title, String pictureUrl, String shortDescription, String position, String description) {
        this.id = id;
        this.title = title;
        this.pictureUrl = pictureUrl;
        this.shortDesciption = shortDescription;
        this.place = position;
        this.description = description;
    }

    public String getTitle(){
        return title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getShortDescription() {
        return shortDesciption;
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
        dest.writeString(shortDesciption);
        dest.writeString(place);
        dest.writeString(description);
    }

    @NonNull
    @Override
    public String toString(){
        return getId()+"test"+ getTitle();
    }
}
