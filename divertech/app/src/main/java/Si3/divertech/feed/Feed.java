package Si3.divertech.feed;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import java.util.Observable;
import java.util.Observer;

import Si3.divertech.ClickableFragment;
import Si3.divertech.databinding.FragmentFeedBinding;
import Si3.divertech.events.EventList;

public abstract class Feed extends Fragment implements ClickableFragment, Observer {
    private Context context;
    private BaseAdapter adapter;
    private FragmentFeedBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFeedBinding.inflate(inflater, container, false);
        adapter = getAdapter();
        binding.feed.setAdapter(adapter);
        EventList.getInstance().addObserver(this);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public FragmentFeedBinding getBinding() {
        return binding;
    }

    public abstract BaseAdapter getAdapter();

    @Override
    public void update(Observable o, Object arg) {
        if (adapter != null)
            adapter.notifyDataSetChanged();
        setEmptyText();
    }

    protected void setEmptyText() {
        binding.emptyText.setText(getEmptyTextRes());
        binding.feed.setEmptyView(binding.emptyText);
    }

    public abstract @StringRes int getEmptyTextRes();
}
