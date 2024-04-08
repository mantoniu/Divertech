package Si3.divertech;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListEvent extends HashMap<Integer, Event> {



    public ListEvent(){
        new HashMap<Integer,Event>();
    }

    public void mock(){
        put(1,new Event(1,"Event1",R.drawable.image_default,"Ceci est le premier évenement et c'est un long texte pour tester que ça marche bien","1 rue du noyé"));
        put(2,new Event(2,"Event2",R.drawable.image_default,"Ceci est le deuxième évenement","1 rue du noyé"));
        put(3,new Event(3,"Event3",R.drawable.image_default,"Ceci est le troisième évenement","1 rue du noyé"));
        put(4,new Event(4,"Event4",R.drawable.image_default,"Ceci est le quatrième évenement", "1 rue du noyé"));
        put(5,new Event(5,"Event5",R.drawable.image_default,"Ceci est le cinquième évenement","1 rue du noyé"));
    }

    public void addEvent(Event event){put(event.getId(),event);
    }

    public void addEvent(int id,String title,int img, String description, String position){
        Event event = new Event(id, title,img,description,position);
        addEvent(event);
    }
}
