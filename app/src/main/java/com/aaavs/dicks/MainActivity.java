package com.aaavs.dicks;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    private static final String url = "https://movesync-qa.dcsg.com/dsglabs/mobile/api/venue/";

    // Hard coded value for initial location
    Double latitude1 = 40.572647;
    Double longitude1 = -79.793396;

    float distanceInMeters;

    ExpandableListView expandableListView;


    ArrayList<String> listOfAddresses = new ArrayList<>();


    TreeMap<String, String> sorted_map = new TreeMap<>();

    // Global vairiable for  Starting Address Lat n Long
    Location loc1 = new Location("");

    // Global vairiable for Destination Address Lat n Long
    Location loc2 = new Location("");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loc1.setLatitude(latitude1);
        loc1.setLongitude(longitude1);
        fetchPosts();

//        expandableListView = findViewById(R.id.expandableLV);



    }


    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, url, onPostsLoaded, onPostsError);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("venues");

                    // looping through All Venues
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");

                        // Location node in JSON Object
                        JSONObject location = c.getJSONObject("location");
                        Double latitude = location.getDouble("latitude");
                        Double longitude = location.getDouble("longitude");
                        String address = location.getString("address");

//                        listOfAddresses.add(address);

                        loc2.setLatitude(latitude);
                        loc2.setLongitude(longitude);

                        // Calculating the distance in metres between the coordinates.
                        distanceInMeters = (loc1.distanceTo(loc2));
                        // Adding distance to Tree Map

                        sorted_map.put((String.valueOf(distanceInMeters))+ " (meters)"," "+address);

                    }
                }


                catch (final JSONException e) {
                    Log.e("Show", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            addDataToListView();
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
        }
    };


    private void addDataToListView() {

//
//        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this,listOfAddresses,sorted_map);
//
//        expandableListView.setAdapter(expandableListAdapter);
//
//        Log.i("Expnad","done");
//        Log.i("Expnad", String.valueOf(sorted_map));


        CustomAdapter adapter = new CustomAdapter(sorted_map);
        ListView mListView = findViewById(R.id.listView);
        mListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Iterator iterator = sorted_map.keySet().iterator();
                while (iterator.hasNext()) {
                    Object key = iterator.next();

                    //adding address value from TreeMap to ArrayList
                    listOfAddresses.add(sorted_map.get(key));

                }

                // gets The address info from arraylist based on click position
                String destination = String.valueOf(listOfAddresses.get(i));

                // Opens google map upon click
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+destination+"&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }

        });
    }

}
