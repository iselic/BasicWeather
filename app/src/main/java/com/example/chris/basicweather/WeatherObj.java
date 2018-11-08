package com.example.chris.basicweather;

public class WeatherObj {

    public String summary, precipitation,max_temp,current_temp, apparent_temp, prob_precipitation = "";

    public WeatherObj(String forecast, String maxTemp, String currentTemp, String appTemp, String probPrecip){
        summary = forecast;
        max_temp = maxTemp;
        current_temp = currentTemp;
        apparent_temp = appTemp;
        prob_precipitation = probPrecip;

    }

    public WeatherObj(String brief, String precip){
        summary = brief;
        precipitation = precip;
    }


}
