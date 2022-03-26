package com.example.social_media;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.social_media.adapters.LoginAdapter;
import com.example.social_media.models.LoginRequest;
import com.example.social_media.models.RoleUtilisateur;
import com.example.social_media.models.Sexe;
import com.example.social_media.models.Utilisateur;
import com.example.social_media.services.AuthorizationInterceptor;
import com.example.social_media.services.Config;
import com.example.social_media.services.SessionManager;
import com.example.social_media.services.UserService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.Interceptor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnexionActivity extends AppCompatActivity {

    public ViewPager2 viewPager;
    public TabLayout tabLayout;
    private UserService userService;
    private SessionManager sessionManager;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        prefs = getSharedPreferences("com.example.social_media", Context.MODE_PRIVATE);
        userService = Config.getApiClient().create(UserService.class);
        setContentView(R.layout.activity_login);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Se connecter"));
        tabLayout.addTab(tabLayout.newTab().setText("s'enregistrer"));
        viewPager = findViewById(R.id.view_page);

        final LoginAdapter loginAdapter = new LoginAdapter(this, tabLayout.getTabCount());
        viewPager.setAdapter(loginAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.setScrollPosition(position, 0f, false);
            }
        });
    }



    public void handleConnection(View view) {
        TextInputEditText editTextEmail = findViewById(R.id.editEmail);
        TextInputEditText editTextPassword = findViewById(R.id.editPassword);
        String mail = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        LoginRequest loginRequest = new LoginRequest(mail, password);

        userService.login(loginRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //Log.i("Token", response.headers().values("Authorization").get(0));
                Log.i("response", String.valueOf(response));
                if (response.code() == 200) {
                    String token = response.headers().get("Authorization");
                    sessionManager.saveAuthToken(token);
                    sessionManager.saveUserDetails();
                    // interceptor
                    Interceptor auth = new AuthorizationInterceptor(token);
                    //set header iinterceptor
                    userService = Config.setInterceptor(auth).create(UserService.class);
                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Echec Connexion")
                            .setMessage("Email ou mot de passe incorrect")
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    public void handleRegister(View view) {

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioProfileGroupSexe);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        TextInputEditText editTextNom = findViewById(R.id.editProfileFirstName);
        TextInputEditText editTextPrenom = findViewById(R.id.editProfileLastName);
        TextInputEditText editTextEmail = findViewById(R.id.editProfileEmail);
        TextInputEditText editTextPassword = findViewById(R.id.editNewPassword);
        TextInputEditText editTextPasswordConfirm = findViewById(R.id.editConfirmPassword);

        String sexeValue = radioButton.getText().toString().trim();
        Log.i("radio", sexeValue);
        String nom = editTextNom.getText().toString().trim();
        String prenom = editTextPrenom.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordConfirm = editTextPasswordConfirm.getText().toString().trim();
        Sexe sexe = (sexeValue.equals("Homme")) ? Sexe.MASCULIN : Sexe.FEMENIN;
        Log.i("password", String.valueOf(sexe));

        if (!password.equals(passwordConfirm)) {

        } else {
            Utilisateur utilisateur = new Utilisateur(nom, prenom, email, password, RoleUtilisateur.USER, sexe);
            userService.register(utilisateur, null).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i("response", String.valueOf(response));
                    if (response.code() == 201) {
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Success")
                                .setMessage("votre compte a été crée avec succès\n un mail de validation est envoyé à l'adresse renseigné\n veuillez activer votre compte pour se connecter")
                                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> Log.i("Yes", "Bouton Yes cliqué"))
                                .setIcon(R.drawable.ic_check_circle)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();

                }
            });

        }

    }
}