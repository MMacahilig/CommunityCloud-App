package com.michael.commcloudapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.michael.commcloudapp.Models.Alert;
import com.michael.commcloudapp.R;

import java.util.ArrayList;

/**
 * Created by michaelmacahilig on 14/08/15.
 * The AlertListAdapter is an adapter class used to generate views and fill the views of the listview
 * with data from the arraylist of alerts
 *
 */
public class AlertListAdapter extends BaseAdapter {

    private ArrayList<Alert> mAlertList;
    private Context mContext;

    public AlertListAdapter(Context context, ArrayList<Alert> arrayList) {
        this.mContext = context;
        this.mAlertList = arrayList;
    }

    /*
    * Returns the size of the arraylist attached to the adapter
    *
    * */
    @Override
    public int getCount() {
        return mAlertList.size();
    }


    /*
    * Returns the item within arraylist based on the integer position that input
    * */
    @Override
    public Object getItem(int i) {
        return mAlertList.get(i);
    }

    /*
    * returns the long item id of within the arraylist
    * */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /***
     * This function is used to refresh the current view instead of generating a new view per
     * instance within the arraylist
     * This function inflates the generated layouts with items within the arraylist
     */


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view;
        if(convertView != null){
            view = convertView;
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_list, null);
        }


        //TextView clientName = (TextView)view.findViewById(R.id.alertClientName);
        TextView type = (TextView)view.findViewById(R.id.alertType);
        TextView rating = (TextView)view.findViewById(R.id.alertRating);
        TextView location = (TextView)view.findViewById(R.id.alertLocation);
        TextView detail = (TextView)view.findViewById(R.id.alertDetail);
        TextView time = (TextView)view.findViewById(R.id.alertTime);


        String address = mAlertList.get(i).getAddress();
        String city = mAlertList.get(i).getCity();
        String state = mAlertList.get(i).getState();

        String eventLocation;

        if(city.isEmpty()){
                eventLocation = state;
        } else {
                eventLocation = city + " " + state;
        }


        //clientName.setText(mAlertList.get(i).getDetails());
        type.setText(mAlertList.get(i).getAlertType());
        rating.setText(mAlertList.get(i).getAlertRating());
        location.setText(eventLocation);
        detail.setText(mAlertList.get(i).getDetails());
        time.setText(mAlertList.get(i).getTime());
        return view;
    }
}
