package Si3.divertech;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class Event implements Parcelable {
    protected int id;
    private String title;
    private int image;
    private String desciption;
    private String position;

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in.readInt(),
                    in.readString(),
                    in.readInt(),
                    in.readString(),
                    in.readString());
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Event(Parcel in){

    }
    public Event(int id, String title,int img,String description, String position){
        this.id = id;
        this.title = title;
        this.image = img;
        this.desciption = description;
        this.position = position;
    }

    public String getTitle(){
        return title;
    }

    public int getImage(){
        return image;
    }

    public String getDesciption(){
        return desciption;
    }

    public int getId(){ return id;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(image);
        dest.writeString(desciption);
        dest.writeString(position);
    }

    @Override
    public String toString(){
        return getId()+"test"+ getTitle();
    }
}
