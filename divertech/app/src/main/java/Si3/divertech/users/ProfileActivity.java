package Si3.divertech.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import Si3.divertech.RequireUserActivity;
import Si3.divertech.databinding.ActivityProfileBinding;

public class ProfileActivity extends RequireUserActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.returnArrow.setOnClickListener(click -> finish());
        onViewCreated((ViewGroup) getWindow().getDecorView(), binding);
    }

    public <T extends ViewGroup> void onViewCreated(T layout, ActivityProfileBinding binding) {
        ProfileController controller = new ProfileController();
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