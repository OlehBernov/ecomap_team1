package com.ecomap.ukraine.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Top10Item;

import java.util.List;


public class Top10ListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Top10Item> top10problems;
    private int iconID;

    public Top10ListAdapter(Context context, List<Top10Item> top10problems, int iconID) {
        this.top10problems = top10problems;
        this.iconID = iconID;
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return top10problems.size();
    }

    @Override
    public Top10Item getItem(int position) {
        return top10problems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.top_10_item, parent, false);
        }
        Top10Item item = getItem(position);
        ((TextView) view.findViewById(R.id.top_10_value)).setText(String.valueOf(item.getValue()));
        ((TextView) view.findViewById(R.id.top_10_item_title)).setText(item.getTitle());
        ((ImageView) view.findViewById(R.id.top_10_item_icon)).setImageResource(iconID);
        return view;
    }
}
