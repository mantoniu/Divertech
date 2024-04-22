package Si3.divertech;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    private static final Event event0 = new Event("0", "Concert", "https://github.com/mantoniu/Divertech/blob/main/image/event1-cropped.jpg?raw=true", "Ceci est le premier évenement et c'est un long texte pour tester que ça marche bien", "1 rue du noyé", DESCRIPTION, ZonedDateTime.of(LocalDateTime.of(2024, 8, 20, 0, 0), ZoneId.of("UTC+1")));
    private static final Event event1 = new Event("1", "Festival", "https://github.com/mantoniu/Divertech/blob/main/image/event2-cropped.jpg?raw=true", "Ceci est le deuxième évenement", "1 rue du noyé", DESCRIPTION, ZonedDateTime.of(LocalDateTime.of(2024, 8, 13, 20, 0), ZoneId.of("UTC+1")));
    private static final Event event2 = new Event("2", "Film", "https://github.com/mantoniu/Divertech/blob/main/image/event3-cropped.jpg?raw=true", "Ceci est le troisième évenement", "1 rue du noyé", DESCRIPTION, ZonedDateTime.of(LocalDateTime.of(2024, 9, 19, 21, 0), ZoneId.of("UTC+1")));
    private static final Event event3 = new Event("3", "Loto", "https://github.com/mantoniu/Divertech/blob/main/image/event4-cropped.jpg?raw=true", "Ceci est le quatrième évenement", "1 rue du noyé", DESCRIPTION, ZonedDateTime.of(LocalDateTime.of(2024, 7, 24, 14, 30), ZoneId.of("UTC+1")));
    private static final Event event4 = new Event("4", "Marché de Noel", "https://github.com/mantoniu/Divertech/blob/main/image/event5-cropped.jpg?raw=true", "Ceci est le cinquième évenement", "1 rue du noyé", DESCRIPTION, ZonedDateTime.of(LocalDateTime.of(2024, 12, 20, 14, 0), ZoneId.of("UTC+1")));
    private static final Map<String, Event> eventMap = new HashMap<String, Event>() {{
        put("0", event0);
        put("1", event1);
        put("2", event2);
        put("3", event3);
        put("4", event4);
    }};

    private static final Map<String, Event> userEventMap = new HashMap<String, Event>() {{
        put("0", event0);
        put("4", event4);
    }};


    public ListEvent(){
    }

    public static Map<String, Event> getEventMap(){
        return eventMap;
    }

    public static void addEvent(Event event) {
        eventMap.put(event.getId(),event);
    }

    public static void addEvent(String id, String title, String pictureUrl, String shortDescription, String position, String description, ZonedDateTime date) {
        Event event = new Event(id, title, pictureUrl, shortDescription, position, description, date);
        addEvent(event);
    }

    public static Map<String, Event> getUserEventMap() {
        return userEventMap;
    }

    public static void addUserEvent(String eventId) {
        if (eventMap.containsKey(eventId))
            userEventMap.put(eventId, eventMap.get(eventId));
    }

    public static Event getEvent(String id) {
        return eventMap.get(id);
    }
}
