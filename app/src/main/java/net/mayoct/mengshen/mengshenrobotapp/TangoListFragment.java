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
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

public class TangoListFragment extends Fragment {
    static final String TAG = TangoListFragment.class.getSimpleName();

    private int categoryNo;

    public void setCategoryNo(int categoryNo) {
        this.categoryNo = categoryNo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tangolist_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        ArrayList tangoList = activity.getTangoList(categoryNo);
        String[] category = activity.getCategory();

        TableLayout baseLayout = view.findViewById(R.id.tangoListLayout);
        TangoListClickListener listener = TangoListClickListener.getInstance();
        listener.setFragmentManager(getFragmentManager());
        listener.setTangoList(tangoList);
        for (int i = 0; i < tangoList.size(); i += 3) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            baseLayout.addView(tableRow);
            for (int j = 0; i + j < tangoList.size() && j < 3; j++) {
                String categoryName = ((Tango) tangoList.get(i + j)).getHanzi();
                Button button = new Button(getContext());
                char[] hanziChars = categoryName.toCharArray();
                button.setText(hanziChars, 0, hanziChars.length);
                button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                button.setTypeface(activity.getDefaultTypeface());
                button.setTextSize(40);
                tableRow.addView(button);

                button.setOnClickListener(listener);
            }
        }
    }
}
