package com.example.chris.basicweather;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import me.anwarshahriar.calligrapher.Calligrapher;


public class MainActivity extends AppCompatActivity {

    TextView  temp_field,today_forecast_text, max_temp_text,precip_text,current_temp_text,app_temp_text;
    Typeface customFont;
    private TabLayout tablayout;
    private ViewPager viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tablayout = findViewById(R.id.tablayout);
        viewpager = findViewById(R.id.viewpager);

        ViewPagerAdaptor adapter = new ViewPagerAdaptor(getSupportFragmentManager());
        adapter.AddFragment(new FragmentWeather(),"Weather");
        adapter.AddFragment(new FragmentSpaceWeather(),"Space Weather");
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);


        Button button = findViewById(R.id.button);

        DownloadWeather task = new DownloadWeather();
        task.execute("balbal");
//        customFont = Typeface.createFromAsset(getAssets(), "fonts/ObelixPro-cyr.ttf");
//        temp_heading.setTypeface(customFont);

//        Calligrapher calligrapher = new Calligrapher(this);
//        calligrapher.setFont(this,"fonts/OpenSanse-Regular.ttf",false);

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                temp_field.setText(String.valueOf(23));
                GetSpaceWeather task = new GetSpaceWeather();
                task.execute("balbal");
            }
        });
    }

    class DownloadWeather extends AsyncTask< String, Void, WeatherObj > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected WeatherObj doInBackground(String... args) {
            String xml;
            WeatherObj weather = new WeatherObj("","");
            try {
//                xml = Function.getData(args[0]);
                String city = Function.getCity(getApplication(),-42.8897,147.3278);
//                weather = Function.getWeather(city);
                weather = WeatherBOM.TodaysWeather(getApplication());
            } catch (IOException e){
                xml = e.toString();
            }
            return weather;
        }

        protected void onPostExecute(WeatherObj weather) {

            FragmentWeather fragment = (FragmentWeather) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
//            Toast.makeText(getApplicationContext(), xml, Toast.LENGTH_LONG).show();
            fragment.today_forecast_text.setText(weather.summary);
            fragment.max_temp_text.setText(weather.max_temp);
            fragment.current_temp_text.setText(weather.current_temp);
            fragment.app_temp_text.setText(weather.apparent_temp);
            fragment.precip_text.setText(weather.prob_precipitation);
        }
    }

    class GetSpaceWeather extends AsyncTask< String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            String xml;
            String weather = "";

//                xml = Function.getData(args[0]);
//                String city = Function.getCity(getApplication(),-42.8897,147.3278);
//                weather = Function.getWeather(city);
                weather = WeatherSpace.GetSpaceWeather(getApplication());

            return weather;
        }

        protected void onPostExecute(String weather) {

            FragmentSpaceWeather fragment = (FragmentSpaceWeather) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 1);
////            Toast.makeText(getApplicationContext(), xml, Toast.LENGTH_LONG).show();
            fragment.kaus_text.setText(weather);
//            fragment.max_temp_text.setText(weather.max_temp);
//            fragment.current_temp_text.setText(weather.current_temp);
//            fragment.app_temp_text.setText(weather.apparent_temp);
//            fragment.precip_text.setText(weather.prob_precipitation);
        }
    }
}
