package com.example.chris.basicweather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FragmentWeather extends Fragment {
    View view;
    TextView today_forecast_text,max_temp_text,precip_text,current_temp_text,app_temp_text;

    public FragmentWeather() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.weather_fragment,container,false);


        today_forecast_text = view.findViewById(R.id.today_forecast_summary);
        max_temp_text = view.findViewById(R.id.max_temp_text);
        precip_text = view.findViewById(R.id.precip_chance);
        current_temp_text = view.findViewById(R.id.current_temp_text);
        app_temp_text = view.findViewById(R.id.app_temp_text);

        return view;
    }




}
