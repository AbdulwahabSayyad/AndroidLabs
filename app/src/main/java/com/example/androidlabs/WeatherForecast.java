package com.example.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        ProgressBar progressBar = findViewById(R.id.progbar);
        progressBar.setVisibility(View.VISIBLE);
        ForeCastQuery forecastQuery = new ForeCastQuery();
        forecastQuery.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");

    }

    public class ForeCastQuery extends AsyncTask<String, Integer, String>{
        String currentTemp, minTemp, maxTemp,windSpeed, weatherIconName;
        double uvRate;
        Bitmap weatherIcon;

        @Override
        protected String doInBackground(String... params) {
            try {
                String urlString = params[0];
                URL url;
                url = new URL(urlString);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput( inStream  , "UTF-8");  //inStream comes from line 46

                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        String tagName = parser.getName(); //get the name of the starting tag: <tagName>
                        if (tagName.equals("temperature")) {
                            Log.e("Tag name", tagName);
                            currentTemp = parser.getAttributeValue(null, "value");
                            Log.e("Current temp", currentTemp);
                            publishProgress(15);
                            minTemp = parser.getAttributeValue(null, "min");
                            Log.e("Minimum temp", minTemp);
                            publishProgress(30);
                            maxTemp = parser.getAttributeValue(null, "max");
                            Log.e("Maximum temp", maxTemp);
                            publishProgress(45);

                        }
                        if (tagName.equals("weather")) {
                            weatherIconName = parser.getAttributeValue(null, "icon");
                        }
                        if (tagName.equals("speed")) {
                            windSpeed = parser.getAttributeValue(null, "value");
                            Log.e("Wind speed", windSpeed);
                            publishProgress(60);
                        }
                    }  parser.next();
                }

                if(!fileExistence(weatherIconName + ".png")) {
                    URL uRL = new URL("http://openweathermap.org/img/w/" + weatherIconName + ".png");
                    HttpURLConnection connection = (HttpURLConnection) uRL.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        weatherIcon = BitmapFactory.decodeStream(connection.getInputStream());
                    }
                    Log.e("WeatherIcon Downloaded ", weatherIcon.toString());
                    publishProgress(75);

                    Bitmap image = weatherIcon;
                    FileOutputStream outputStream = openFileOutput(weatherIconName + ".png", Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }

                if(fileExistence(weatherIconName + ".png")) {
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(weatherIconName + ".png");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    weatherIcon = BitmapFactory.decodeStream(fis);
                    publishProgress(75);
                    Log.e("Weather Icon Local", weatherIcon.toString());
                }

                URL UVurl = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                inStream = UVConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null){
                sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONObject jsonObject = new JSONObject(result);
                uvRate = jsonObject.getLong("value");
                Log.e("UV Rating", ""+uvRate);
                publishProgress(100);

                Thread.sleep(2000);

            }catch (Exception ex)
            {
                Log.e("Crash", ex.getMessage());
            }

            return "";
        }

        public boolean fileExistence(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
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
            TextView uvRating = findViewById(R.id.uvrating);
            TextView windSp = findViewById(R.id.windspeed);
            ImageView imageView = findViewById(R.id.imageview);
            current.setText("Current temperature: " + currentTemp + "°C");
            min.setText("Min temperature: " + minTemp + "°C");
            max.setText("Max Temperature: " + maxTemp + "°C");
            uvRating.setText("UV Rating: " + uvRate);
            windSp.setText("Wind speed: " + windSpeed + " KM/H");
            imageView.setImageBitmap(weatherIcon);
            ProgressBar progressBar = findViewById(R.id.progbar);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
