package com.example.marvelcomicsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ArrayList<ComicModel> allComicsList = new ArrayList<>();
    RecyclerView recyclerViewMain;

    String api = "https://gateway.marvel.com/v1/public/" +
            "comics?limit=100&format=comic&ts=1" +
            "&apikey=977888e445cee33b47b9ee6acb72d439" +
            "&hash=2112d097d4cbe3d87a60196784ffc30c";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewMain = findViewById(R.id.recycler_view);
        getData();
        RecycleViewAdapter adapter = new RecycleViewAdapter(this, allComicsList);
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMain.setAdapter(adapter);
        bottomNavigationView =  findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);


         //Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.search_comic:
                        startActivity(new Intent(getApplicationContext(),SearchComicActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });
    }

    private void getData() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //here we take a json from Marvel api
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray results = data.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject singleObject = results.getJSONObject(i);
                                ComicModel singleModel = new ComicModel(
                                        singleObject.getString("title"),
                                        singleObject.getString("description"),
                                        (
                                                singleObject.getJSONObject("thumbnail").getString("path") + "/portrait_medium." +
                                                        singleObject.getJSONObject("thumbnail").getString("extension")
                                        )
                                );
                                if (singleModel.getDescription().isEmpty()) {
                                    singleModel.setDescription("No description");
                                }
                                allComicsList.add(singleModel);

                            }

                            recyclerViewMain.setAdapter(new RecycleViewAdapter(MainActivity.this, allComicsList));

                            Log.e("api", "onResponse" + allComicsList.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("api", "onResponse " + e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("api", "OnErrorResponse" + error.getLocalizedMessage());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

//



}