package Si3.divertech;

import android.content.Context;
import android.content.Intent;

public interface ClickableActivity {
    Context getContext();

    void startActivity(Intent intent);
    default void onCick(int page){
        Intent i;
        switch (page){
            case 1 : {
                i = new Intent(getContext(),MultiPagesAdminActivity.class);
                i.putExtra("type",EventActivity.REPORTING);
                break;
            }
            case 2 :{
                i = new Intent(getContext(),MultiPagesAdminActivity.class);
                i.putExtra("type",EventActivity.OBJET);
                break;
            }
            case 3 : {
                i = new Intent(getContext(),ListEventActivity.class);
                break;
            }
            default: i = new Intent(getContext(),MainActivity.class);
        }
        startActivity(i);
    }
}
