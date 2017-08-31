package com.ferit.clowntastic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.ferit.clowntastic.R;

import com.ferit.clowntastic.models.User;

import java.util.List;

public class ClownsAdapter extends BaseAdapter {

    private List<User> clowns;

    public ClownsAdapter(List<User> clowns) {
        this.clowns = clowns;
    }

    @Override
    public int getCount() {
        return clowns.size();
    }

    @Override
    public User getItem(int position) {
        return clowns.get(position);
    }

    @Override
    public long getItemId(int position) {
        return clowns.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClownsViewHolder clownsViewHolder;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_clown, parent, false);
            clownsViewHolder = new ClownsViewHolder(convertView);
            convertView.setTag(clownsViewHolder);
        } else {
            clownsViewHolder = (ClownsViewHolder) convertView.getTag();
        }
        User clown = clowns.get(position);
        clownsViewHolder.tvName.setText(clown.getFirstName() + " " + clown.getLastName());
        clownsViewHolder.tvEmail.setText(clown.getEmail());

        return convertView;
    }

    public static class ClownsViewHolder {
        public TextView tvName, tvEmail;

        public ClownsViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvEmail = (TextView) view.findViewById(R.id.tv_email);
        }
    }
}
