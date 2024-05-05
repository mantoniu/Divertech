package Si3.divertech;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageSelected {
    public static String LANGUAGE_SELECTED = "fr";

    public static void setLanguage(Context context){
        Locale locale = new Locale(LANGUAGE_SELECTED);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration();

        config.setLocale(locale);

        res.updateConfiguration(config, res.getDisplayMetrics());
    }

}
