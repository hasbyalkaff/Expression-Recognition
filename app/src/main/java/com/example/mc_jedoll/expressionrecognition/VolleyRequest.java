package com.example.mc_jedoll.expressionrecognition;

import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequest {
    private String TAG = "Volley_TAG";
    private String url = "http://172.20.10.2:5000";
    private RequestQueue mRequestQueue;
    private Context context;
    //private JSONObject data;

    public VolleyRequest(Context context){
        this.context = context;
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        this.mRequestQueue = new RequestQueue(cache, network);
        this.mRequestQueue.start();
    }

    public void methodGET(){
        this.mRequestQueue.add(this.stringRequest);
    }

    public void methodPOST(){
        this.mRequestQueue.add(this.postRequest);
    }

    private StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "/",
            new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.i(TAG, response);
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, error.toString());
            error.printStackTrace();
        }
    });

    public void jsonPOST(){
        JSONObject data = new JSONObject();
        try {
            data.put("image", "12345678");
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url + "/json",
                data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                error.printStackTrace();
            }
        });

        this.mRequestQueue.add(jsonObjectRequest);
    }

    private StringRequest postRequest = new StringRequest(Request.Method.POST, url + "/json",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i(TAG, response);
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, error.toString());
            error.printStackTrace();
        }
    }){
        @Override
        public Map<String, String> getHeaders() {
            Map<String, String>  params = new HashMap<String, String>();
            params.put("Content-Type", "application/json");
            return params;
        }
        @Override
        protected Map<String, String> getParams(){
            Map<String,String> params = new HashMap<>();
            // POST Parameter
            params.put("image", "1234567");
            return params;
        }
    };
}
