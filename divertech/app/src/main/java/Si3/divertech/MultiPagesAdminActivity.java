package Si3.divertech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MultiPagesAdminActivity extends AppCompatActivity {

    private String profil = "0";

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
        int type = getIntent().getIntExtra("type",0);

        switch (type) {
            case EventActivity.CONTACT:
                title.setText("Objet message");
                break;
            case EventActivity.OBJET:
                title.setText("Objet Perdu");
                break;
            default:
                title.setText("Titre incident");
                break;
        }

        View call = findViewById(R.id.bloc_contact_phone);
        call.setOnClickListener(click->{
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ListProfil.getProfilMap().get(profil).getPhoneNumber()));
            startActivity(intent);
        });

        View mail = findViewById(R.id.bloc_contact_mail);
        mail.setOnClickListener(click->{
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+ListProfil.getProfilMap().get(profil).getEmail()));
            startActivity(intent);
        });
    }

}
