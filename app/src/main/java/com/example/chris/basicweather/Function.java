package com.example.chris.basicweather;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.net.ftp.FTPClient;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import static java.nio.file.Files.createFile;

public class Function {

    public static String getData(String query) throws IOException {
        String surl = "http://services.swpc.noaa.gov/products/solar-wind/mag-1-day.json";
//        surl = "https://jsonplaceholder.typicode.com/todos/1";
        URL url = new URL(surl);
        URLConnection request = url.openConnection();
        request.connect();
//        JsonParser jp = new JsonParser(); //from gson
//        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
//        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
//        String zipcode = rootobj.get("title").getAsString();
        String inputLine;
        StringBuffer response = new StringBuffer();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(request.getInputStream()));
        // feed response into the StringBuilder
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

//        JsonParser jp = new JsonParser();
//        JsonElement root = jp.parse(new InputStreamReader((InputStream)request.getContent()));
//        JsonArray rootobj = root.getAsJsonArray();
//        final double solar_wind = rootobj.get(rootobj.size()-1).getAsJsonArray().get(2).getAsDouble();
        Log.d("MSG", response.toString());
        return response.toString();
    }

    public static String getCity(Context context,Double latitude, Double longitude) throws IOException {
        String val="asd";
        ArrayList<LatLon> list = new ArrayList<>();
        ArrayList<Double> distances = new ArrayList<>();
        ArrayList<String> place_names = new ArrayList<>();

        String jsonString = loadJSONFromAsset(context);
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(jsonString);
        JsonObject root1 = root.getAsJsonObject();
        JsonArray features = root1.get("features").getAsJsonArray();
        for (int i = 0; i < features.size(); i++) {
            JsonObject point = features.get(i).getAsJsonObject();
            Double lat = point.getAsJsonObject().get("properties").getAsJsonObject().get("LAT").getAsDouble();
            Double lon = point.getAsJsonObject().get("properties").getAsJsonObject().get("LON").getAsDouble();
            String name = point.getAsJsonObject().get("properties").getAsJsonObject().get("PT_NAME").toString();
            list.add(new LatLon(lat,lon));
            place_names.add(name);
        }

        for (int i = 0; i < list.size(); i++) {
            Double lat = list.get(i).latitude;
            Double lon = list.get(i).longitude;
            Double latDif = Math.abs(latitude-lat);
            Double lonDif = Math.abs(longitude-lon);
            distances.add(latDif+lonDif);
        }

        Double maxVal = Collections.min(distances); // should return 7
        Integer maxIdx = distances.indexOf(maxVal);

        val = place_names.get(maxIdx);
//        val = distances.get(maxIdx).toString();
        return val;
    }


    public static WeatherObj getWeather(String city) throws IOException {
        URL url;
        WeatherObj weather;
        String xmlString = "null";
        String summary = "";
        String precipitation = "";

        try {
            url = new URL("ftp://ftp.bom.gov.au/anon/gen/fwo/IDT16710.xml");

            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            xmlString="";
            final StringBuffer buffer = new StringBuffer(2048);
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                xmlString += inputLine;
            in.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not find on server.");
            System.exit(0);
        }catch (Exception e) {
            e.printStackTrace();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlString)));
            XPath xpath = XPathFactory.newInstance().newXPath();
            Node area = (Node) xpath.evaluate("//product/forecast/area[@description="+city+"]",document,XPathConstants.NODE);
            Node forecast = (Node) xpath.evaluate("forecast-period[@index=\"0\"]",area,XPathConstants.NODE);
            Node text = (Node) xpath.evaluate("text[@type=\"precis\"]",forecast,XPathConstants.NODE);
            summary = text.getTextContent();
            text = (Node) xpath.evaluate("text[@type=\"probability_of_precipitation\"]",forecast,XPathConstants.NODE);
            precipitation = text.getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        weather = new WeatherObj(summary,precipitation);
        return weather;
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("data/cities.geojson");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}

