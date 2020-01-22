package com.projet.easybusiness.recycler_view_helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projet.easybusiness.Annonce;
import com.projet.easybusiness.R;
import com.projet.easybusiness.helper_request.HelperClass;
import com.squareup.picasso.Picasso;

public class AnnonceViewHolder extends RecyclerView.ViewHolder {

    private TextView titre;
    private TextView date;
    private TextView id;
    private TextView description;
    private ImageView img;
    public AnnonceViewHolder(@NonNull View itemView) {
        super(itemView);
       titre = (TextView)itemView.findViewById(R.id.titre);
       date= (TextView)itemView.findViewById(R.id.date);
       description=(TextView)itemView.findViewById(R.id.description);
       id= (TextView)itemView.findViewById(R.id.idAnnonce);
       img = itemView.findViewById(R.id.imgAd);

        // initialisation des items de la vue
    }

    public void bind(Annonce ad) {
        titre.setText(ad.getTitre());
        date.setText(HelperClass.formatDate(ad.getDate()));
        description.setText(ad.getDescription().substring(0,60)+"....");
        id.setText(ad.getId());
        Picasso.get().load(ad.getImages()[0]).error(R.drawable.laptop_hp).into(img);
    }
}