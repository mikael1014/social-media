package com.example.social_media.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.social_media.services.ImageUpload;
import com.example.social_media.R;
import com.example.social_media.models.Utilisateur;
import com.example.social_media.services.Config;
import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

public class UserGridAdapter extends ArrayAdapter<Utilisateur> {
    private Context context;
    private int resource;

    public UserGridAdapter(@NonNull Context context, int resource, @NonNull List<Utilisateur> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Object personne = getItem(position);
        LinkedTreeMap<Object,Object> t = (LinkedTreeMap) personne;
        String nom = t.get("nom").toString();
        String prenom = t.get("prenom").toString();
        String avatar = t.get("avatar").toString();;
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);
        TextView personneName = convertView.findViewById(R.id.personName);
        personneName.setText(nom + " " + prenom);
        ImageView avatarView = convertView.findViewById(R.id.avatarUser);
        new ImageUpload(avatarView).execute(Config.BASE_URL + avatar);
//       I
        return convertView;
    }
}
