package Si3.divertech;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import Si3.divertech.events.EventList;
import Si3.divertech.notifications.NotificationList;
import Si3.divertech.users.UserData;

public abstract class RequireUserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        returnToLaunch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        returnToLaunch();
    }

    private void returnToLaunch() {
        if (UserData.getInstance().getConnectedUser() != null)
            return;

        EventList.getInstance().reset();
        NotificationList.getInstance().reset();
        Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
