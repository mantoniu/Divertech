package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import Si3.divertech.qr_reader.CameraPreviewActivity;

public class ListEventActivity extends AppCompatActivity implements ClickableActivity{

    private ListEventAdapter listEventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listevent);
        Bundle b = new Bundle();
        b.putInt("page",3);
        FootMenu f = new FootMenu();
        f.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.footMenu,f).commit();

        ListView listView = (findViewById(R.id.listView));

        listEventAdapter = new ListEventAdapter(getContext(), ListEvent.getUserEventMap());
        listView.setAdapter(listEventAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), EventActivity.class);
            String eventId = String.valueOf(view.getTag());
            intent.putExtra("event", ListEvent.getUserEventMap().get(eventId));
            startActivity(intent);
        });

        findViewById(R.id.button_add).setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), CameraPreviewActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listEventAdapter != null)
            listEventAdapter.notifyDataSetChanged();
    }

    @Override
    public Context getContext(){
        return getApplicationContext();
    }
}