package com.michael.commcloudapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.michael.commcloudapp.Models.Alert;
import com.michael.commcloudapp.Models.Event;
import com.michael.commcloudapp.R;

import java.util.ArrayList;

/**
 * Created by michaelmacahilig on 4/11/15.
 * The EventListAdapter is an adapter class used to generate views and fill the views of the listview
 * with data from the arraylist of events
 *
 */
public class EventListAdapter extends BaseAdapter {

    private ArrayList<Event> mEventList;
    private Context mContext;

    public EventListAdapter(Context context, ArrayList<Event> arrayList) {
        this.mContext = context;
        this.mEventList = arrayList;
    }

    /**
     * Returns the size of the arraylist attached to the adapter
     * @return int count
     */
    @Override
    public int getCount() {
        return mEventList.size();
    }

    /**
     * THis function returns the item within the arraylist based on the parameter i
     * @param i
     * @return Event
     */
    @Override
    public Object getItem(int i) {
        return mEventList.get(i);
    }


    /**
     * This function is used to refresh the current view instead of generating a new view per
     * instance within the arraylist
     * This function inflates the generated layouts with items within the arraylist
     * @param i
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view;
        if(convertView != null){
            view = convertView;
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list, null);
        }


        //TextView clientName = (TextView)view.findViewById(R.id.alertClientName);
        TextView type = (TextView)view.findViewById(R.id.eventType);
        TextView rating = (TextView)view.findViewById(R.id.eventRating);
        TextView location = (TextView)view.findViewById(R.id.eventLocation);
        TextView detail = (TextView)view.findViewById(R.id.eventDetails);
        TextView time = (TextView)view.findViewById(R.id.eventTime);


        String address = mEventList.get(i).getAddress();
        String city = mEventList.get(i).getCity();
        String state = mEventList.get(i).getState();

        String eventLocation;

        if(address.isEmpty()){
            if(city.isEmpty()){
                eventLocation = state;
            } else {
                eventLocation = city + " " + state;
            }
        }else {
            eventLocation = address + " " + city + " " + state;
        }

        //clientName.setText(mEventList.get(i).getDetails());
        type.setText(mEventList.get(i).getAlertType());
        rating.setText(mEventList.get(i).getAlertRating());
        location.setText(eventLocation);
        detail.setText(mEventList.get(i).getDetails());
        time.setText(mEventList.get(i).getTime());

        return view;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
}
