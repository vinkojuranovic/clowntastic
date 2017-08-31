package com.ferit.clowntastic.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ferit.clowntastic.CheckOrders;
import com.ferit.clowntastic.R;
import com.ferit.clowntastic.helpers.DatabaseHelper;
import com.ferit.clowntastic.models.Order;
import com.ferit.clowntastic.models.User;

import java.util.Calendar;
import java.util.List;

public class CheckOrdersAdapter extends BaseAdapter {

    private List<Order> orders;
    private DatabaseHelper databaseHelper;

    public CheckOrdersAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Order getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return orders.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckOrdersViewHolder confirmOrdersViewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.check_order_item, parent, false);
            confirmOrdersViewHolder = new CheckOrdersViewHolder(convertView);
            convertView.setTag(confirmOrdersViewHolder);
        } else {
            confirmOrdersViewHolder = (CheckOrdersViewHolder) convertView.getTag();
        }
        Order order = orders.get(position);
        User clown = databaseHelper.getUserByServerId(order.getClownId());
        if (clown != null) {
            confirmOrdersViewHolder.tvOrderClownName.setText(clown.getFirstName() + " " + clown.getLastName());
            confirmOrdersViewHolder.tvOrderClownEmail.setText(clown.getEmail());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Integer.valueOf(order.getDate()) * 1000);
        confirmOrdersViewHolder.tvOrderDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "." +
                calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR) + ".");
        confirmOrdersViewHolder.vColorStatus.setBackgroundColor(order.getColor(parent.getContext()));
        confirmOrdersViewHolder.tvPackageName.setText(order.getaPackage().toString());
        return convertView;
    }


    public static class CheckOrdersViewHolder {
        public TextView tvOrderClownName, tvOrderClownEmail, tvOrderDate, tvPackageName;
        public View vColorStatus;

        public CheckOrdersViewHolder(View view) {
            tvOrderClownName = (TextView) view.findViewById(R.id.tv_orderClownName);
            tvOrderClownEmail = (TextView) view.findViewById(R.id.tv_orderClownEmail);
            tvOrderDate = (TextView) view.findViewById(R.id.tv_orderDate);
            tvPackageName = (TextView) view.findViewById(R.id.tv_packageName);
            vColorStatus = (View) view.findViewById(R.id.v_colorStatus);
        }
    }
}
