package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.Observable;
import java.util.Observer;

import Si3.divertech.users.ProfileActivity;
import Si3.divertech.users.UserData;

public class HeaderMenu extends Fragment implements Observer {

    private Menu menu = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        UserData.getInstance().addObserver(this);
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
        if (this.menu == null) {
            this.menu = menu;
            updateToolbar();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void updateToolbar() {
        MenuItem profileImageItem = menu.findItem(R.id.menu_profile);
        View profileImageView = profileImageItem.getActionView();
        if (profileImageView != null) {
            profileImageView.setOnClickListener(this::showProfilePopupMenu);
            Picasso.get().load(UserData.getInstance().getConnectedUser().getPictureUrl()).into((android.widget.ImageView) profileImageView.findViewById(R.id.profile_picture));
        }
        MenuItem langImage = menu.findItem(R.id.menu_language);
        if (langImage.getActionView() != null)
            langImage.getActionView().setOnClickListener(this::showLangMenu);
    }

    private void showProfilePopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.profile_popup_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_logout) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                UserData.getInstance().disconnect();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (id == R.id.menu_profile_edit) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
            return false;
        });
        popupMenu.show();
    }

    private void showLangMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.lang_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_language_english) {
                UserData.getInstance().setLanguage("en");
            } else if (id == R.id.menu_language_french) {
                UserData.getInstance().setLanguage("fr");
            }
            return false;
        });
        popupMenu.show();
    }


    @Override
    public void update(Observable o, Object arg) {
        updateToolbar();
    }
}
