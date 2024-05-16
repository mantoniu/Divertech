package Si3.divertech.feed;

import Si3.divertech.users.UserType;

public class FeedFactory {
    public static Feed createFeed(FeedType feedType, UserType userType) {
        switch (feedType) {
            case EVENTS:
                return new EventFeed();
            case NOTIFICATION:
                switch (userType) {
                    case NORMAL:
                        return new NotificationFeed();
                    case ADMIN:
                        return new AdminNotificationFeed();
                    default:
                        throw new UnsupportedOperationException("This user type is unsupported !" + userType);
                }
            default:
                throw new UnsupportedOperationException("This feed type is unsupported !" + feedType);
        }
    }
}
