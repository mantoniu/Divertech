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
        ListEvent mock = new ListEvent();
        mock.mock();
        Log.d("test",mock.toString());
        ListView listView = ((ListView)findViewById(R.id.listView));
        listView.setAdapter(new ListEventAdapter(getContext(),mock));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(getContext(), EventActivity.class);
                intent.putExtra("event", (Parcelable) mock.get(view.getTag()));
                startActivity(intent);
            }
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