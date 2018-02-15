package com.michael.commcloudapp;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.michael.commcloudapp.Models.Event;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * this activity is used to create a form and send the alert message to both servers
 */
public class sendAlertActivity extends Activity {



    private Spinner mAlertTypeSpinner;
    private EditText mAlertDetailText;
    private Spinner mAlertRatingSpinner;
    private  EditText mAlertLocationText;
    private EditText mAddress;
    private EditText mCity;
    private EditText mState;

    private String mAlertType;
    private String mAlertRating;
    private String mAlertLocation;
    private String mAlertDetails;
    private String mAlertAddress;
    private String mAlertCity;
    private String mAlertState;
    private String mUserId;
    private String mName;


    /**
     * initialises the variables and generates the views
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_alert);

        mAlertTypeSpinner = (Spinner)findViewById(R.id.alertTypeSpinner);
        mAlertDetailText = (EditText)findViewById(R.id.alertDetailText);
        mAlertRatingSpinner = (Spinner)findViewById(R.id.alertRatingSpinner);
        //mAlertLocationText = (EditText)findViewById(R.id.alertLocationText);
        mAddress = (EditText)findViewById(R.id.alertAddressIn);
        mCity = (EditText)findViewById(R.id.cityIn);
        mState = (EditText)findViewById(R.id.stateIn);


        mAlertAddress =  getIntent().getExtras().getString("USER_ADDRESS");
        mAlertCity = getIntent().getExtras().getString("USER_CITY");
        mAlertState = getIntent().getExtras().getString("USER_STATE");
        mUserId = getIntent().getExtras().getString("USER_ID");
        mName = getIntent().getExtras().getString("USER_NAME");
        mAddress.setText(mAlertAddress);
        mCity.setText(mAlertCity);
        mState.setText(mAlertState);

        final String[] typesArray = getResources().getStringArray(R.array.alertTypeArray);
        final String[] ratingsArray = getResources().getStringArray(R.array.alertRatingArray);

        ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, typesArray);

        ArrayAdapter<String> ratingsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, ratingsArray);

        mAlertTypeSpinner.setAdapter(typesAdapter);
        mAlertRatingSpinner.setAdapter(ratingsAdapter);

        mAlertTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();
                mAlertType = typesArray[index];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAlertRatingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();
                mAlertRating = ratingsArray[index];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


    }


    /**
     * This function is triggered when the send button is clicked
     * It sends a HTTP Post to both servers and sends an alert message to emergency services
     * @param view
     */
    public void sendAlert(View view){
        //new alertSendTask().execute();

        Date date = new Date();
        String dateString = date.toString();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://emergencyservicecloud.herokuapp.com/cloud/mobileAlert");

        mAlertDetails = mAlertDetailText.getText().toString();
        mAlertAddress = mAddress.getText().toString();
        mAlertCity = mCity.getText().toString();
        mAlertState = mState.getText().toString();


        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("AlertType", mAlertType));
            nameValuePairs.add(new BasicNameValuePair("details", mAlertDetails));
            nameValuePairs.add(new BasicNameValuePair("address", mAlertAddress));
            nameValuePairs.add(new BasicNameValuePair("city", mAlertCity));
            nameValuePairs.add(new BasicNameValuePair("state", mAlertState));
            nameValuePairs.add(new BasicNameValuePair("rating", mAlertRating));
            nameValuePairs.add(new BasicNameValuePair("createdBy", mName));
            nameValuePairs.add(new BasicNameValuePair("createdId", mUserId));
            nameValuePairs.add(new BasicNameValuePair("created", dateString));

            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }


            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            Toast.makeText(getBaseContext(),"DONE!", Toast.LENGTH_SHORT).show();
            String line = "";
            while ((line = rd.readLine()) != null) {
                //System.out.println(line);
                Log.d("DEBUG", line);
                if (line.startsWith("Auth=")) {
                    String key = line.substring(5);
                    // do something with the key
                }

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
        }
        //finish();
        sendClientAlert();

    }

    private void sendClientAlert(){
        //new alertSendTask().execute();

        Date date = new Date();
        String dateString = date.toString();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://communitycloud.herokuapp.com/cloud/mobileAlert");

        mAlertDetails = mAlertDetailText.getText().toString();
        mAlertAddress = mAddress.getText().toString();
        mAlertCity = mCity.getText().toString();
        mAlertState = mState.getText().toString();


        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("AlertType", mAlertType));
            nameValuePairs.add(new BasicNameValuePair("details", mAlertDetails));
            nameValuePairs.add(new BasicNameValuePair("address", mAlertAddress));
            nameValuePairs.add(new BasicNameValuePair("city", mAlertCity));
            nameValuePairs.add(new BasicNameValuePair("state", mAlertState));
            nameValuePairs.add(new BasicNameValuePair("rating", mAlertRating));
            nameValuePairs.add(new BasicNameValuePair("createdBy", mName));
            nameValuePairs.add(new BasicNameValuePair("createdId", mUserId));
            nameValuePairs.add(new BasicNameValuePair("created", dateString));

            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }


            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            Toast.makeText(getBaseContext(),"DONE!", Toast.LENGTH_SHORT).show();
            String line = "";
            while ((line = rd.readLine()) != null) {
                //System.out.println(line);
                Log.d("DEBUG", line);
                if (line.startsWith("Auth=")) {
                    String key = line.substring(5);
                    // do something with the key
                }

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
        }
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
