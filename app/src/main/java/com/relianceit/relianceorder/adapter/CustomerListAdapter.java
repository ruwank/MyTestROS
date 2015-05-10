package com.relianceit.relianceorder.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.models.ROSCustomer;

/**
 * Created by Admin on 5/9/15.
 */
public class CustomerListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ROSCustomer> customerList;


    public CustomerListAdapter(Context context, ArrayList<ROSCustomer> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

    @Override
    public int getCount() {
        return customerList.size();
    }

    @Override
    public Object getItem(int location) {
        return customerList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        TextView nameTextView, addressTextView;
        ROSCustomer customer = customerList.get(position);


        if (convertView == null) {
            if (inflater == null)
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.customer_list_row, null);
            nameTextView = (TextView) convertView.findViewById(R.id.customer_name);
            addressTextView = (TextView) convertView.findViewById(R.id.customer_address);

            holder = new ViewHolder(nameTextView, addressTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            nameTextView = holder.name;
            addressTextView = holder.address;
        }

        nameTextView.setText(customer.getFirstName()+ ""+customer.getLastName());
        addressTextView.setText(customer.getTownName());

        return convertView;
    }

    private static class ViewHolder {
        public TextView name, address;

        public ViewHolder(TextView name, TextView address) {
            this.name = name;
            this.address = address;
        }

    }
}
