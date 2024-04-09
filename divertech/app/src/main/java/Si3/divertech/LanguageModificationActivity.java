package Si3.divertech;



import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class LanguageModificationActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_modification);
        Spinner dropdown = findViewById(R.id.langues);
        String[] languages = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        dropdown.setAdapter(adapter);
        View b = findViewById(R.id.returnArrow);
        b.setOnClickListener(click->finish());
    }
}