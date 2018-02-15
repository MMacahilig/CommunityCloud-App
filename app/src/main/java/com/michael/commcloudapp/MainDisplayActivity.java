package com.michael.commcloudapp;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.michael.commcloudapp.Adapters.AlertListAdapter;
import com.michael.commcloudapp.Adapters.EventListAdapter;
import com.michael.commcloudapp.Models.Alert;
import com.michael.commcloudapp.Models.Event;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The activity uses a navigation drawer to display the multiple list views that displays event
 * information, and the sent alerts
 */

public class MainDisplayActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    private boolean mRequestLocationUpdates = false;

    private LocationRequest mLocationRequest;


    private static int UPDATE_INTERVAL = 10000;
    private static int FATEST_INTERVAL = 5000;
    private static int DISPLACEMENT = 10;

    private String mAddress;
    private String mUserId;
    private String mCity;
    private String mState;
    private String mEvents;
    private String mAlerts;
    private String mName;



    private ListView mEventListView;
    private ListView mAlertListView;
    private ListView mManageListView;
    private AlertListAdapter mAlertListAdapter;
    private EventListAdapter mEventListAdapter;

    private ArrayList<Alert> mAlertList;
    private ArrayList<Event> mEventList;

    private ProgressBar mProgressBar;

    private NavigationDrawerFragment mNavigationDrawerFragment;


    private CharSequence mTitle;

    /***
     * Generates the view and intialises the adapters and listviews
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alerts);

        mUserId =  getIntent().getExtras().getString("USER_ID");
        mEvents = getIntent().getExtras().getString("USER_EVENT");
        mAlerts = getIntent().getExtras().getString("USER_ALERT");
        mName = getIntent().getExtras().getString("USER_NAME");
        Log.d("USER_ID",mUserId);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mEventListView = (ListView)findViewById(R.id.eventListView);
        mAlertListView = (ListView)findViewById(R.id.alertListView);
        mManageListView = (ListView)findViewById(R.id.manageListView);
        mAlertList = new ArrayList<Alert>();
        mEventList = new ArrayList<Event>();


        mAlertListAdapter = new AlertListAdapter(this,mAlertList);
        if(mAlertListView != null){
            mAlertListView.setAdapter(mAlertListAdapter);
        }

        mEventListAdapter = new EventListAdapter(this, mEventList);
        if(mEventListView != null){
            mEventListView.setAdapter(mEventListAdapter);
        }
        processEvent(mEvents);
        processAlert(mAlerts);

    }

    /**
     *This function is called when the activity starts
     * It connects the google play services onto the app for retrieving the location
     */
    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    /**
     * the activity activated by the send alert button to activate the send alert activity
     * passing the location data, user id, and user name
     *
     */
    public void sendAlert(View view){
        Intent intent = new Intent(this, sendAlertActivity.class);
        intent.putExtra("USER_CITY", mCity);
        intent.putExtra("USER_STATE",mState);
        intent.putExtra("USER_ADDRESS",mAddress);
        intent.putExtra("USER_ID",mUserId);
        intent.putExtra("USER_NAME",mName);
        startActivity(intent);
    }

    /**
     * function called when the activity is paused
     */
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        //stopLocationUpdates();
        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        Log.d("DEBUG", "Activity Paused");
    }


    /**
     * this function activates when the activity resumes
     * this triggets the checkplayservices function
     */
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Log.d("DEBUG", "Activity Resumed");

        checkPlayServices();
