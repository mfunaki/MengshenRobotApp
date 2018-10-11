package net.mayoct.mengshen.mengshenrobotapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TitleFragment extends Fragment {
    static final String TAG = TitleFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.title_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        TitleClickListener listener = TitleClickListener.getInstance();
        listener.setFragmentManager(getFragmentManager());

        TextView title1 = view.findViewById(R.id.textTitle1);
        title1.setTypeface(activity.getDefaultTypeface());
        title1.setTextSize(45);
        TextView title2 = view.findViewById(R.id.textTitle2);
        title2.setTypeface(activity.getDefaultTypeface());
        title2.setTextSize(45);

        Button button = view.findViewById(R.id.btnStart);
        button.setTypeface(activity.getDefaultTypeface());
        button.setTextSize(40);
        button.setOnClickListener(listener);
    }
}
