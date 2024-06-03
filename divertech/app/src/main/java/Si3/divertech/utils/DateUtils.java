package Si3.divertech.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {
    public static String formatZonedDate(ZonedDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy à HH:mm", Locale.getDefault()));
    }

    public static String formatZonedDateWithoutTime(ZonedDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault()));
    }


    public static ZonedDateTime parseString(String date) {
        return ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
}
