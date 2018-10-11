package net.mayoct.mengshen.mengshenrobotapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TangoFragment extends Fragment {
    static final String TAG = TangoFragment.class.getSimpleName();
    private Tango tango;

    public void setTango(Tango tango) {
        this.tango = tango;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tango_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();

        TextView textHanzi = view.findViewById(R.id.textHanzi);
        TextView textPinyin = view.findViewById(R.id.textPinyin);
        Button button = view.findViewById(R.id.btnRepeat);

        char[] hanziChars = tango.getHanzi().toCharArray();
        textHanzi.setText(hanziChars, 0, hanziChars.length);
        textHanzi.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));

        char[] pinyinChars = tango.getPinyin().toCharArray();
        textPinyin.setText(pinyinChars, 0, pinyinChars.length);
        textPinyin.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));

        button.setTypeface(activity.getDefaultTypeface());
        button.setTextSize(30);

        TangoClickListener listener = TangoClickListener.getInstance();
        listener.setTangoFragment(this);
        listener.setTango(tango);
        button.setOnClickListener(listener);
    }

    @Override
    public void onStart() {
        super.onStart();
        speakTango(tango);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.d("TangoFragment", "Interruption1: " + e.getLocalizedMessage());
        }
    }

    public void speakTango(Tango tango) {
        MainActivity activity = (MainActivity) getActivity();


        activity.ttsSpeak(tango.getHanzi(), "Full: " + tango.getHanzi());

        activity.btSend(MainActivity.COMMAND_INIT, tango.getTones());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.d("TangoFragment", "Interruption2: " + e.getLocalizedMessage());
        }

        activity.btSend(MainActivity.COMMAND_SPEAK_01, tango.getTones());
        activity.ttsSpeak(tango.getHanzi().substring(0, 1), "1st: " + tango.getHanzi().substring(0, 1));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Log.d("TangoFragment", "Interruption3: " + e.getLocalizedMessage());
        }
        activity.btSend(MainActivity.COMMAND_SPEAK_02, tango.getTones());
        activity.ttsSpeak(tango.getHanzi().substring(1, 2), "2nd: " + tango.getHanzi().substring(1, 2));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Log.d("TangoFragment", "Interruption4: " + e.getLocalizedMessage());
        }
        activity.btSend(MainActivity.COMMAND_SPEAK_03, tango.getTones());
        activity.ttsSpeak(tango.getHanzi().substring(2), "3rd: " + tango.getHanzi().substring(2));
    }
}
