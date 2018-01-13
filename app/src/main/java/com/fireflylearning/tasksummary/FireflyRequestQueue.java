package com.fireflylearning.tasksummary;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Ben on 11/07/2017.
 */

public class FireflyRequestQueue {

    private final String DeviceId = "AndroidApp";
    private final String GetParamDeviceId = "ffauth_device_id";
    private final String GetParamToken = "ffauth_secret";

    private static FireflyRequestQueue mInstance;

    private RequestQueue mRequestQueue;
    private Context mContext;
    private String mHost;
    private String mToken;

    public static void initialise(Context context, String host, String token)
    {
        mInstance = new FireflyRequestQueue();
        mInstance.mContext = context;
        mInstance.mHost = host;
        mInstance.mToken = token;
        mInstance.mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized FireflyRequestQueue getInstance() {
        return mInstance;
    }

    private String BuildUrl(String postfix) {

        String query = GetParamDeviceId + "=" + DeviceId + "&" + GetParamToken + "=" + mToken;

        if(postfix.contains("?")) {
            return "https://" + mHost + "/" + postfix + "&" + query;
        }

        return "https://" + mHost + "/" + postfix + "?" + query;
    }

    public void RunGetRequest(String relativeUrl, Listener<String> listener, ErrorListener errorListner) {

        StringRequest request = new StringRequest(
                Request.Method.GET,
                BuildUrl(relativeUrl),
                listener,
                errorListner);

        mRequestQueue.add(request);
    }

    public void RunGraphqlQuery(int queryResourceId, final Listener<JSONObject> listener, final ErrorListener errorListner) {

        final String query = mContext.getResources().getString(queryResourceId);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                BuildUrl("_api/1.0/graphql"),
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            listener.onResponse(json.getJSONObject("data"));
                        } catch (JSONException ex) {
                            errorListner.onErrorResponse(new VolleyError());
                        }
                    }
                },
                errorListner)
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", query);
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    public String createSession(String url){
        return BuildUrl("login/api/getsession") + "&Prelogin=/" + url;
    }
}
