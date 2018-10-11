package net.mayoct.mengshen.mengshenrobotapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class CategoryListFragment extends Fragment {
    static final String TAG = CategoryListFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.categorylist_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        String[] category = activity.getCategory();

        LinearLayout baseLayout = view.findViewById(R.id.categoryListLayout);
        CategoryListClickListener listener = CategoryListClickListener.getInstance();
        listener.setFragmentManager(getFragmentManager());
        listener.setCategory(category);

        for (int i = 0; i < category.length; i += 2) {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            baseLayout.addView(layout);
            for (int j = 0; i + j < category.length && j < 2; j++) {
                String categoryName = category[i + j];
                Button button = new Button(getContext());
                char[] categoryChars = category[i + j].toCharArray();
                button.setText(categoryChars, 0, categoryChars.length);
                button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                button.setTypeface(activity.getDefaultTypeface());
                button.setTextSize(40);
                layout.addView(button);

                button.setOnClickListener(listener);
            }

        }
    }
}
