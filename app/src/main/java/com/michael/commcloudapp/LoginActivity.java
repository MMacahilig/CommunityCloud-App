package com.michael.commcloudapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.michael.commcloudapp.Models.User;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Login activity that displays the login form for the user to log into the app
 */
public class LoginActivity extends Activity {
    private String mEmail;
    private String mPassword;
    private String mStatus;
    private String mEvents;
    private String mAlerts;

    private EditText mUserNameInput;
    private EditText mPasswordInput;
    private TextView mErrorMessage;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        mUserNameInput = (EditText)findViewById(R.id.usernameInput);
        mPasswordInput = (EditText)findViewById(R.id.passwordInput);
        mErrorMessage = (TextView)findViewById(R.id.errorTitleID);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * the function is triggered when the sign in button is clicked, it triggers the loginasynctask
     * when it finds that the input views are not empty
     * @param view
     */
    public void loginFunction(View view) {


        if(mErrorMessage.getVisibility() == View.VISIBLE){
            mErrorMessage.setVisibility(View.GONE);
        }
        String username = mUserNameInput.getText().toString().trim();
        String password = mPasswordInput.getText().toString();
        if(username.isEmpty() && password.isEmpty()){
            mErrorMessage.setText(R.string.login_error_1);
            mErrorMessage.setVisibility(View.VISIBLE);
        }else if(mUserNameInput.getText().toString().isEmpty()) {
            mErrorMessage.setText(R.string.login_error_2);
            mErrorMessage.setVisibility(View.VISIBLE);
        }else if(mPasswordInput.getText().toString().isEmpty()) {
            mErrorMessage.setText(R.string.login_error_3);
            mErrorMessage.setVisibility(View.VISIBLE);
        }else {

            new LoginAsyncTask().execute();
        }
    }

    /**
     * the loginasynctask is an asynctask that communuicates with the server to check whether the
     * client server if the user credentials match with the database
     */
    private class LoginAsyncTask extends AsyncTask<Void, Void, Void>{
        private View mProgressBar;

        private LoginAsyncTask() {
        }


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            // Reference to the progressbar
            mProgressBar = findViewById(R.id.activity_main_progressBar);

            // Show the progressbar
            mProgressBar.setVisibility(View.VISIBLE);

            mEmail = mUserNameInput.getText().toString();
            mPassword = mPasswordInput.getText().toString();

        }

        /**
         * the asynctask that sends a HTTP POST onto the server and sets the mStatus
         * so that the main display activity starts if the set status is 200
         * @param voids
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            try {


                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://communitycloud.herokuapp.com/users/mobilelogin");
                // Update all the bus rows with the new arrival time.
                // Normally you would go to a server here and get the latest arrival times for the buses.
                // This can take some time in reality
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("email", mEmail));
                nameValuePairs.add(new BasicNameValuePair("password", mPassword));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }


                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                //Toast.makeText(getBaseContext(), "DONE!", Toast.LENGTH_SHORT).show();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    //mStatus = line;
                    JSONObject json;
                    try {
                        Log.d("DEBUG", "RETURN" +line);
                        json = new JSONObject(line);
                        mStatus = json.get("status").toString();
                        String firstname = json.get("firstName").toString();
                        String lastname = json.get("lastName").toString();
                        String id = json.get("id").toString();
                        String email = json.get("email").toString();
                        String address = json.get("firstName").toString();
                        String city = json.get("firstName").toString();
                        String state = json.get("firstName").toString();
                        mAlerts = json.get("alert").toString();
                        mEvents = json.get("event").toString();

                        mUser = new User(firstname, lastname,  id,  email, address, city, state);
                        Log.d("STATUS",mStatus);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("RETURN", line);
                    if (line.startsWith("Auth=")) {
                        String key = line.substring(5);
                        // do something with the key
                    }

                }

                //finish();



        } catch (ClientProtocolException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
        } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void aVoid)
        {

            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            mUserNameInput.setText("");
            mPasswordInput.setText("");
            //Log.d("auth",mStatus);
            if (mStatus != null){
                if (mStatus.equals("200")){
                    Intent intent = new Intent(getBaseContext(), MainDisplayActivity.class);
                    intent.putExtra("USER_ID", mUser.getId());
                    intent.putExtra("USER_ADDRESS", mUser.getAddress());
                    intent.putExtra("USER_CITY", mUser.getCity());
                    intent.putExtra("USER_STATE", mUser.getState());
                    intent.putExtra("USER_EVENT",mEvents);
                    intent.putExtra("USER_ALERT",mAlerts);
                    intent.putExtra("USER_NAME", mUser.getFirstName() + " " + mUser.getLastName());

                    startActivity(intent);
                } else {
                    if(mErrorMessage.getVisibility() == View.VISIBLE){
                        mErrorMessage.setVisibility(View.GONE);
                    } else {
                        mErrorMessage.setText(R.string.authError);
                        mErrorMessage.setVisibility(View.VISIBLE);
                    }
                }
            }

        }
    }
}
