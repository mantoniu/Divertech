package Si3.divertech;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FootMenu extends Fragment {
    private ClickableActivity activity;
    private String attachedActivity;
    List<Integer> elements = List.of(R.id.homeLayout,R.id.locationLayout,R.id.eventLayout);
    List<ConstraintLayout> buttons = new ArrayList<>();
    List<Integer> borders = List.of(R.id.feedborderTop,R.id.locationBorderTop,R.id.eventborderTop);

    List<Integer> images = List.of(R.id.homeIcon,R.id.locationIcon,R.id.eventIcon);



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
        List<Drawable> drawables= List.of(getResources().getDrawable(R.drawable.home),getResources().getDrawable(R.drawable.location),getResources().getDrawable(R.drawable.agenda));

        int n = requireArguments().getInt("page",1);
        for (int i=0;i<elements.size() ;i++){
            buttons.add(root.findViewById(elements.get(i)));
            int finalI = i+1;
            if(n == finalI) {
                root.findViewById(borders.get(i)).setVisibility(View.VISIBLE);
                Drawable drawable = drawables.get(i);
                drawable.setTint(getResources().getColor(R.color.mediumPurple));
                ((ImageView)root.findViewById(images.get(i))).setImageDrawable(drawable);
            }
            else{
                Drawable drawable = drawables.get(i);
                drawable.setTint(getResources().getColor(R.color.black));
                ((ImageView)root.findViewById(images.get(i))).setImageDrawable(drawable);
            }

            buttons.get(i).setOnClickListener(click-> {
                activity.onCick(finalI,n);
            });
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