package Si3.divertech;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class headerMenu extends Fragment {

    public headerMenu(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_header_menu, container, false);
        View b = root.findViewById(R.id.languageIcon);
        b.setOnClickListener(click->{
            Intent intent = new Intent(getContext(), ChangementLangue.class);
            startActivity(intent);
        });
        return root;
    }
}