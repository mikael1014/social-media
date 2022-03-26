package com.example.social_media.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.social_media.HomeActivity;
import com.example.social_media.NewPublicationActivity;
import com.example.social_media.services.ImageUpload;
import com.example.social_media.PublicationEpreuveActivity;
import com.example.social_media.PublicationMediaActivity;
import com.example.social_media.R;
import com.example.social_media.models.Publication;
import com.example.social_media.services.Config;
import com.example.social_media.services.PublicationService;
import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Publication> publications;

    private Context context;

    private int selectedItemPosition;

    private SharedPreferences prefs;
    PublicationService pubService = Config.getApiClient().create(PublicationService .class);

    public PublicationAdapter(Context context, List<Publication> publications) {
        this.publications = publications;
        this.context = context;
        prefs = context.getSharedPreferences("com.example.social_media", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        View view;
        switch (viewType) {
            case 0:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.item_publication_epreuve, parent, false);
                return new PublicationEpreuveViewHolder(view);
            case 1:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.item_publication_media, parent, false);
                return new PublicationMediaViewHolder(view);

        }
        return super.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object publication = publications.get(position);
        LinkedTreeMap<Object, Object> pub = (LinkedTreeMap) publication;
        LinkedTreeMap<Object, Object> auth = (LinkedTreeMap) pub.get("publisher");
        Long idPublisher = ((Double) auth.get("id")).longValue();
        Long idUser =  prefs.getLong("id", 0L);
        Long idPublication = ((Double) pub.get("id")).longValue();
        switch (holder.getItemViewType()) {
            case 0:
                PublicationEpreuveViewHolder epreuveHolder = (PublicationEpreuveViewHolder) holder;
                epreuveHolder.autheur.setText(auth.get("nom").toString() + " " + auth.get("prenom").toString());
                epreuveHolder.contenu.setText(pub.get("texte").toString());
                epreuveHolder.lien.setText(pub.get("lien").toString());
                epreuveHolder.category.setText(pub.get("categorieEpreuve").toString());
                //String s = new SimpleDateFormat("dd/MM/yyyy").format(pub.get("datePassage").toString());
                epreuveHolder.date.setText(pub.get("datePassage").toString());
                epreuveHolder.classe.setText(pub.get("classe").toString());
                if (auth.get("avatar") != null)
                    new ImageUpload(epreuveHolder.avatar).execute(Config.BASE_URL + auth.get("avatar").toString());
                else
                    epreuveHolder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.user_man));
                //details de la publication epreuve
                holder.itemView.setOnClickListener(v -> {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, PublicationEpreuveActivity.class);
                    intent.putExtra("id", Long.valueOf(pub.get("id").toString()));
                    context.startActivity(intent);
                });
                if (idPublisher == idUser) {
                    epreuveHolder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //creating a popup menu
                            PopupMenu popup = new PopupMenu(context, epreuveHolder.buttonViewOption);
                            //inflating menu from xml resource
                            popup.inflate(R.menu.publication_menu_option);
                            //adding click listener
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.itemEditer:
                                            Log.i("publication à éditer", idPublication + "");
                                            Intent intent = new Intent(context, NewPublicationActivity.class);
                                            intent.putExtra("editMode", 1);
                                            intent.putExtra("idPublication", idPublication);
                                            intent.putExtra("typePublication", 0);
                                            context.startActivity(intent);
                                            return true;
                                        case R.id.itemSupprimer:
                                            new AlertDialog.Builder(view.getContext())
                                                    .setTitle("Supprimer une publication")
                                                    .setMessage("Voulez-vous supprimer?")
                                                    .setPositiveButton(android.R.string.yes, (dialogInterface, i)
                                                            -> {
                                                        Log.i("Yes", "Bouton Yes cliqué");
                                                        pubService.deletePublication(idPublication).enqueue(new Callback<Void>() {
                                                            @Override
                                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                                Log.i("response", response.toString());
                                                                Intent intent = new Intent(context, HomeActivity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                context.startActivity(intent);
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Void> call, Throwable t) {
                                                                t.printStackTrace();
                                                            }
                                                        });
                                                    })
                                                    .setNegativeButton(android.R.string.no, null)
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            //displaying the popup
                            popup.show();
                        }
                    });
                } else {
                    epreuveHolder.buttonViewOption.setVisibility(View.GONE);
                }

                break;

            case 1:
                PublicationMediaViewHolder mediaHolder = (PublicationMediaViewHolder) holder;
                mediaHolder.autheur.setText(auth.get("nom").toString() + " " + auth.get("prenom").toString());
                mediaHolder.contenu.setText(pub.get("texte").toString());
                if (auth.get("avatar") != null)
                    new ImageUpload(mediaHolder.avatar).execute(Config.BASE_URL + auth.get("avatar").toString());
                else
                    mediaHolder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.user_man));
                if (pub.get("referenceMedia") != null) {
                    new ImageUpload(mediaHolder.image).execute(Config.BASE_URL + pub.get("referenceMedia").toString());
                    //publication sans media ?
                } else {
                    mediaHolder.image.setVisibility(View.GONE);
                }
                //details de la publication media
                mediaHolder.itemView.setOnClickListener(v -> {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, PublicationMediaActivity.class);
                    intent.putExtra("id", idPublication);
                    context.startActivity(intent);
                });
                if (idPublisher == idUser) {
                    mediaHolder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //creating a popup menu
                            PopupMenu popup = new PopupMenu(context, mediaHolder.buttonViewOption);
                            //inflating menu from xml resource
                            popup.inflate(R.menu.publication_menu_option);
                            //adding click listener
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.itemEditer:
                                            Log.i("publication à éditer", idPublication + "");
                                            Intent intent = new Intent(context, NewPublicationActivity.class);
                                            intent.putExtra("editMode", 1);
                                            intent.putExtra("idPublication", idPublication);
                                            intent.putExtra("typePublication", 1);
                                            context.startActivity(intent);
                                            return true;
                                        case R.id.itemSupprimer:
                                            new AlertDialog.Builder(view.getContext())
                                                    .setTitle("Supprimer une publication")
                                                    .setMessage("Voulez-vous supprimer?")
                                                    .setPositiveButton(android.R.string.yes, (dialogInterface, i)
                                                            -> {
                                                        Log.i("Yes", "Bouton Yes cliqué");
                                                        pubService.deletePublication(idPublication).enqueue(new Callback<Void>() {
                                                            @Override
                                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                                Log.i("response", response.toString());
                                                                Intent intent = new Intent(context, HomeActivity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                context.startActivity(intent);
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Void> call, Throwable t) {
                                                                t.printStackTrace();
                                                            }
                                                        });
                                                    })
                                                    .setNegativeButton(android.R.string.no, null)
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            //displaying the popup
                            popup.show();
                        }
                    });
                } else {
                    mediaHolder.buttonViewOption.setVisibility(View.GONE);
                }
                break;

        }

    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }

    @Override
    public int getItemCount() {
        return publications.size();
    }

    @Override
    public int getItemViewType(int position) {  //type de la publication //vérifier le champs date de passsage  not null
        Object publication = publications.get(position);
        LinkedTreeMap<Object, Object> pub = (LinkedTreeMap) publication;
        if (pub.get("datePassage") != null)
            return 0;
        else return 1;
    }

    public static class PublicationMediaViewHolder extends RecyclerView.ViewHolder {

        public TextView autheur;
        public TextView contenu;
        public ImageView avatar;
        public ImageView image;
        public TextView buttonViewOption;

        public PublicationMediaViewHolder(@NonNull View itemView) {
            super(itemView);
            autheur = itemView.findViewById(R.id.authorPub);
            contenu = itemView.findViewById(R.id.textPub);
            avatar = itemView.findViewById(R.id.avatarPub);
            image = itemView.findViewById(R.id.imagePub);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
        }
    }

    public static class PublicationEpreuveViewHolder extends RecyclerView.ViewHolder  {
        public ImageView avatar;
        public TextView autheur;
        public TextView contenu;
        public TextView lien;
        public TextView date;
        public TextView category;
        public TextView classe;
        public TextView buttonViewOption;


        public PublicationEpreuveViewHolder(@NonNull View itemView) {
            super(itemView);
            autheur = itemView.findViewById(R.id.authorPub);
            contenu = itemView.findViewById(R.id.textPubEpreuve);
            lien = itemView.findViewById(R.id.textViewLien);
            date = itemView.findViewById(R.id.textViewDatePassage);
            category = itemView.findViewById(R.id.textViewCategorie);
            classe = itemView.findViewById(R.id.textViewClasse);
            avatar = itemView.findViewById(R.id.avatarPub);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
        }

    }
}

