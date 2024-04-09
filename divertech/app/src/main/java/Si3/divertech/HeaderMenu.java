package Si3.divertech;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HeaderMenu extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_header_menu,container,false);
        root.findViewById(R.id.profile_button).setOnClickListener(this::launchProfileActivity);
        return root;
    }

    public void launchProfileActivity(View view) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        startActivity(intent);
    }
}