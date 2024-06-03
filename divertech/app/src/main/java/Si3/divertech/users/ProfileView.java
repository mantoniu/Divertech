package Si3.divertech.users;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Si3.divertech.databinding.ActivityProfileBinding;

public class ProfileView implements Observer {
    private ProfileController controller;
    private final ViewGroup layout;
    private final ActivityProfileBinding binding;
    private final List<TextInputLayout> fields;
    private final List<Button> notificationsOptions;
    private final ActivityResultLauncher<Intent> startCrop;
    private final LayoutInflater inflater;

    public ProfileView(ViewGroup layout, ActivityProfileBinding binding, ActivityResultLauncher<Intent> startCrop, LayoutInflater inflater) {
        this.layout = layout;
        this.binding = binding;
        this.startCrop = startCrop;
        this.inflater = inflater;

        this.fields = Collections.unmodifiableList(Arrays.asList(
                binding.lastNameContainer,
                binding.firstNameContainer,
                binding.mailContainer,
                binding.phoneContainer,
                binding.addressContainer,
                binding.cityContainer,
                binding.postalcodeContainer,
                binding.passwordContainer
        ));

        binding.profilePicture.setOnClickListener(click -> controller.editProfilePicture());

        this.notificationsOptions = Collections.unmodifiableList(Arrays.asList(
                binding.notifInfoButton,
                binding.notifWarningButton
        ));

        for (Button button : notificationsOptions) {
            button.setOnClickListener(click -> controller.setNotificationOption(button));
        }
    }

    public void setController(ProfileController controller) {
        this.controller = controller;
        controller.setOnClickEdit();
        controller.setValues();
    }

    @Override
    public void update(Observable o, Object arg) {
        controller.setValues();
    }

    public Context getContext() {
        return layout.getContext();
    }

    public void startCrop(Intent intent) {
        this.startCrop.launch(intent);
    }

    public ActivityProfileBinding getBinding() {
        return binding;
    }

    public List<TextInputLayout> getFields() {
        return fields;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }
}
