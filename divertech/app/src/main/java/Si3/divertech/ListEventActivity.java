package Si3.divertech;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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
        ((ListView)findViewById(R.id.listView)).setAdapter(new ListEventAdapter(getContext(),mock));
    }

    @Override
    public Context getContext(){
        return getApplicationContext();
    }
}