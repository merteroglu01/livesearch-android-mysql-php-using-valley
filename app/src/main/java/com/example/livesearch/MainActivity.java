package com.example.livesearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.livesearch.Adapters.FishAdapter;
import com.example.livesearch.AppConfig.AppConfig;
import com.example.livesearch.AppController.AppController;
import com.example.livesearch.Model.FishModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRVFish;
    private FishAdapter mAdapter;
    final List<FishModel> data = new ArrayList<>();
    private static final String TAG = MainActivity.class.getSimpleName();
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        searchView = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // adds item to action bar
        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Get Search item from action bar
        // and get search service
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            searchView.setIconified(true);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
                if(!searchView.getQuery().toString().isEmpty())
                    refreshInfo();
//              }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
    @Override
    protected void onNewIntent(Intent intent) {
        // Get search query and create object of class AsyncFetch
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }

        }
    }


    private void refreshInfo() {
        // Tag used to cancel the request
        String requestTag = "fetchFish";
        if(!data.isEmpty())
            data.clear();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.LIVE_SEARCH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                if(response.equals("No records found.")) {
                    Toast.makeText(MainActivity.this, "No Results found for entered query", Toast.LENGTH_LONG).show();
                }
                else{

                    try {

                        JSONArray jArray = new JSONArray(response);

                        // Extract data from json and store into ArrayList as class objects
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json_data = jArray.getJSONObject(i);
                            FishModel fishData = new FishModel();
                            fishData.fishName = json_data.getString("fishname");
                            fishData.category = json_data.getString("category");
                            fishData.size = json_data.getInt("size");
                            fishData.price = json_data.getInt("price");
                            data.add(fishData);
                        }

                        // Setup and handover data to recyclerview
                        mRVFish = (RecyclerView) findViewById(R.id.fishPriceList);
                        mAdapter = new FishAdapter(getApplicationContext(), data);
                        mRVFish.setAdapter(mAdapter);
                        mRVFish.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                    } catch (JSONException e) {
                        // You to understand what actually error is and handle it
                        Log.e(TAG,e.toString());
                    }

                }
            }}, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Request Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to livesearch url
                Map<String, String> params = new HashMap<String, String>();
                params.put("queryString", searchView.getQuery().toString());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, requestTag);

    }
}
