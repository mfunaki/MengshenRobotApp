package net.mayoct.mengshen.mengshenrobotapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

public class CategoryListClickListener implements View.OnClickListener {
    private static final CategoryListClickListener instance = new CategoryListClickListener();
    private static FragmentManager fragmentManager;
    private static String[] category;

    private CategoryListClickListener() {
        super();
    }

    public static CategoryListClickListener getInstance() {
        return instance;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        CategoryListClickListener.fragmentManager = fragmentManager;
    }

    public void setCategory(String[] category) {
        CategoryListClickListener.category = category;
    }

    @Override
    public void onClick(View v) {
        int n = 0;
        if (v != null) {
            String text = new String(((Button) v).getText().toString());
            for (int i = 0; i < CategoryListClickListener.category.length; i++) {
                if (CategoryListClickListener.category[i].compareTo(text) == 0) {
                    n = i;
                    break;
                }
            }
            FragmentTransaction xact = fragmentManager.beginTransaction();
            Fragment fragment = new TangoListFragment();
            ((TangoListFragment) fragment).setCategoryNo(n);
            xact.replace(R.id.fragment_area, fragment, TangoListFragment.TAG);
            xact.addToBackStack(null);
            xact.commit();
        }
    }
}
