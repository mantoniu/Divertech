package Si3.divertech;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MultiPagesAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_pages_admin);
        View b = findViewById(R.id.return_arrow);
        b.setOnClickListener(click -> finish());
        View close = findViewById(R.id.bloc_close);
        close.setOnClickListener(click -> finish());
        TextView title = findViewById(R.id.title);
        EditText hint = findViewById(R.id.edit_text_area);
        int index = getIntent().getIntExtra("type", 0);
        NotificationTypes type = NotificationTypes.values()[index];
        switch (type) {
            case CONTACT:
                title.setText("Objet message");
                break;
            case LOST_OBJECT:
                title.setText("Objet Perdu");
                break;
            default:
                title.setText("Titre incident");
                break;
        }
    }

}
