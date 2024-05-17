package Si3.divertech.events;

import Si3.divertech.users.UserType;

public class EventActivitiesFactory {
    public static Class<?> getEventActivityClass(UserType userType) {
        switch (userType) {
            case NORMAL:
                return EventActivity.class;
            case ADMIN:
                return AdminEventActivity.class;
            default:
                throw new IllegalArgumentException("Unsupported user type: " + userType);
        }
    }
}
