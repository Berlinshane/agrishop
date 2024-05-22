package com.example.agrishop;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class FarmersActivity extends AppCompatActivity {
    TextView tv1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers);
        tv1 = findViewById(R.id.textView1);

        parseXMLFromAssets("file.xml");
        try {
            parseJSONFromAssets("file.json");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseXMLFromAssets(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            NodeList nList = doc.getElementsByTagName("farmer");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    tv1.append("\nName : " + getValue("name", element) + "\n");
                    tv1.append("Phone : " + getValue("phone", element) + "\n");
                    tv1.append("Address : " + getValue("address", element) + "\n");
                    tv1.append("UPI ID : " + getValue("upi_id", element) + "\n");
                    tv1.append("-----------------------\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJSONFromAssets(String fileName) throws JSONException {
        try {
            InputStream is = getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            JSONArray farmersArray = new JSONObject(sb.toString()).getJSONArray("farmers");
            for (int i = 0; i < farmersArray.length(); i++) {
                JSONObject farmerObject = farmersArray.getJSONObject(i);
                tv1.append("\nName : " + farmerObject.getString("name") + "\n");
                tv1.append("Phone : " + farmerObject.getString("phone") + "\n");
                tv1.append("Address : " + farmerObject.getString("address") + "\n");
                tv1.append("UPI ID : " + farmerObject.getString("upi_id") + "\n");
                tv1.append("-----------------------\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}
