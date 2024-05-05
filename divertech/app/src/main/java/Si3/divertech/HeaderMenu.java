package Si3.divertech;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;

public class HeaderMenu extends Fragment implements AdapterView.OnItemSelectedListener {
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_header_menu, container, false);

        ImageButton b = root.findViewById(R.id.languageIcon);
        Spinner s = root.findViewById(R.id.language);
        s.setAdapter(new LangAdapter(getContext()));
        s.setOnItemSelectedListener(this);
        s.setSelection(LanguageSelected.LANGUAGE_SELECTED.equals("fr") ? 0 : 1);
        Log.d("postion", String.valueOf(s.getSelectedItemPosition()));
        b.setImageResource(LanguageSelected.LANGUAGE_SELECTED.equals("fr") ? R.drawable.fr_flag : R.drawable.en_flag);
        b.setOnClickListener(v -> {
            s.performClick();
        });
        

        root.findViewById(R.id.profile_button).setOnClickListener(this::launchProfileActivity);
        return root;
    }

    public void launchProfileActivity(View view) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(LanguageSelected.LANGUAGE_SELECTED.equals((String) parent.getItemAtPosition(position)))
            return;
        LanguageSelected.LANGUAGE_SELECTED = (String) parent.getItemAtPosition(position);
        LanguageSelected.setLanguage(activity);
        ImageButton b = getView().findViewById(R.id.languageIcon);
        b.setImageResource(LanguageSelected.LANGUAGE_SELECTED.equals("fr") ? R.drawable.fr_flag : R.drawable.en_flag);
        activity.recreate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (activity != null) {
            this.activity = activity;
            Log.d("Activity", activity.toString());
        }
    }
}