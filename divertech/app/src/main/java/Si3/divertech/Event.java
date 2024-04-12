package Si3.divertech;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Event implements Parcelable {
    protected int id;
    private String title;
    private int image;
    private String shortDesciption;
    private String place;
    private String description;



    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in.readInt(),
                    in.readString(),
                    in.readInt(),
                    in.readString(),
                    in.readString(),
                    in.readString());}

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Event(int id, String title,int img,String shortDescription, String position,String description){
        this.id = id;
        this.title = title;
        this.image = img;
        this.shortDesciption = shortDescription;
        this.place = position;
        this.description = description;

    }

    public String getTitle(){
        return title;
    }

    public int getImage(){
        return image;
    }

    public String getShortDesciption(){
        return shortDesciption;
    }

    public int getId(){ return id;}
    public String getPlace(){ return place;}

    public String getDescription(){return description;}



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(image);
        dest.writeString(shortDesciption);
        dest.writeString(place);
        dest.writeString(description);

    }

    @Override
    public String toString(){
        return getId()+"test"+ getTitle();
    }
}
