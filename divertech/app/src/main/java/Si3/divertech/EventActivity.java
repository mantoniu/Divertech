package Si3.divertech;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity implements ClickableActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle b = new Bundle();
        b.putInt("page",1);
        FootMenu f = new FootMenu();
        f.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.footMenu,f).commit();
    }
}