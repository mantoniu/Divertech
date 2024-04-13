package Si3.divertech;

import java.util.HashMap;
import java.util.Map;

public class ListEvent {
    private static final String DESCRIPTION = "\n" +
            "\n" +
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean elit justo, tincidunt ut consequat in, egestas sed magna. Donec et elementum nulla. Nullam nec imperdiet velit. Maecenas lacus elit, cursus id vehicula et, commodo et lectus. Nullam sed urna malesuada sapien lacinia euismod a id tortor. Nullam iaculis mollis orci, vel suscipit urna egestas ac. Nullam quis nibh tristique, placerat lectus quis, elementum mauris. Maecenas sagittis, enim eget commodo pharetra, nulla dolor imperdiet leo, vel facilisis ligula diam sit amet mi. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;\n" +
            "\n" +
            "Integer vitae arcu sit amet nisi pulvinar ornare. Phasellus feugiat porta orci, id pulvinar nulla. Duis mollis laoreet tortor a sollicitudin. Etiam eleifend feugiat velit, a viverra libero. Sed purus magna, suscipit quis vulputate ut, pharetra non urna. Maecenas ante elit, porttitor sed suscipit ac, pretium ut odio. Maecenas finibus ex eget facilisis luctus. Phasellus id leo eu elit pharetra ultrices. Pellentesque ac risus mauris. Proin sit amet sollicitudin nulla, vel vulputate urna. Vestibulum gravida, sem quis tincidunt posuere, ante mauris sodales augue, ut ornare leo nulla ut ex. Mauris congue facilisis nisl eget tempus.\n" +
            "\n" +
            "Morbi sed est ac ante molestie luctus at et turpis. Aenean tincidunt est nec ex interdum tincidunt. Sed blandit gravida mauris quis ullamcorper. Donec non accumsan turpis, ac eleifend metus. Donec accumsan massa non auctor viverra. Phasellus sed interdum magna. Suspendisse potenti. Aliquam quis sem faucibus, posuere tellus non, molestie nunc. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Proin lacinia at felis quis dignissim. In sagittis hendrerit magna, quis accumsan quam malesuada vitae. Vivamus consequat sollicitudin laoreet.\n" +
            "\n" +
            "Etiam quis urna velit. Quisque tristique sapien vel venenatis laoreet. Quisque feugiat est et leo cursus vestibulum. Suspendisse elit ipsum, pharetra vehicula sollicitudin id, tincidunt vel libero. Quisque maximus turpis ac ex suscipit consectetur. Cras dictum tempor ultricies. Sed eu turpis eget dolor vulputate fringilla nec quis quam.\n" +
            "\n" +
            "Fusce scelerisque, dui ac scelerisque mollis, nulla mauris porta nulla, ac scelerisque orci erat sit amet diam. In blandit quam turpis. Nam vehicula et est nec mattis. Donec tincidunt neque metus, eget tempor libero euismod a. Morbi aliquet velit eget luctus facilisis. Duis id pulvinar elit. Pellentesque ac ipsum maximus, molestie nisi sed, tristique ipsum. Proin eget semper sapien, in venenatis ligula. Aliquam eget ligula a felis sollicitudin aliquet. Nulla facilisi. Sed pulvinar eros ipsum, eu aliquet odio tempus sit amet. In congue, augue at interdum tincidunt, ligula est imperdiet nisi, sit amet efficitur diam ante eu enim. Etiam pharetra tristique diam, nec bibendum libero suscipit vehicula. Duis imperdiet malesuada sem eget consectetur. Nulla consequat leo quam, nec interdum erat semper eget. Curabitur efficitur eros egestas nibh facilisis rutrum. ";

    private static final Event event0 = new Event("0", "Event1", R.drawable.image_default, "Ceci est le premier évenement et c'est un long texte pour tester que ça marche bien", "83 cours Franklin Roosevelt, Marseille, 13009", DESCRIPTION);
    private static final Event event1 = new Event("1", "Event2", R.drawable.image_default, "Ceci est le deuxième évenement", "30 rue du Château, Saint-Étienne, 42100", DESCRIPTION);
    private static final Event event2 = new Event("2", "Event3", R.drawable.image_default, "Ceci est le troisième évenement", "41 avenue de Provence, Valenciennes, 59300", DESCRIPTION);
    private static final Event event3 = new Event("3", "Event4", R.drawable.image_default, "Ceci est le quatrième évenement", "60 Rue Joseph Vernet, Bagneux, 92220", DESCRIPTION);
    private static final Event event4 = new Event("4", "Event5", R.drawable.image_default, "Ceci est le cinquième évenement", "96 Avenue Millies Lacroix, Eaubonne, 95600", DESCRIPTION);
    private static final Map<String, Event> eventMap = new HashMap<String, Event>() {{
        put(event0.getId(), event0);
        put(event1.getId(), event1);
        put(event2.getId(), event2);
        put(event3.getId(), event3);
        put(event4.getId(), event4);
    }};

    private static final Map<String, Event> userEventMap = new HashMap<String, Event>() {{
        put(event2.getId(), event2);
        put(event4.getId(), event4);
    }};


    public ListEvent(){
    }

    public static Map<String, Event> getEventMap(){
        return eventMap;
    }

    public static void addEvent(Event event) {
        eventMap.put(event.getId(),event);
    }

    public static void addEvent(String id, String title, int img, String shortDescription, String position, String description) {
        Event event = new Event(id, title, img, shortDescription, position, description);
        addEvent(event);
    }

    public static Map<String, Event> getUserEventMap() {
        return userEventMap;
    }

    public static void addUserEvent(String eventId) {
        if (eventMap.containsKey(eventId))
            userEventMap.put(eventId, eventMap.get(eventId));
    }
}
