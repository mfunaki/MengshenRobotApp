package net.mayoct.mengshen.mengshenrobotapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TangoListAdapter extends ArrayAdapter<Tango> {
    private int mResource;
    private List<Tango> mItems;
    private LayoutInflater mInflater;

    public TangoListAdapter(Context context, int resource, List<Tango> items) {
        super(context, resource, items);

        mResource = resource;
        mItems = items;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(mResource, null);
        }

        Tango item = mItems.get(position);

        TextView hanzi = (TextView) view.findViewById(R.id.hanzi);
        hanzi.setText(item.getHanzi());

        TextView pinyin = (TextView) view.findViewById(R.id.pinyin);
        pinyin.setText(item.getPinyin());

        return view;
    }
}
