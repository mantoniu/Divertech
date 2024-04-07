package Si3.divertech;

public class Event {
    private String title;
    private int image;
    private String desciption;

    public Event(String title,int img,String description){
        this.title = title;
        this.image = img;
        this.desciption = description;
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

}
