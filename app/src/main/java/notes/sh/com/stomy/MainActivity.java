package notes.sh.com.stomy;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather mCurrentWeather;
    private TextView mWeatherTextView;
    private Button mShowQuoteButton;
    private Button mrelayTurnOffButton;
    private Button mrelayTurnOnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWeatherTextView = (TextView)findViewById(R.id.testtextView);
        mShowQuoteButton = (Button)findViewById(R.id.httpbutton);
        mrelayTurnOffButton = (Button)findViewById(R.id.relayOffButton);
        mrelayTurnOnButton = (Button)findViewById(R.id.relayOnButton);


        String apiKey = "fd49cd2d94dcec990df5dd28d53d1ad4";

        double latitude = 37.8267;
        double longitude = -122.423;

        //overide mainthread network exception going put this into new thread
        String forecastURI = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getData();


            }


        };

        View.OnClickListener listenerOff = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                turnOff();


            }


        };


        View.OnClickListener listenerOn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                turnOn();


            }


        };

        mShowQuoteButton.setOnClickListener(listener);
        mrelayTurnOffButton.setOnClickListener(listenerOff);
        mrelayTurnOnButton.setOnClickListener(listenerOn);


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(forecastURI)
                .build();
        //call object
        Call call = client.newCall(request);
        //execute call in queue
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {


                    String jsonData = response.body().string();

                    Log.v(TAG, response.body().string());
                    if (response.isSuccessful()) {

                        mCurrentWeather = getCurrentDetails(jsonData);

                    } else {

                        alertUserAboutError();
                    }

                } catch (IOException e) {
                    Log.e(TAG, "Exception caught", e);
                } catch (JSONException e) {
                    Log.e(TAG, "Exception caught", e);
                }
            }


        });


        Log.d(TAG, "Main UI code is running");
    }



    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {

        //class called jsonObject pass in string of jsonData
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG,"From json............................"+timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTimeZone(timezone);

        Log.d(TAG, "Current Time Formatted ............."+currentWeather.getFormatTime() );

        return new CurrentWeather();

    }

    private void alertUserAboutError() {

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");


    }

    public void getData() {


        Log.d(TAG, "result ............................................................................" );
      //  int color = mColorWheel.getColor();
        // mRelativeLayout.setBackgroundColor(color);

        String result = "";
        InputStream isr = null;
      //  mWeatherTextView.setText(result);


//remember about the build gradle add on
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://funtimequote.com/readData.php");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        }
        catch(Exception e) {
            String  connectionError = "No connection";
            mWeatherTextView.setText(connectionError);
            Log.e("log_tag", "Error in http connection " + e.toString());

        }

        //convert response to string

        try {
            // Toast.makeText(FunQuotesActivity.this,
            //        "Your Message:"+isr+"", Toast.LENGTH_LONG).show();
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            //  while ((line = reader.readLine()) != null) {
            //    sb.append(line + "\n");
            // }

            line = reader.readLine();
            sb.append(line + "\n");

            isr.close();

            result=sb.toString();
            Log.d(TAG, "result ............................................................................" + sb);
            mWeatherTextView.setText(result);
        }
        catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }

        //parse json data
        try {
            mWeatherTextView.setText(result);
            //  mSqlTextView.setText(result);
        }
        catch(Exception e) {
            Log.e("log_tag", "Couldn't set text, damnit.");
        }

    }


    private void turnOff() {

        Log.d(TAG, "Turning OFFFFF ............................................................................" + "");

        String result = "";
        InputStream isr = null;
        //  mWeatherTextView.setText(result);


//remember about the build gradle add on
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://funtimequote.com/relayOff.php");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        }
        catch(Exception e) {
            String  connectionError = "No connection";
            mWeatherTextView.setText(connectionError);
            Log.e("log_tag", "Error in http connection " + e.toString());

        }

    }


    private void turnOn() {

        Log.d(TAG, "Turning ONNNNNNNNNHHN ............................................................................" + "");

        String result = "";
        InputStream isr = null;
        //  mWeatherTextView.setText(result);


//remember about the build gradle add on
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://funtimequote.com/relayOn.php");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        }
        catch(Exception e) {
            String  connectionError = "No connection";
            mWeatherTextView.setText(connectionError);
            Log.e("log_tag", "Error in http connection " + e.toString());

        }



    }


}

