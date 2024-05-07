package Si3.divertech;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class HeaderMenu extends Fragment {

    public HeaderMenu(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_header_menu, container, false);
        View b = root.findViewById(R.id.languageIcon);
        b.setOnClickListener(click->{
            Intent intent = new Intent(getContext(), LanguageModificationActivity.class);
            startActivity(intent);
        });
        root.findViewById(R.id.profile_image).setOnClickListener(this::launchProfileActivity);
        return root;
    }

    public void launchProfileActivity(View view) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        startActivity(intent);
    }
}