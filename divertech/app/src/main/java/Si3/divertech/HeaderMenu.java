package Si3.divertech;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import Si3.divertech.utils.LangUtils;

public class HeaderMenu extends Fragment implements Observer {

    private Menu menu = null;
    private Activity activity;

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
        Activity activity = getActivity();
        if (activity != null) {
            this.activity = activity;
            LangUtils.changeActionBarTitle(activity);
            Log.d("HeaderMenu", "Activity attached" + activity);
        }

    }

    public void updateToolbar() {
        MenuItem profileImageItem = menu.findItem(R.id.menu_profile);
        View profileImageView = profileImageItem.getActionView();
        if (UserData.getInstance().getConnectedUser() != null) {
            if (profileImageView != null) {
                profileImageView.setOnClickListener(this::showProfilePopupMenu);
                if (!UserData.getInstance().getConnectedUser().getPictureUrl().isEmpty())
                    Picasso.get().load(UserData.getInstance().getConnectedUser().getPictureUrl()).into((android.widget.ImageView) profileImageView.findViewById(R.id.profile_picture));
            }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }
        popupMenu.show();
    }

    private void showLangMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.lang_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_language_english) {
                if(UserData.getInstance().getConnectedUser().getLanguage().equals("en")) return false;
                UserData.getInstance().setLanguage("en");
                LangUtils.changeLang(activity, "en");
                Intent intent = new Intent(getActivity(),activity.getClass());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);
            } else if (id == R.id.menu_language_french) {
                if(UserData.getInstance().getConnectedUser().getLanguage().equals("fr")) return false;
                UserData.getInstance().setLanguage("fr");
                LangUtils.changeLang(activity, "fr");
                Intent intent = new Intent(getActivity(), activity.getClass());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);

            }
            return false;
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }
        popupMenu.show();
    }


    @Override
    public void update(Observable o, Object arg) {
        if (menu != null) updateToolbar();
    }

}
