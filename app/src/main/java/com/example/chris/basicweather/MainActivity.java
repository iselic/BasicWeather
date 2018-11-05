package com.example.chris.basicweather;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import me.anwarshahriar.calligrapher.Calligrapher;


public class MainActivity extends AppCompatActivity {

    TextView  temp_heading,temp_field;
    Typeface customFont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp_field = findViewById(R.id.temp_field);
        temp_heading = findViewById(R.id.temp_heading);
        Button button = findViewById(R.id.button);
        customFont = Typeface.createFromAsset(getAssets(), "fonts/ObelixPro-cyr.ttf");
        temp_heading.setTypeface(customFont);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"fonts/ObelixPro-cyr.ttf",true);

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                temp_field.setText(String.valueOf(23));
                DownloadWeather task = new DownloadWeather();
                task.execute("balbal");
            }
        });
    }

    class DownloadWeather extends AsyncTask< String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            String xml;
            try {
//                xml = Function.getData(args[0]);
                String city = Function.getCity(getApplication(),-42.8897,147.3278);
                xml = Function.getWeather(city);
            } catch (IOException e){
                xml = e.toString();
            }
            return xml;
        }

        protected void onPostExecute(String xml) {
            Toast.makeText(getApplicationContext(), xml, Toast.LENGTH_LONG).show();
        }
    }
}
