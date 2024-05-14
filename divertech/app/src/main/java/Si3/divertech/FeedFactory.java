package Si3.divertech;

public class FeedFactory {
    public static Feed createFeed(FeedType feedType) {
        switch (feedType) {
            case EVENTS:
                return new EventFeed();
            case NOTIFICATION:
                return new NotificationFeed();
            default:
                throw new UnsupportedOperationException("This feed type is unsupported !");
        }
    }
}
