package Si3.divertech;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import Si3.divertech.users.User;
import Si3.divertech.users.UserData;

public class HeaderMenu extends Fragment {
    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    //inflate the layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_header_menu, container, false);
    }

    //inflate the menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.header_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();
        User user = UserData.getInstance().getConnectedUser();
        if (id == R.id.menu_language_english) {
            user.setLanguage("en");
        } else if (id == R.id.menu_language_french) {
            user.setLanguage("fr");
        } else if (id == R.id.menu_logout) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            user.disconnect();
            startActivity(intent);
            activity.finish();
        }
        else if(id == R.id.menu_profile_edit){
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
