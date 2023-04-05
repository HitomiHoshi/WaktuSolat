package com.hitomi.waktusolat.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hitomi.waktusolat.callback.JakimCallback;
import com.hitomi.waktusolat.data.Message;
import com.hitomi.waktusolat.data.WaktuSolatData;
import com.hitomi.waktusolat.data.WaktuSolatListData;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JakimService extends Service {

    private final RequestQueue requestQueue;
    private final String url;
    private final Context context;

    public JakimService(Context context) {
        this.context = context;
        url = "https://www.e-solat.gov.my/index.php?r=esolatApi/xmlfeed&zon=";
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getWaktuSolatByZon(String zon, final JakimCallback jakimCallback) {
        StringRequest request = new StringRequest(Request.Method.GET, url + zon,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<WaktuSolatData> waktuSolatArrayList = new ArrayList();
                        WaktuSolatData waktuSolat = new WaktuSolatData() ;
                        WaktuSolatListData waktuSolatList= new WaktuSolatListData();

                        try {
                            XmlPullParserFactory factory;
                            factory = XmlPullParserFactory.newInstance();
                            factory.setNamespaceAware(true);
                            XmlPullParser xpp = factory.newPullParser();
                            xpp.setInput( new StringReader( response ) );
                            int eventType = xpp.getEventType();
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                if(eventType == XmlPullParser.START_DOCUMENT) {
//                                    System.out.println("Start document");
                                }
                                else if(eventType == XmlPullParser.START_TAG) {
//                                    System.out.println("Start tag "+xpp.getName());
                                    if (xpp.getName().equals("date")){
                                        xpp.next();
                                        waktuSolatList.date = xpp.getText();
                                    }
                                    else if (xpp.getName().equals("item")){
                                        waktuSolat = new WaktuSolatData() ;
                                    }
                                    else if (xpp.getName().equals("title")){
                                        xpp.next();
                                        waktuSolat.title = xpp.getText();
                                    }
                                    else if (xpp.getName().equals("description")){
                                        xpp.next();
                                        waktuSolat.description = xpp.getText();
                                    }
                                }
                                else if(eventType == XmlPullParser.END_TAG) {
//                                    System.out.println("End tag "+xpp.getName());
                                    if (xpp.getName().equals("item")){
                                        waktuSolatArrayList.add(waktuSolat);
                                    }
                                }
                                else if(eventType == XmlPullParser.TEXT) {
//                                    System.out.println("Text "+xpp.getText());
                                }
                                eventType = xpp.next();
                            }
//                            System.out.println("End document");
                            waktuSolatList.waktuSolatList = waktuSolatArrayList;
                            jakimCallback.onSuccess(waktuSolatList);
                        } catch (XmlPullParserException | IOException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        error.getMessage();

//                        try {
//
//                            Message resp = new Message(new JSONObject(new String(error.networkResponse.data)));
//
//                            jakimCallback.onError(resp);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}