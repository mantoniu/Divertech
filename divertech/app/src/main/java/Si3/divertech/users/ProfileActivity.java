package Si3.divertech.users;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import Si3.divertech.R;
import Si3.divertech.RequireUserActivity;
import Si3.divertech.databinding.ActivityProfileBinding;
import Si3.divertech.notificationservice.NotificationChannel;

public class ProfileActivity extends RequireUserActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.returnArrow.setOnClickListener(click -> finish());
        onViewCreated((ViewGroup) getWindow().getDecorView(), binding);
        setNotificationOption(binding);
    }

    public void setNotificationOption(ActivityProfileBinding binding) {
        Button notifInfoButton = binding.notifInfoButton;
        Button notifWarningButton = binding.notifWarningButton;

        notifInfoButton.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, NotificationChannel.CHANNEL_INFO);
                startActivity(intent);

            }else{
                Toast.makeText(this, R.string.no_channel, Toast.LENGTH_SHORT).show();
            }
        });

        notifWarningButton.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, NotificationChannel.CHANNEL_WARNING);
                startActivity(intent);
            }else{
                Toast.makeText(this, R.string.no_channel, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public <T extends ViewGroup> void onViewCreated(T layout, ActivityProfileBinding binding) {
        ProfileController controller = new ProfileController(getBaseContext());
        ActivityResultLauncher<Intent> startCrop = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            controller.uploadImage(data.getStringExtra("croppedImageUri"));
                        }
                    }
                }
        );
        ProfileView view = new ProfileView(layout, binding, startCrop, getLayoutInflater());
        controller.setView(view);
        UserData.getInstance().addObserver(view);
        view.setController(controller);
    }
}