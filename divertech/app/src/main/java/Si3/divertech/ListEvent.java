package Si3.divertech;

import java.util.ArrayList;

public class ListEvent extends ArrayList<Event> {



    public ListEvent(){
        new ArrayList<>();
    }

    public void mock(){
        add(new Event("Event1",R.drawable.image_default,"Ceci est le premier évenement"));
        add(new Event("Event2",R.drawable.image_default,"Ceci est le deuxième évenement"));
        add(new Event("Event3",R.drawable.image_default,"Ceci est le troisième évenement"));
        add(new Event("Event4",R.drawable.image_default,"Ceci est le quatrième évenement"));
        add(new Event("Event5",R.drawable.image_default,"Ceci est le cinquième évenement"));
    }

    public void addEvent(Event event){
        add(event);
    }

    public void addEvent(String title,int img, String description){
        Event event = new Event(title,img,description);
        addEvent(event);
    }
}
