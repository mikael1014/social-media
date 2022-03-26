package com.example.social_media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.social_media.adapters.PublicationAdapter;
import com.example.social_media.models.Publication;
import com.example.social_media.services.Config;
import com.example.social_media.services.ImageUpload;
import com.example.social_media.services.PublicationService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    public SharedPreferences prefs;
    private PublicationService pubService;
    List<Publication> publications = new ArrayList<>();
    RecyclerView recyclerView;
    PublicationAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        prefs = getSharedPreferences("com.example.social_media", Context.MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.navigation_open, R.string.navigation_close);
        toolbar.setNavigationIcon(R.drawable.ic_burger_menu);
        drawer.addDrawerListener(toggle);
        navigationView = findViewById(R.id.navView);
        this.initHeaderNav();
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();

        recyclerView = (RecyclerView) findViewById(R.id.publicationHomeView);
        recyclerView.setAdapter(new PublicationAdapter(this,publications));

        pubService = Config.getApiClient().create(PublicationService.class);
        this.initListPublication();
    }


    public void submitNewPublication(View view) {
        Intent intent = new Intent(getBaseContext(), NewPublicationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.navAccount:
                //Log.i("item1", item.getTitle().toString());
                Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                startActivity(intent);
                this.drawer.closeDrawer(GravityCompat.START);
                return true;
            case  R.id.navArticle:
                Log.i("item2", item.getTitle().toString());
                this.drawer.closeDrawer(GravityCompat.START);
                return true;
            case  R.id.navGroupe:
                Log.i("item3", item.getTitle().toString());
                this.drawer.closeDrawer(GravityCompat.START);
                return true;
            case  R.id.navMessage:
                Log.i("item4", item.getTitle().toString());
                this.drawer.closeDrawer(GravityCompat.START);
                return true;
            case  R.id.navShare:
                Log.i("item5", item.getTitle().toString());
                this.drawer.closeDrawer(GravityCompat.START);
                return true;
            case  R.id.navSend:
                Log.i("item6", item.getTitle().toString());
                this.drawer.closeDrawer(GravityCompat.START);
                return true;
            default:
                this.drawer.closeDrawer(GravityCompat.START);
                return super.onOptionsItemSelected(item);
        }
    }


    private void initHeaderNav() {
        View header = navigationView.getHeaderView(0);
        TextView profileName = header.findViewById(R.id.profileName);
        profileName.setText(prefs.getString("fullName", ""));
        TextView profileEmail = header.findViewById(R.id.profileEmail);
        profileEmail.setText(prefs.getString("email", ""));
        ImageView avatar = header.findViewById(R.id.avatar);
        Log.i("avatar", prefs.getString("avatar", ""));
        new ImageUpload(avatar).execute(Config.BASE_URL + prefs.getString("avatar", ""));

    }

    private void initListPublication() {
        pubService.getAllPublication().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Log.i("response", response.toString());
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                recyclerView.setHasFixedSize(true);
                TextView emptyView = findViewById(R.id.empty_view2);
                if (response.code() == 204) {   //no content
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);

                } else {
                    publications = (List<Publication>) response.body().get("publications");
                    Log.i("publication", publications.toString());

                    adapter = new PublicationAdapter(getBaseContext(), publications);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

}