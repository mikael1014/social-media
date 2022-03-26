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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.social_media.models.Publication;
import com.example.social_media.models.Sexe;
import com.example.social_media.models.Visibilite;
import com.example.social_media.services.Config;
import com.example.social_media.services.FunctionsUtils;
import com.example.social_media.services.ImageUpload;
import com.example.social_media.services.PublicationService;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPublicationActivity extends AppCompatActivity {
    private PublicationService publicationService;
    SharedPreferences prefs;
    private Uri selectedImage;
    private File file;
    private EditText texte;
    private ImageView imageView;
    public static final int PICK_IMAGE = 1;
    private int editMode;
    private int publicationType;
    private long idPublication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_publication);
        publicationService = Config.getApiClient().create(PublicationService.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nouvelle publication");
        prefs = getSharedPreferences("com.example.social_media", Context.MODE_PRIVATE);
        texte = findViewById(R.id.editTextePublication);
        imageView = findViewById(R.id.imagePreview);
        idPublication = getIntent().getLongExtra("idPublication", 0);
        editMode = getIntent().getIntExtra("editMode", 0);
        publicationType = getIntent().getIntExtra("typePublication", 0);
        if (editMode == 1) {
            initEditTexts(idPublication);
            Button button = (Button) findViewById(R.id.savePublication);
            button.setText("Mettre à jour");
        }
    }

    private void initEditTexts(long idPublication) {
        publicationService.getPublicationById(idPublication).enqueue(new Callback<Publication>() {
            @Override
            public void onResponse(Call<Publication> call, Response<Publication> response) {
                Publication publication = response.body();
                Log.i("response", publication.toString());
                texte.setText(publication.getTexte());
                if (publication.getReferenceMedia() != null)
                    new ImageUpload(imageView).execute(Config.BASE_URL + publication.getReferenceMedia());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            }

            @Override
            public void onFailure(Call<Publication> call, Throwable t) {
                t.printStackTrace();

            }
        });
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
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageURI(selectedImage);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void savePublication(View view) throws IOException {
        MultipartBody.Part media = null;
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPublicationVisibilite);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        Visibilite visibilite = null;
        if (radioButton != null) {
            String visibiliteValue = radioButton.getText().toString().trim();
            visibilite = (visibiliteValue.equals("public")) ? Visibilite.PUBLIC: Visibilite.PRIVEE;
        }
        if (selectedImage != null) {

            file = FunctionsUtils.getFileFromUri(view.getContext(), selectedImage);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse(getContentResolver().getType(selectedImage)), file);


            media =
                    MultipartBody.Part.createFormData("media", file.getName(),requestFile);

        }
        String text = texte.getText().toString();
        Publication publication = new Publication(visibilite, text);
        if(editMode == 1) {
            publicationService.updatePublication(idPublication, publication, media).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.i("response", response.toString());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(view.getContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 3000);
                    Snackbar.make(view, "publication est mis à jour avec succes", Snackbar.LENGTH_SHORT)
                            .show();

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });


        } else {
            publicationService.savePublication(publication, media).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i("response", response.toString());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(view.getContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 3000);
                    Snackbar.make(view, "publication ajoutée avec succes", Snackbar.LENGTH_SHORT)
                            .show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();

                }
            });

        }


    }
}