package Si3.divertech;

import android.content.Context;
import android.content.Intent;

public interface ClickableActivity {
    Context getContext();

    void startActivity(Intent intent);

    void overridePendingTransition(int resId1, int resId2);

    void finish();
    default void onCick(int page, int currentPage){
        Intent i = null;
        switch (page){
            case 1 : {
                if(currentPage != 1) i = new Intent(getContext(),MainActivity.class);
                break;
            }
            case 2 :{
                if(currentPage != 2) i = new Intent(getContext(), MapActivity.class);
                break;
            }
            case 3 : {
                if(currentPage != 3) i = new Intent(getContext(),ListEventActivity.class);
                break;
            }
            default:
                i = new Intent(getContext(), MainActivity.class);
        }
        if (i != null) {
            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
            overridePendingTransition(0, 0);
            finish();
        }
    }
}
