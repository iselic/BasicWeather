package com.example.chris.basicweather;

import android.content.Context;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class WeatherBOM {

    public static WeatherObj TodaysWeather(Context context){
        WeatherObj weather;

        // Get text summary
        String summary = "-";
        String xmlString = getXML("ftp://ftp.bom.gov.au/anon/gen/fwo/IDT13630.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();

            Document document = builder.parse(new InputSource(new StringReader(xmlString)));
            XPath xpath = XPathFactory.newInstance().newXPath();
            Node area = (Node) xpath.evaluate("//product/forecast/area[@description=\"Hobart\"]",document,XPathConstants.NODE);
            Node forecast = (Node) xpath.evaluate("forecast-period[@index=\"0\"]",area,XPathConstants.NODE);
            Node text = (Node) xpath.evaluate("text[@type=\"forecast\"]",forecast,XPathConstants.NODE);
            summary = text.getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get current observations
        xmlString = getXML("ftp://ftp.bom.gov.au/anon/gen/fwo/IDT60920.xml");
        String current_temp_text = "-";
        String app_temp_text = "-";
        factory = DocumentBuilderFactory.newInstance();

        try {
            builder = factory.newDocumentBuilder();

            Document document = builder.parse(new InputSource(new StringReader(xmlString)));
            XPath xpath = XPathFactory.newInstance().newXPath();
            Node area = (Node) xpath.evaluate("//product/observations/station[@description=\"Hobart\"]",document,XPathConstants.NODE);
            Node forecast = (Node) xpath.evaluate("period[@index=\"0\"]",area,XPathConstants.NODE);
            Node base = (Node) xpath.evaluate("level[@index=\"0\"]",forecast,XPathConstants.NODE);
            Node current_temp = (Node) xpath.evaluate("element[@type=\"air_temperature\"]",base,XPathConstants.NODE);
            current_temp_text = current_temp.getTextContent();
            Node app_temp = (Node) xpath.evaluate("element[@type=\"apparent_temp\"]",base,XPathConstants.NODE);
            app_temp_text = app_temp.getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get chance of rain
        xmlString = getXML("ftp://ftp.bom.gov.au/anon/gen/fwo/IDT16710.xml");
        String chance_precip_text = "-";
        String max_temp_text = "-";
        factory = DocumentBuilderFactory.newInstance();

        try {
            builder = factory.newDocumentBuilder();

            Document document = builder.parse(new InputSource(new StringReader(xmlString)));
            XPath xpath = XPathFactory.newInstance().newXPath();
            Node area = (Node) xpath.evaluate("//product/forecast/area[@description=\"Hobart\"]",document,XPathConstants.NODE);
            Node forecast = (Node) xpath.evaluate("forecast-period[@index=\"0\"]",area,XPathConstants.NODE);
            Node prob_precip = (Node) xpath.evaluate("text[@type=\"probability_of_precipitation\"]",forecast,XPathConstants.NODE);
            chance_precip_text = prob_precip.getTextContent();
//            Node max_temp = (Node) xpath.evaluate("element[@type=\"air_temperature_maximum\"]",forecast,XPathConstants.NODE);
//            max_temp_text = max_temp.getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        weather = new WeatherObj(summary,max_temp_text,current_temp_text,app_temp_text,chance_precip_text);

        return weather;
    }

    public static String getXML(String in_url){

        String xmlString = "";

        try {
            URL url = new URL(in_url);
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
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
        return xmlString;
    }
}
