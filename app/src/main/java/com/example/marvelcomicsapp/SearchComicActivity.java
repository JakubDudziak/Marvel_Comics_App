package com.example.marvelcomicsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

public class SearchComicActivity extends AppCompatActivity {
    TextView textView;
    SearchView searchView;
    BottomNavigationView bottomNavigationView;
    RecyclerView  recyclerView;
    ArrayList<ComicModel> allComicsList = new ArrayList<>();
    ArrayList<ComicModel> searchList;
    String api = "https://gateway.marvel.com/v1/public/" +
            "comics?limit=100&format=comic&ts=1" +
            "&apikey=977888e445cee33b47b9ee6acb72d439" +
            "&hash=2112d097d4cbe3d87a60196784ffc30c";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_comic);

        getData();
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchComicActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        RecycleViewAdapter adapter = new RecycleViewAdapter(SearchComicActivity.this, allComicsList);
        recyclerView.setAdapter(adapter);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.search_comic);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchList = new ArrayList<>();

                if(query.length() > 0){
                    for (int i = 0; i < allComicsList.size(); i++) {
                        if (allComicsList.get(i).getTitle().toLowerCase().contains(query.toLowerCase())){
                            ComicModel comicModel = new ComicModel();
                            comicModel.setTitle(allComicsList.get(i).getTitle());
                            comicModel.setDescription(allComicsList.get(i).getDescription());
                            comicModel.setThumbnail(allComicsList.get(i).getThumbnail());
                            searchList.add(comicModel);
                        }
                    }
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchComicActivity.this);
                    recyclerView.setLayoutManager(layoutManager);

                    RecycleViewAdapter adapter = new RecycleViewAdapter(SearchComicActivity.this, searchList);
                    recyclerView.setAdapter(adapter);
                } else {

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchComicActivity.this);
                    recyclerView.setLayoutManager(layoutManager);

                    RecycleViewAdapter adapter = new RecycleViewAdapter(SearchComicActivity.this, allComicsList);
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList = new ArrayList<>();

                if(newText.length() > 0){
                    searchList.clear();
                    for (int i = 0; i < allComicsList.size(); i++) {
                        if (allComicsList.get(i).getTitle().toLowerCase().contains(newText.toLowerCase())){
                            ComicModel comicModel = new ComicModel();
                            comicModel.setTitle(allComicsList.get(i).getTitle());
                            comicModel.setDescription(allComicsList.get(i).getDescription());
                            comicModel.setThumbnail(allComicsList.get(i).getThumbnail());
                            searchList.add(comicModel);
                        }
                    }
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchComicActivity.this);
                    recyclerView.setLayoutManager(layoutManager);

                    RecycleViewAdapter adapter = new RecycleViewAdapter(SearchComicActivity.this, searchList);
                    recyclerView.setAdapter(adapter);

                    if (searchList.size() <= 0) {
                        displayToast();
                    }

                } else {

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchComicActivity.this);
                    recyclerView.setLayoutManager(layoutManager);

                    RecycleViewAdapter adapter = new RecycleViewAdapter(SearchComicActivity.this, allComicsList);
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }
        });
        //Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.search_comic:
                        return true;
                }
                return false;
            }
        });


    }


    //Todo add paging
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
                                                singleObject.getJSONObject("thumbnail").getString("path") + "/portrait_uncanny." +
                                                        singleObject.getJSONObject("thumbnail").getString("extension")
                                        )
                                );
                                if (singleModel.getDescription().isEmpty()) {
                                    singleModel.setDescription("No description");
                                }

                                allComicsList.add(singleModel);
                                System.out.println(singleModel.getThumbnail());
                            }

                            recyclerView.setAdapter(new RecycleViewAdapter(SearchComicActivity.this, allComicsList));

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

    public void displayToast() {
        Toast.makeText(SearchComicActivity.this, "No results", Toast.LENGTH_SHORT).show();
    }

}