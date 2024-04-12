package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FootMenu extends Fragment {
    private ClickableActivity activity;
    private String attachedActivity;
    List<Integer> elements = List.of(R.id.homeIcon,R.id.locationIcon,R.id.eventIcon);
    List<ImageView> buttons = new ArrayList<>();

    public FootMenu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View root =  inflater.inflate(R.layout.fragment_foot_menu,container,false);
        Integer n = requireArguments().getInt("page",1);
        for (int i=0;i<elements.size() ;i++){
            buttons.add(root.findViewById(elements.get(i)));
            Log.d("test",buttons.get(i).toString());
            int finalI = i+1;
            buttons.get(i).setOnClickListener(click->{activity.onCick(finalI,n);});
        }
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        super.onAttach(context);
        attachedActivity = getActivity().getClass().getSimpleName();
        List<Class<?>> interfaces = Arrays.asList(getActivity().getClass().getInterfaces());
        if (interfaces==null || !interfaces.contains(ClickableActivity.class)) try {
            throw new Throwable(attachedActivity + " must implements ClickableActivity");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        activity = (ClickableActivity)getActivity();
    }
}