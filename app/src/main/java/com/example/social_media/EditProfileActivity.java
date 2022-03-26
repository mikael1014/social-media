package com.example.social_media;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.social_media.models.RoleUtilisateur;
import com.example.social_media.models.Sexe;

import com.example.social_media.models.Utilisateur;
import com.example.social_media.services.Config;
import com.example.social_media.services.FunctionsUtils;

import com.example.social_media.services.SessionManager;
import com.example.social_media.services.UserService;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private UserService userService;
    private SessionManager sessionManager;
    SharedPreferences prefs;
    private ImageView imageView;
    private Uri selectedImage;
    private File file;
    public static final int PICK_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userService = Config.getApiClient().create(UserService.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Modifier son profile");
        prefs = getSharedPreferences("com.example.social_media", Context.MODE_PRIVATE);
        imageView = findViewById(R.id.avatarEdit);
    }

    public void openImagePicker(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            selectedImage = data.getData();
            imageView.setImageURI(selectedImage);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateUser(View view) throws IOException {
        EditText editNom = findViewById(R.id.editProfileFirstName);
        String nom = editNom.getText().toString();

        EditText editPrenom = findViewById(R.id.editProfileLastName);
        String prenom = editPrenom.getText().toString();

        EditText editEmail = findViewById(R.id.editProfileEmail);
        String email = editEmail.getText().toString();

        EditText editPhone = findViewById(R.id.editProfileTelphone);
        String phone = editPhone.getText().toString();

        EditText editDesc = findViewById(R.id.editProfileDescription);
        String desc = editDesc.getText().toString();

        MultipartBody.Part avatar = null;
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioProfileGroupSexe);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        Sexe sexe = null;
        if (radioButton != null) {
            String sexeValue = radioButton.getText().toString().trim();
            sexe = (sexeValue.equals("Homme")) ? Sexe.MASCULIN: Sexe.FEMENIN;
        }
        if (selectedImage != null) {

            file = FunctionsUtils.getFileFromUri(view.getContext(), selectedImage);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse(getContentResolver().getType(selectedImage)), file);


           avatar =
                    MultipartBody.Part.createFormData("media", file.getName(),requestFile);

        }
        Utilisateur utilisateur = new Utilisateur(nom, prenom, email, RoleUtilisateur.USER, sexe, phone, desc);
        userService.updateUser(utilisateur, avatar).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("profile update" , response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}