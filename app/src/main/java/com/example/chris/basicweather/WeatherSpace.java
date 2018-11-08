package com.example.chris.basicweather;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WeatherSpace {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String GetSpaceWeather (Context context){
        String bomData = GetBOMData();
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(bomData);
        JsonObject rootArr = root.getAsJsonObject();
        String response = rootArr.get("data").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
        System.out.println(response);

        return "";
    };

    public static String GetBOMData() {
        final String kaus_value;
        String region = "Hobart";
        String response_string = "";
        String jsonRequest = "{\"api_key\": \"6cb000fc-c598-46f1-b75a-cec78b25b1a3\",\"options\":{\"location\":\"" + region + "\"}}";
        try {
            URL url=new URL("http://sws-data.sws.bom.gov.au/api/v1/get-k-index");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("X-HTTP-Method-Override", "POST");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(jsonRequest.getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                response_string += (line + "\n");
            }
            br.close();
            System.out.println("" + response_string);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response_string;
    };


}
