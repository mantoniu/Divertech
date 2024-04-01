package Si3.divertech;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements ClickableActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle b = new Bundle();
        b.putInt("page",1);
        FootMenu f = new FootMenu();
        f.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.footMenu,f).commit();
    }

    @Override
    public Context getContext(){
        return getApplicationContext();
    }
}