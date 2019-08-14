package net.kampungweb.myjobscheduler;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class GetCurrentWeatherJobService extends JobService {

    public static final String TAG = GetCurrentWeatherJobService.class.getSimpleName();
    final String APP_ID = "ISIKAN DENGAN API KEY ANDA";
    final String CITY = "Jakarta";
    //final String CITY = "ISIKAN DENGAN NAMA KOTA ANDA";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob Executed");
        getCurrentWeather(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob() Executed");
        return true;
    }

    private void getCurrentWeather(final JobParameters job) {
        Log.d(TAG, "Running");
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "htp://api.openweathermap.org/data/2.5/weather?q="+CITY+"appid="+APP_ID;
        Log.e(TAG, "getCurrentWeather: "+ url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    String currentWeather = responseObject.getJSONArray("weather").getJSONObject(0).getString("main");
                    String description = responseObject.getJSONArray("weather").getJSONObject(0).getString("description");
                    double tempInKelvin = responseObject.getJSONObject("main").getDouble("temp");
                    double tempInCelcius = tempInKelvin - 273;
                    String temperature = new DecimalFormat("##.##").format(tempInCelcius);
                    String title = "Current Weather";
                    String message = currentWeather + ", " + description + "with " + temperature + " celcius";
                    int notifId = 100;

                    showNotification(getApplicationContext(), title, message, notifId);
                    jobFinished(job, false);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void showNotification(Context context, String title, String message, int notifId) {
    }
}
