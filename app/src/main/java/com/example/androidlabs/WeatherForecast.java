package com.example.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        TextView current = findViewById(R.id.currenttemp);
        TextView min = findViewById(R.id.mintemp);
        TextView max = findViewById(R.id.maxtemp);
        ProgressBar progressBar = findViewById(R.id.progbar);
        progressBar.setVisibility(View.VISIBLE);
    }

    public class ForeCastQuery extends AsyncTask<String, Integer, String>{
        String windSpeed,currentTemp, minTemp, maxTemp, weatherIconName;
        Bitmap weatherIcon;

        @Override
        protected String doInBackground(String... params) {
            try {
                String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
                URL url = null;
                url = new URL(urlString);

                HttpURLConnection conn = null;
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                InputStream stream = conn.getInputStream();
                parser.setInput(stream, null);

                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        String tagName = parser.getName(); //get the name of the starting tag: <tagName>
                        if (tagName.equals("temperature")) {
                            currentTemp = parser.getAttributeValue(null, "value");
                            publishProgress(25);
                            minTemp = parser.getAttributeValue(null, "min");
                            publishProgress(50);
                            maxTemp = parser.getAttributeValue(null, "max");
                            publishProgress(75);

                        }
                        if (tagName.equals("weather")) {
                            weatherIconName = parser.getAttributeValue(null, "icon");
                        }
                    } else parser.next();
                }
            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.getMessage() );
            }

//            URL url = new URL("http://openweathermap.org/img/w/" + weatherIconName + ".png");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.connect();
//            int responseCode = connection.getResponseCode();
//            if (responseCode == 200) {
//                weatherIcon = BitmapFactory.decodeStream(connection.getInputStream());
//            }
//            publishProgress(100);
//            Bitmap image  = HTTPUtils.getImage();
//            FileOutputStream outputStream = openFileOutput( weatherIconName + ".png", Context.MODE_PRIVATE);
//            image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
//            outputStream.flush();
//            outputStream.close();



            return "Finished task";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            ProgressBar progressBar = findViewById(R.id.progbar);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            TextView current = findViewById(R.id.currenttemp);
            TextView min = findViewById(R.id.mintemp);
            TextView max = findViewById(R.id.maxtemp);
            current.setText("Current temperature: " + currentTemp);
            min.setText("Min temperatureee: " + minTemp);
            max.setText("Max temperature: " + maxTemp);
            ProgressBar progressBar = findViewById(R.id.progbar);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
