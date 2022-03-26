package com.example.social_media;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.social_media.adapters.PublicationAdapter;
import com.example.social_media.adapters.UserGridAdapter;
import com.example.social_media.models.Publication;
import com.example.social_media.models.Utilisateur;
import com.example.social_media.services.Config;
import com.example.social_media.services.ImageUpload;
import com.example.social_media.services.UserService;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    public SharedPreferences prefs;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userService = Config.getApiClient().create(UserService.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mon profile");
        prefs = getSharedPreferences("com.example.social_media", Context.MODE_PRIVATE);
        this.initProfileHeader();
        this.initListAmis();
        this.initListPublication();


    }

    private void initListPublication() {
        userService.getListPublication().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Log.i("response", response.toString());
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.publicationView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                recyclerView.setHasFixedSize(true);
                TextView emptyView = findViewById(R.id.empty_view);
                if (response.code() == 204) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);

                } else {
                    List<Publication> publications = (List<Publication>) response.body().get("publications");
                    Log.i("publication", publications.toString());

                    PublicationAdapter adapter = new PublicationAdapter(getBaseContext(), publications);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });
    }

    private void initListAmis() {
        userService.getListAmis().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Log.i("response", response.toString());
                GridView gridView = (GridView) findViewById(R.id.freindsGrid);
                TextView emptyView = findViewById(R.id.empty_view1);
                if (response.code() == 204) {   //no content
                    gridView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    List<Utilisateur> amis = (List<Utilisateur>) response.body().get("utilisateurs");
                    Log.i("friends", amis.toString());
                    UserGridAdapter adapter = new UserGridAdapter(getBaseContext(), R.layout.item_user_profile, amis);
                    gridView.setAdapter(adapter);

                }


            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });
    }

    public void initProfileHeader() {
        TextView name = findViewById(R.id.profileName2);
        Log.i("text ", name.getText().toString());
        Log.i("refs", prefs.getString("fullName", ""));
        name.setText(prefs.getString("fullName", ""));
        TextView description = findViewById(R.id.profileDescription);
        description.setText(prefs.getString("description", ""));
        ImageView avatar = findViewById(R.id.avatarEdit);
        new ImageUpload(avatar).execute(Config.BASE_URL + prefs.getString("avatar", ""));

    }

    public void editProfile(View view) {
        Intent intent = new Intent(getBaseContext(), EditProfileActivity.class);
        intent.putExtra("idUser", prefs.getLong("id", 0L));
        startActivity(intent);
    }
}