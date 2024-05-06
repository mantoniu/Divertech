package Si3.divertech;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);
        View b = findViewById(R.id.return_arrow);
        b.setOnClickListener(click -> finish());


        Button button = findViewById(R.id.send_button);
        button.setOnClickListener(click -> forward());
    }

    private void forward() {
        finish();
    }

}