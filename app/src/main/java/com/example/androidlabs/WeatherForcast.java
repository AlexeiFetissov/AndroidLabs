package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForcast extends AppCompatActivity {

    TextView valueText;
    TextView minText;
    TextView maxText;
    TextView uvText;
    ImageView wview;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forcast);

        pb = (ProgressBar) findViewById(R.id.my_pb);
        pb.setVisibility(View.VISIBLE);
        valueText = (TextView) this.findViewById(R.id.et_current_weather);
        minText = (TextView) this.findViewById(R.id.et_min_temperature);
        maxText = (TextView) this.findViewById(R.id.et_max_temperature);
        uvText = (TextView) this.findViewById(R.id.et_uv_rating);
        wview = (ImageView) this.findViewById(R.id.iv_weather);

        new ForecastQuery().execute("");
    }


    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        String windUV;
        String min;
        String max;
        String value;
        String icon;
        Bitmap imgcw;
        double uv;
        String TAG;

        @Override
        protected String doInBackground(String... strings) {
            String ret;
            String queryURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
            try {       // Connect to the server:
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = null;
                inStream = urlConnection.getInputStream(); //text response returned from server

                //Set up the XML parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");

                //Iterate over the XML tags:
                int EVENT_TYPE;         //While not the end of the document:
                boolean more = true;

                while (more && (EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT) {
                    switch (EVENT_TYPE) {
                        case START_TAG:         //This is a start tag < ... >
                            String tagName = xpp.getName(); // What kind of tag?

                            if (tagName.equals("temperature")) {
                                max = xpp.getAttributeValue(null, "max");
                                publishProgress(25);
                                publishProgress(50);
                                publishProgress(75);
                                min = xpp.getAttributeValue(null, "min");
                                publishProgress(25);
                                publishProgress(50);
                                publishProgress(75);

                                value = xpp.getAttributeValue(null, "value");//What is the String associated with message?
                                publishProgress(25);
                                publishProgress(50);
                                publishProgress(75);
                            }

                            if (tagName.equals("weather")) {
                                icon = xpp.getAttributeValue(null, "icon");
                                more = false;
                            }
                            break;
                        case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:              //This is text between tags < ... > Hello world </ ... >
                            break;
                    }

                    if (more) {
                        xpp.next(); // move the pointer to next XML element
                    }
                }
            } catch (MalformedURLException mfe) {
                ret = "Malformed URL exception";
            } catch (IOException ioe) {
                ret = "IO Exception. Is the Wifi connected?";
            } catch (XmlPullParserException pe) {
                pe.printStackTrace();
                ret = "XML Pull exception. The XML is not properly formed";
            }

            // get UV
            try {
                String urlString = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = null;
                inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                String result = sb.toString();
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    uv = jObject.getDouble("value");
                } catch (Exception e) {
                }
            } catch (MalformedURLException mfe) {
                ret = "Malformed URL exception";
            } catch (IOException ioe) {
                ret = "IO Exception. Is the Wifi connected?";
            }
            // get the weather icon
            if (fileExistance(icon + ".png")) {
                FileInputStream fis = null;
                try {
                    fis = openFileInput(icon + ".png");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imgcw = BitmapFactory.decodeStream(fis);
                publishProgress(100);
                Log.i(TAG, "doInBackground: looking for " + icon + ".png.Found locally. ");
            } else {
                try {
                    String urlString = "http://openweathermap.org/img/w/" + icon + ".png";
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        imgcw = BitmapFactory.decodeStream(connection.getInputStream());
                    }
                } catch (MalformedURLException mfe) {
                    ret = "Malformed URL exception";
                } catch (IOException ioe) {
                    ret = "IO Exception. Is the Wifi connected?";
                }

                FileOutputStream outputStream = null;
                try {
                    outputStream = openFileOutput(icon + ".png", Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imgcw.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                try {
                    outputStream.flush();
                    outputStream.close();
                    // publishProgress(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //What is returned here will be passed as a parameter to onPostExecute:
            ret = null;
            return ret;
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            //update GUI Stuff:
            valueText.setText(value);
            minText.setText(min);
            maxText.setText(max);
            Double uo = uv;
            uvText.setText(uo.toString());
            wview.setImageBitmap(imgcw);

            pb.setVisibility(View.INVISIBLE);
        }
        @Override
        protected void onProgressUpdate(Integer... value){
            super.onProgressUpdate(value);
            pb.setVisibility(View.VISIBLE);
            int v = value[0].intValue();
            pb.setProgress(v);
        }

    }
}