//        if(mGoogleApiClient.isConnected() && mRequestLocationUpdates) {
//            startLocationUpdates();
//        }
        if(isConnected()){
            //Toast.makeText(getBaseContext(),"Connected!",Toast.LENGTH_SHORT).show();
            //new EventHttpAsyncTask().execute("http://communitycloud.herokuapp.com/cloud/getEvents");
        }
        // Get the Camera instance as the activity achieves full user focus

    }

    /**
     * This function triggers when the activity stops
     * it disconnects the google play services
     *
     */
    @Override
    protected void onStop() {
        super.onStop();

        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     *
     * This function is triggered when the refresh icon is clicked,
     * it triggers the refresh asycn task pulling the event and alert data from the client server
     */
    public void alertRefresh(){
        if(isConnected()){
            new RefreshHttpAsyncTask().execute("http://communitycloud.herokuapp.com/cloud/refreshmobile?id="+mUserId);
        }
    }

    /**
     * this function is called to check whether the mobile device is connected to the internet
     */
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    /**
     * THis function returns triggers the HTTP get and processes the corresponding
     * JSON object into a string
     */
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = converter(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    /**
     * this function converts the response into a string json that can be processed
     */
    private static String converter(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    /**
     * This function retrieves the current location and sets the address information onto the variables
     * and triggers the updateLocation function
     */
    private void displayLocation() throws IOException {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            Geocoder geocoder;
            geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses;

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            Log.d("CURRENT", city);

            mCity = city;
            mState = state;
            mAddress = address;
            updateLocation();

            //Toast.makeText(this,"longitude" + mLongitude,Toast.LENGTH_SHORT).show();
            //lblLocation.setText(latitude + ", " + longtitude);
        } else {
            //lblLocation.setText("Couldn't get the location. Make sure location is enabled on the device");
        }
    }

    /**
     * a function that triggers when the mobile device is connected to the google play services
     */
    @Override
    public void onConnected(Bundle bundle) {
        try {
            displayLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this function is called when the mobile connection is suspended
     * it reconnects to the google play services
     */
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }


    /**
     * this function is triggered when the location has changed and updates the
     * location
     */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        try {
            displayLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * this function triggers when the connection fails
    * it logs the error onto the console
    */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: " + connectionResult.getErrorCode());
    }

    /**
     * This function is called to initilaise the google play services and location services
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * generates the location request used to extract current location
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * this function is used to check when the app is connected to google play services and
     * warns the user if they are not connected
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS) {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * sends a HTTP PUT onto the client server to update the user's current location
     */
    private void updateLocation() throws IOException {
        URL url = new URL("http://communitycloud.herokuapp.com/cloud/setlocation");
        HttpClient client = new DefaultHttpClient();
        HttpPut put= new HttpPut(String.valueOf(url));

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id", mUserId ));
        pairs.add(new BasicNameValuePair("city", mCity));
        pairs.add(new BasicNameValuePair("state", mState));
        put.setEntity(new UrlEncodedFormEntity(pairs));

        HttpResponse response = client.execute(put);

        //Toast.makeText(getBaseContext(),"PUT",Toast.LENGTH_SHORT).show();
    }


    /**
     * This refreshasynctask that pulls the information from the client server
     */
    private class RefreshHttpAsyncTask extends AsyncTask<String, Void, String> {
        RelativeLayout progressbarlayout;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressbarlayout = (RelativeLayout)findViewById(R.id.progressbarlayout);
            mProgressBar = (ProgressBar)findViewById(R.id.loadingSpinner);

            progressbarlayout.setVisibility(View.VISIBLE);
            // Show the progressbar
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            //Toast.makeText(getBaseContext(), "ALERTS", Toast.LENGTH_SHORT).show();
            try {
                //Toast.makeText(getBaseContext(),"TWO",Toast.LENGTH_SHORT).show();

                JSONObject json = new JSONObject(result);
                //JSONArray json = new JSONArray(result);
                Log.d("ALERTS", json.toString());
                int length = json.length();
                mAlertList.clear();
                for (int i = 0; i< length; i++) {

                    mAlerts = json.get("alert").toString();
                    mEvents = json.get("event").toString();

                }
                processEvent(mEvents);
                processAlert(mAlerts);
                //mProgressBar.setVisibility(View.GONE);
                //Toast.makeText(getBaseContext(),"TEST",Toast.LENGTH_LONG).show();
                //mResponse.setText(json.toString(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (mProgressBar.getVisibility() == View.VISIBLE){
                mProgressBar.setVisibility(View.GONE);
            }

        }
    }



    private void processEvent(String eventString){
        try{
            JSONArray eventJson = null;
            eventJson = new JSONArray(eventString);
            Log.d("EVENTS", eventJson.toString());
            int eventLength = eventJson.length();
            mEventList.clear();
            for (int i = 0; i< eventLength; i++) {
                String alertType = eventJson.getJSONObject(i).getString("alertType");
                String details = eventJson.getJSONObject(i).getString("details");
                String alertRating = eventJson.getJSONObject(i).getString("rating");
                String address = eventJson.getJSONObject(i).getString("address");
                String city = eventJson.getJSONObject(i).getString("city");
                String state = eventJson.getJSONObject(i).getString("state");
                String date = eventJson.getJSONObject(i).getString("created");
                Event event = new Event(details,alertType,alertRating ,date,address,city,state);
                mEventList.add(event);
            }
            mEventListAdapter.notifyDataSetChanged();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processAlert(String alertstring){
        JSONArray alertJson = null;
        try {
            alertJson = new JSONArray(alertstring);
            Log.d("ALERTS", alertJson.toString());
            int length = alertJson.length();
            mAlertList.clear();
            for (int i = 0; i < length; i++) {
                String alertType = alertJson.getJSONObject(i).getString("alertType");
                String details = alertJson.getJSONObject(i).getString("details");
                String alertRating = alertJson.getJSONObject(i).getString("rating");
                String address = alertJson.getJSONObject(i).getString("address");
                String city = alertJson.getJSONObject(i).getString("city");
                String state = alertJson.getJSONObject(i).getString("state");
                String date = alertJson.getJSONObject(i).getString("created");
                Alert alert = new Alert(details, alertType, alertRating, date, address, city, state);
                mAlertList.add(alert);
            }
            mAlertListAdapter.notifyDataSetChanged();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_events);


                if (mEventListView.getVisibility() == View.GONE){
                    mEventListView.setVisibility(View.VISIBLE);
                }
                if (mAlertListView.getVisibility() == View.VISIBLE){
                    mAlertListView.setVisibility(View.GONE);
                }
                if (mManageListView.getVisibility() == View.VISIBLE){
                    mManageListView.setVisibility(View.GONE);
                }

                break;
            case 2:
                mTitle = getString(R.string.title_alerts);


                if (mAlertListView.getVisibility() == View.GONE){
                    mAlertListView.setVisibility(View.VISIBLE);
                }
                if (mEventListView.getVisibility() == View.VISIBLE){
                    mEventListView.setVisibility(View.GONE);
                }
                if (mManageListView.getVisibility() == View.VISIBLE){
                    mManageListView.setVisibility(View.GONE);
                }

                break;
            case 3:
                mTitle = getString(R.string.title_manage);

                if (mManageListView.getVisibility() == View.GONE){
                    mManageListView.setVisibility(View.VISIBLE);
                }
                if (mAlertListView.getVisibility() == View.VISIBLE){
                    mAlertListView.setVisibility(View.GONE);
                }
                if (mEventListView.getVisibility() == View.VISIBLE){
                    mEventListView.setVisibility(View.GONE);
                }

                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        Log.d("TEST_KEY",mTitle.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main_alerts, menu);
            restoreActionBar();

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_alerts, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainDisplayActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }



}
