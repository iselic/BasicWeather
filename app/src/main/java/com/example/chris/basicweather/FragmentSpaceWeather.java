package com.example.chris.basicweather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentSpaceWeather extends Fragment {
    View view;
    TextView kaus_text;
    public FragmentSpaceWeather() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.space_weather_fragment,container,false);

        kaus_text = view.findViewById(R.id.kaus_index);

        return view;
    }
}
