package Si3.divertech;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
        EditText typeSelector = findViewById(R.id.selector);
        int index = getIntent().getIntExtra("type", 0);
        NotificationTypes type = NotificationTypes.values()[index];
        typeSelector.setText(type.getContent());
        //switch (type) {
        //    case CONTACT:
        //        title.setText("Objet message");
        //        break;
        //    case LOST_OBJECT:
        //        title.setText("Objet Perdu");
        //        break;
        //    default:
        //        title.setText("Titre incident");
        //        break;
        //}
    }

}
