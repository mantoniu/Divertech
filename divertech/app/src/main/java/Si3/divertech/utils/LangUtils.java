package Si3.divertech.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import Si3.divertech.R;

public class LangUtils {

    public static void changeLang(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration();

        config.setLocale(locale);

        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public static void changeActionBarTitle(Context context) {
        ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();

        if(actionBar != null && actionBar.getTitle() != null) {
            if(actionBar.getTitle().equals("Reporting") || actionBar.getTitle().equals("Signalement")) {
                actionBar.setTitle(R.string.signalement);
            }
            else if(actionBar.getTitle().equals("My events") || actionBar.getTitle().equals("Mes évènements") ){
                actionBar.setTitle(R.string.events);
            }
            else if(actionBar.getTitle().equals("My Map") || actionBar.getTitle().equals("Ma Carte") ){
                actionBar.setTitle(R.string.ma_carte);
            }

        }
    }
}
