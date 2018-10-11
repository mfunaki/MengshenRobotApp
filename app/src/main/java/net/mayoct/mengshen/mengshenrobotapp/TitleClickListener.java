package net.mayoct.mengshen.mengshenrobotapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class TitleClickListener implements View.OnClickListener {
    private static final TitleClickListener instance = new TitleClickListener();
    private static FragmentManager fragmentManager;

    private TitleClickListener() {
        super();
    }

    public static TitleClickListener getInstance() {
        return instance;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        TitleClickListener.fragmentManager = fragmentManager;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction xact = fragmentManager.beginTransaction();
        Fragment fragment = new CategoryListFragment();
        xact.replace(R.id.fragment_area, fragment, CategoryListFragment.TAG);
        xact.addToBackStack(null);
        xact.commit();
    }
}
