package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    }

    @Override
    public Context getContext(){
        return getApplicationContext();
    }
}