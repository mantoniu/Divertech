package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import Si3.divertech.qr_reader.CameraPreviewActivity;

public class ListEventActivity extends AppCompatActivity implements ClickableActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listevent);
        Bundle b = new Bundle();
        b.putInt("page",3);
        FootMenu f = new FootMenu();
        f.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.footMenu,f).commit();

        Log.d("divertech","listEvent =" + ListEvent.getEventMap());

        ListView listView = (findViewById(R.id.listView));
        listView.setAdapter(new ListEventAdapter(getContext(), ListEvent.getEventMap()));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), EventActivity.class);
            String eventId = String.valueOf(view.getTag());
            Log.d("VIEWTAG", eventId);
            Log.d("TEST", String.valueOf(ListEvent.getEventMap().get(eventId)));
            intent.putExtra("event", ListEvent.getEventMap().get(eventId));
            startActivity(intent);
        });

        findViewById(R.id.button_add).setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), CameraPreviewActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public Context getContext(){
        return getApplicationContext();
    }
}