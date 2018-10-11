package net.mayoct.mengshen.mengshenrobotapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class TangoListClickListener implements View.OnClickListener {
    private static final TangoListClickListener instance = new TangoListClickListener();
    private static FragmentManager fragmentManager;
    private static ArrayList<Tango> tangoList;

    private TangoListClickListener() {
        super();
    }

    public static TangoListClickListener getInstance() {
        return instance;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        TangoListClickListener.fragmentManager = fragmentManager;
    }

    public void setTangoList(ArrayList<Tango> tangoList) {
        TangoListClickListener.tangoList = tangoList;
    }

    @Override
    public void onClick(View v) {
        int n = 0;
        if (v != null) {
            String hanzi = new String(((Button) v).getText().toString());
            for (int i = 0; i < tangoList.size(); i++) {
                if (tangoList.get(i).getHanzi().compareTo(hanzi) == 0) {
                    n = i;
                    break;
                }
            }
            FragmentTransaction xact = fragmentManager.beginTransaction();
            Fragment fragment = new TangoFragment();
            ((TangoFragment) fragment).setTango(tangoList.get(n));
            xact.replace(R.id.fragment_area, fragment, TangoFragment.TAG);
            xact.addToBackStack(null);
            xact.commit();
        }
    }
}
