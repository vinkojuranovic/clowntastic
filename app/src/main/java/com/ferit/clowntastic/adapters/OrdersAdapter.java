package com.ferit.clowntastic.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ferit.clowntastic.R;

import com.ferit.clowntastic.helpers.DatabaseHelper;
import com.ferit.clowntastic.models.Order;
import com.ferit.clowntastic.models.User;

import java.util.Calendar;
import java.util.List;

public class OrdersAdapter extends BaseAdapter {

    private List<Order> orders;
    private DatabaseHelper databaseHelper;

    public OrdersAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Order getItem(int position) { return orders.get(position); }

    @Override
    public long getItemId(int position) {
        return orders.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrdersViewHolder ordersViewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_order, parent, false);
            ordersViewHolder = new OrdersViewHolder(convertView);
            convertView.setTag(ordersViewHolder);
        } else {
            ordersViewHolder = (OrdersViewHolder) convertView.getTag();
        }
        Order order = orders.get(position);
        User customer = databaseHelper.getUserByServerId(order.getCustomerId());
        if (customer != null) {
            ordersViewHolder.tvOrderCustomerEmail.setText(customer.getEmail());
            ordersViewHolder.tvOrderCustomer.setText(customer.getFirstName() + " " + customer.getLastName());
        }
        Calendar calendar = Calendar.getInstance();
        Log.e("DATE", order.getDate());
        Log.e("DATE", String.valueOf(Long.valueOf(order.getDate()) * 1000));
        calendar.setTimeInMillis(Long.valueOf(order.getDate()) * 1000);
        ordersViewHolder.tvOrderDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "." +
                Integer.valueOf(calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR) + ".");
        ordersViewHolder.vvOrderStatusView.setBackgroundColor(order.getColor(parent.getContext()));
        return convertView;
    }

    public static class OrdersViewHolder {
        public TextView tvOrderDate, tvOrderCustomer, tvOrderCustomerEmail;
        public View vvOrderStatusView;

        public OrdersViewHolder(View view) {
            tvOrderDate = (TextView) view.findViewById(R.id.tv_orderDate);
            tvOrderCustomer = (TextView) view.findViewById(R.id.tv_orderCustomer);
            tvOrderCustomerEmail = (TextView) view.findViewById(R.id.tv_orderCustomerEmail);
            vvOrderStatusView = (View) view.findViewById(R.id.vv_orderStatusColor);
        }
    }
}
