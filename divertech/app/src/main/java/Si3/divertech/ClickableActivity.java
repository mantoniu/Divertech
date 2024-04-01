package Si3.divertech;

import android.content.Context;
import android.content.Intent;

public interface ClickableActivity {
    Context getContext();

    void startActivity(Intent intent);
    default void onCick(int page){
        Intent i;
        switch (page){
            case 1 : i = new Intent(getContext(),MainActivity.class);
            case 2 : i = new Intent(getContext(),MapActivity.class);
            case 3 : i = new Intent(getContext(), ListEventActivity.class);
            default: i = new Intent(getContext(),MainActivity.class);
        }
        startActivity(i);
    }
}
