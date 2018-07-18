package com.ybj.basedemo.okhttps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ybj.basedemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request build = new Request.Builder()
                        .url("https://www.baidu.com/")
                        .build();
                try {
                    Response response = okHttpClient.newCall(build).execute();
                    okHttpClient.newCall(build).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                        }
                    });
                    String requestBody = response.body().toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void parseXMLWithPull(String data){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(data));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT :
                        if("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        }else if("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        }else if("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        if("app".equals(nodeName)) {
                            Log.e("TAG", "id is " + id);
                            Log.e("TAG", "name is " + name);
                            Log.e("TAG", "version is " + version);
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseXMLWithSAX(String data){
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            ContentHandler handler = new ContentHandler();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(new StringReader(data)));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseJsonWithJSONObject(String data){
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0 ; i < jsonArray.length() ; i ++){
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString("id");
                String name = object.getString("name");
                String version = object.getString("version");
                Log.e("TAG", "id is " + id);
                Log.e("TAG", "name is " + name);
                Log.e("TAG", "version is " + version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseGsonWithJSONObject(String data){
        List<App> listInfo = new Gson().fromJson(data, new TypeToken<List<App>>() {
        }.getType());
        for (App app : listInfo){
            Log.e("TAG", "id is " + app.getId());
            Log.e("TAG", "name is " + app.getName());
            Log.e("TAG", "version is " + app.getVersion());
        }
    }



}
