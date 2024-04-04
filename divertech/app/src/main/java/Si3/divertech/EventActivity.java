package Si3.divertech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EventActivity extends AppCompatActivity {


    public static final int REPORTING = 0;
    public static final int CONTACT = 1;
    public static final int OBJET = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
    }
}