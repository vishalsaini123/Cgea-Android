package com.creditgaea.sample.credit.java.creditgea.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.creditgaea.sample.credit.java.creditgea.utils.CarbonLogModel;
import com.quickblox.sample.videochat.java.R;

import java.util.ArrayList;

/**
 * Created by user on 11/27/2016.
 */

public class AlllogsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CarbonLogModel> arrayList;

    public AlllogsAdapter(Context context, ArrayList<CarbonLogModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.inflate_layout_calllogs, viewGroup, false);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_score = (TextView) convertView.findViewById(R.id.tv_score);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_date.setText(arrayList.get(i).getDate());
        holder.tv_score.setText(arrayList.get(i).getScore());

        return convertView;
    }

    static class ViewHolder {
        TextView tv_date, tv_score;
    }
}
