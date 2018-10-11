package net.mayoct.mengshen.mengshenrobotapp;

import android.view.View;

public class TangoClickListener implements View.OnClickListener {
    private static final TangoClickListener instance = new TangoClickListener();
    private static TangoFragment tangoFragment;
    private static Tango tango;

    private TangoClickListener() {
        super();
    }

    public static TangoClickListener getInstance() {
        return instance;
    }

    public void setTango(Tango tango) {
        TangoClickListener.tango = tango;
    }

    public void setTangoFragment(TangoFragment tangoFragment) {
        TangoClickListener.tangoFragment = tangoFragment;
    }

    @Override
    public void onClick(View v) {
        v.setEnabled(false);
        tangoFragment.speakTango(tango);
        v.setEnabled(true);
    }
}
