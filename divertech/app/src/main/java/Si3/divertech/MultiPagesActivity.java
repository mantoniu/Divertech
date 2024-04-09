package Si3.divertech;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MultiPagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_pages);
        View b = findViewById(R.id.return_arrow);
        b.setOnClickListener(click -> finish());

        TextView title = findViewById(R.id.title);
        EditText hint = findViewById(R.id.edit_text_area);
        Button button = findViewById(R.id.send_Button);
        int type = getIntent().getIntExtra("type",0);

        switch (type) {
            case EventActivity.CONTACT:
                title.setText(R.string.contact_admin);
                hint.setHint(R.string.hint_contact);
                button.setText(R.string.button_contact);
                break;
            case EventActivity.OBJET:
                title.setText(R.string.lost_object);
                hint.setHint(R.string.hint_object_lost);
                button.setText(R.string.button_objet);
                break;
            default:
                title.setText(R.string.report_incident);
                hint.setHint(R.string.hint_report);
                button.setText(R.string.button_report);
                break;
        }
    }

}
